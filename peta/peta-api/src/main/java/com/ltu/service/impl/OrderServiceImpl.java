package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.NFTConstant;
import com.ltu.domain.mp_entity.*;
import com.ltu.enums.EnumUtils;
import com.ltu.enums.OrderDict;
import com.ltu.mapper.OrderMapper;
import com.ltu.mapper.PayRecordMapper;
import com.ltu.model.request.collectionOrder.CreateOrderReq;
import com.ltu.model.request.collectionOrder.OrderPageReq;
import com.ltu.model.request.collectionOrder.PayReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.payment.PubPayService;
import com.ltu.service.*;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import com.ltu.util.exception.ApiException;

import com.ltu.util.redis.RedistUtil;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付订单 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-04
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, OrderEntity> implements OrderService {

	private final PayRecordMapper payRecordMapper;
	private final UserService userService;
	private final ArtCollectionService artCollectionService;
	private final OrderDetailService orderDetailService;
	private final AccountService accountService;


	@Autowired
	RedistUtil redistUtil;
	@Autowired
	UserCollectionsService userCollectionsService;
	@Autowired
	CollectionBlindBoxService collectionBlindBoxService;

	@Autowired
	UserJoinDropService userJoinDropService;


	private static final String USER_COLLECTION_ORDER="USER_COLLECTION_ORDER:";
	private static final Long ORDER_REDIS_TIME_OUT = 15*60L;

	@Override
	public CodeDataResp placeOrder(CreateOrderReq req) {

		UserEntity user = userService.getById(ShiroUtil.getPrincipalUserId());
	
		ArtCollectionEntity artCollectionEntity = artCollectionService.getById(req.getId());
		if (artCollectionEntity==null)
			return CodeDataResp.valueOfFailed("藏品数据非法");
//		if (artCollectionEntity.getSoldStatus()!= NFTConstant.SOLD_STATUS_SELLING)
//			return CodeDataResp.valueOfFailed("藏品不在销售时间段内");
		//预约抽签类型的藏品是否中签 、
		UserJoinDropEntity userJoinDropEntity = null;
		if (artCollectionEntity.getSoldType()==NFTConstant.SOLD_TYPE_BOOK_DROP){
			userJoinDropEntity = userJoinDropService.getUserDropRecordByCollection(artCollectionEntity.getId(),user.getId());
			if (userJoinDropEntity==null)
				return CodeDataResp.valueOfFailed("没有参加预约抽签");
			if (userJoinDropEntity.getDropStatus()!=1)
				return CodeDataResp.valueOfFailed("需要中签后抢购");
			if (userJoinDropEntity.getStatus()!=0)
				return CodeDataResp.valueOfFailed("已中签抢购");
		}

		// 限时抢购的判断库存剩余

		OrderEntity order =getRedisOrderByUser(artCollectionEntity.getId(),user.getId());
		if (order!=null&&order.getIsExpire()==0) {
			order.setCountDownTime(ORDER_REDIS_TIME_OUT - (DateUtils.currentSecs() - order.getCreateTime()));
		}
		else {
			order = new OrderEntity();
			order = createOrder(user, artCollectionEntity,req);
			order.setCountDownTime(ORDER_REDIS_TIME_OUT);
			setUserOrderToRedis(order);

		}
		if (userJoinDropEntity!=null){
			userJoinDropEntity.setOrderId(order.getId());
			userJoinDropService.saveOrUpdate(userJoinDropEntity);
		}

		return CodeDataResp.valueOfSuccess(order);
	}

	/**
	 * 创建订单支付记录
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public CodeDataResp createOrderPayRecord(PayReq req) {
		UserEntity user = userService.getById(ShiroUtil.getPrincipalUserId());
		if (user==null){
			return CodeDataResp.valueOfFailed("用户信息非法");
		}
		OrderEntity order = getRedisOrderByUser(req.getArtCollectionId(),user.getId());
		if (order==null)
			return CodeDataResp.valueOfFailed("不存在订单");
		//判断订单是否有效
		if (order.getIsExpire()==1)
			return CodeDataResp.valueOfFailed("订单已无效");
		//限时抢购需判断藏品库存


		PayRecordEntity payRecord = new PayRecordEntity();
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtils.format(new Date(), DateUtils.DATE_TIME_FORMAT_NUMBER))
				.append(StrUtils.generateZeroCharacter(user.getId().toString(), 4, "0")).append("0")
				.append(StrUtils.randomnum(1000, 9999));
		payRecord.setCreateTime(order.getCreateTime())
				.setOrderId(order.getId())
				.setOrderNo(order.getOrderNo())
				.setPayableFee(order.getPayableFee())
				.setPayWay(req.getPayWay())
				.setUserId(order.getUserId())
				.setUserName(order.getUserName())
				.setUserAvatar(order.getUserAvatar())
				.setOrderNo(order.getOrderNo())
				.setPayRecordType(1)
				.setTotalFee(order.getTotalFee())
				.setRebateFee(order.getRebateFee())
				.setAmount(order.getAmount())
				.setPayRecordNo(sb.toString())
				.setPayName(order.getCollectionName());
		payRecordMapper.insert(payRecord);

		//测试订单
		if (order.getCollectionOpenType()== NFTConstant.OEPN_TYPE_BLIND_BOX)
			collectionBlindBoxService.createBox(order);
		else
			userCollectionsService.createUserCollection(user,order);
		//JSONObject result = pubPayService.placeOrder(payRecord, "", "");
		return CodeDataResp.valueOfSuccess(payRecord);
	}

	/**
	 * 创建订单
	 * 
	 * @param user
	 * @param
	 * @return
	 */
	private OrderEntity createOrder(UserEntity user,ArtCollectionEntity artCollectionEntity,
									CreateOrderReq req) {
		OrderDict.OrderSource orderSource = EnumUtils.indexToEnum(OrderDict.OrderSource.class,
				artCollectionEntity.getSoldType());
		Date currentDate = new Date();
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtils.format(currentDate, DateUtils.DATE_TIME_FORMAT_NUMBER))
				.append(StrUtils.generateZeroCharacter(user.getId().toString(), 4, "0")).append("0")
				.append(StrUtils.randomnum(1000, 9999));
		OrderEntity order = new OrderEntity();
		order.setUserId(user.getId()).setUserName(user.getUserNick()).setUserAvatar(user.getUserAvatar())
				.setOrderNo(sb.toString()).setPayWay(1)
				.setStatus(1)
				.setMintStatus(NFTConstant.MINT_STATUS_WAIT_MINT)
				.setCreateTime(DateUtils.currentMillis())
				.setUpdateTime(DateUtils.currentMillis())
				.setOrderSourceDesc(orderSource.getValue())
				.setCharacterId(artCollectionEntity.getId())
				.setCharacterName(artCollectionEntity.getCharacterName())
				.setCollectionCover(artCollectionEntity.getCollectionCover())
				.setUserCollectionCover(artCollectionEntity.getCollectionCover())//购买成功则更新为用户真正持有的藏品图片
				.setCollectionName(artCollectionEntity.getCollectionName())
				.setCollectionId(artCollectionEntity.getId())
				.setTotalFee(artCollectionEntity.getPrice())//由购买数量*单价
				.setAmount(order.getAmount())
				.setTotalFee(artCollectionEntity.getPrice())
				.setRebateFee(0)
				.setCollectionOpenType(artCollectionEntity.getCollectionOpenType())
				.setPayableFee(artCollectionEntity.getPrice())
				.setCollectionType(artCollectionEntity.getCollectionType())
				.setArtCreatorId(artCollectionEntity.getArtCreatorId());

		this.save(order);
		return order;
	}

	/**
	 * 创建订单详情
	 * 
	 * @param order
	 * @param
	 */
	private void addOrderDetailBySource2(OrderEntity order, List<OrderDetailEntity> details) {
		Set<Integer> ids = details.stream().map(OrderDetailEntity::getProductId).collect(Collectors.toSet());
		List<ArtCollectionEntity> arts = artCollectionService.listByIds(ids);
		if (CollectionUtils.isEmpty(arts) || arts.size() < details.size())
			throw new ApiException("艺术藏品不存在");
		Map<Integer, ArtCollectionEntity> artsMap = arts.stream()
				.collect(Collectors.toMap(ArtCollectionEntity::getId, d -> d, (k1, k2) -> k1));
		Integer amounts = 0;
		Integer totalFeel = 0;
		for (OrderDetailEntity item : details) {
			ArtCollectionEntity art = artsMap.get(item.getProductId());
			if (art == null)
				throw new ApiException(item.getProductId().toString() + "艺术藏品");
			item.setOrderId(order.getId()).setOrderNo(order.getOrderNo()).setUnitPrice(art.getPrice())
					.setCharacterId(art.getCharacterId()).setCharacterName(art.getCharacterName())
					.setCharacterAvatar(art.getCharacterAvatar()).setCollectionCover(art.getCollectionCover())
					.setCollectionName(art.getCollectionName()).setTotalAmount(art.getPrice() * item.getAmount());
			amounts = amounts + item.getAmount();
			totalFeel = totalFeel + item.getTotalAmount();

		}
		order.setPayableFee(totalFeel).setAmount(amounts).setTotalFee(totalFeel);
		this.updateById(order);
		orderDetailService.saveBatch(details);

	}


	public void updateOrderByMintSuccess(UserCollectionsEntity userCollectionsEntity){
		OrderEntity order = super.getById(userCollectionsEntity.getOrderId());
		if (order!=null){
			order.setNftId(userCollectionsEntity.getNftId());
			order.setNftIdAmount(userCollectionsEntity.getNftIdAmount());
			order.setUpdateTime(DateUtils.currentMillis());
			order.setMintStatus(NFTConstant.MINT_STATUS_SUSSCESS_MINT);

			super.saveOrUpdate(order);
		}
	}

	/**
	 * 回调业务处理
	 */
	@Override
	public void payCallBack(PayRecordEntity payRecord) {
		/*
		 * 订单状态更改 
		 * 藏品数量
		 * 创建用户和藏品的记录 s
		 */
		OrderEntity order = this.getById(payRecord.getOrderId());
		order.setIsPay(1).setStatus(2).setUpdateTime(payRecord.getUpdateTime());

	}

	/**
	 * 获取用户在redis藏品订单
	 */
	private OrderEntity getRedisOrderByUser(Integer collectionId,Integer userId){
		return redistUtil.getObject(getUserOrderRedisKey(collectionId,userId),OrderEntity.class);
	}
	/**
	 * 将用户的藏品订单存放在redis
	 */
	private void setUserOrderToRedis(OrderEntity order){
		redistUtil.setValue(getUserOrderRedisKey(order.getCollectionId(),order.getUserId()),order,ORDER_REDIS_TIME_OUT);
	}
	/**
	 * 用户某藏品 存储redis 唯一key
	 */
	private String getUserOrderRedisKey(Integer collectionId,Integer userId){
		return new StringBuilder().append(USER_COLLECTION_ORDER).append(collectionId).append("&&").append(userId).toString();
	}

   public CodeDataResp getPage(OrderPageReq pageReqData){
	   UserDto userDto = ShiroUtil.getPrincipalUser();
	   AccountEntity accountEntity = accountService.getById(userDto.getId());
	   pageReqData.setSortOrder("desc");
	   pageReqData.setSortType("create_time");
	   Page<OrderEntity> page = MpPageUtil.getCommonPage(pageReqData);
	   QueryWrapper<OrderEntity> condition = new QueryWrapper<>();
	   	if (StrUtils.isVaileNum(pageReqData.getStatus()))
			condition.eq("status",pageReqData.getStatus());
	    if (StrUtils.isVaileNum(pageReqData.getStatus()))
		   condition.eq("is_pay",pageReqData.getIsPay());
	    if (StrUtils.isVaileNum(pageReqData.getStatus()))
		   condition.eq("pay_way",pageReqData.getPayWay());

	   if (StrUtils.isVaileNum(pageReqData.getCharacterId()))
		   condition.eq("character_id",pageReqData.getCharacterId());
	   if (StrUtils.isVaileNum(pageReqData.getArtCreatorId()))
		   condition.eq("art_creator_id",pageReqData.getArtCreatorId());
	   if (StrUtils.isVaileNum(pageReqData.getCollectionId()))
		   condition.eq("collection_id",pageReqData.getCollectionId());



		return CodeDataResp.valueOfSuccess(super.page(page,condition));
   }

   public CodeDataResp getOrders(OrderPageReq pageReqData){
	   UserDto userDto = ShiroUtil.getPrincipalUser();
	   UserEntity userEntity = userService.getById(userDto.getId());
	   pageReqData.setSortOrder("desc");
	   pageReqData.setSortType("create_time");
	   Page<OrderEntity> page = MpPageUtil.getCommonPage(pageReqData);
	   QueryWrapper<OrderEntity> condition = new QueryWrapper<>();
	   condition.eq("user_id",userEntity.getId());
	   if (StrUtils.isVaileNum(pageReqData.getStatus()))
		   condition.eq("status",pageReqData.getStatus());
	   else {
		   condition.ge("status",1);
	   }


	   return CodeDataResp.valueOfSuccess(this.page(page,condition));
   }

}
