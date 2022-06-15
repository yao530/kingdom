package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.NFTConstant;
import com.ltu.constant.RedisPubKeyConstant;
import com.ltu.domain.mp_entity.*;
import com.ltu.mapper.CollectionBlindBoxMapper;
import com.ltu.model.request.artCollections.BlindBoxPageReq;
import com.ltu.model.request.artCollections.TranserCollectionReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.*;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户盲盒 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-02
 */
@Service
public class CollectionBlindBoxServiceImpl extends BaseServiceImpl<CollectionBlindBoxMapper, CollectionBlindBoxEntity> implements CollectionBlindBoxService {

    @Autowired
    UserJoinDropService userJoinDropService;
    @Autowired
    ArtCollectionService artCollectionService;
    @Autowired
    CollectionSoldSettingService collectionSoldSettingService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    UserCollectionsService userCollectionsService;


    public CodeDataResp getUserBoxs(BlindBoxPageReq pageReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        UserEntity userEntity = userService.getById(userDto.getId());
        if (userEntity==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        Page<CollectionBlindBoxEntity> page = MpPageUtil.getCommonPage(pageReq);
        QueryWrapper<CollectionBlindBoxEntity> condition = new QueryWrapper<>();
        condition.eq("status",pageReq.getStatus());
        condition.eq("user_id",userEntity.getId());

        return CodeDataResp.valueOfSuccess(super.page(page,condition));
    }


   public void createBox(OrderEntity order){
       CollectionBlindBoxEntity blindBox = new CollectionBlindBoxEntity();
       blindBox.setCreateTime(DateUtils.currentMillis());
       blindBox.setCollectionId(order.getCollectionId());
       blindBox.setCollectionCover(order.getCollectionCover());
       blindBox.setOrderId(order.getId());
       blindBox.setUserId(order.getUserId());
       blindBox.setStatus(0);
       blindBox.setCollectionName(order.getCollectionName());
       super.save(blindBox);
   }


   public Boolean createBoxByAirDrop(List<Integer> userIds, List<UserJoinDropEntity> joinDropEntityList,
                                   ArtCollectionEntity artCollection, CollectionSoldSettingEntity collectionSoldSetting) {
       Integer nftAmount = collectionSoldSetting.getPublishAmount();
       Integer soldAmout = 0;
       List<UserJoinDropEntity> dropEntityList = new ArrayList<>();
       List<CollectionBlindBoxEntity> blindBoxEntityList = new ArrayList<>();
       if (joinDropEntityList.size() > 0) {
           for (Integer userId : userIds) {
               List<UserJoinDropEntity> userJoinDropEntityList = joinDropEntityList.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toList());
               if (userJoinDropEntityList.size() > 0) {
                   UserJoinDropEntity userJoinDropEntity = userJoinDropEntityList.get(0);
                   userJoinDropEntity.setDropStatus(1);
                   userJoinDropEntity.setStatus(1);
                   CollectionBlindBoxEntity blindBox = new CollectionBlindBoxEntity();
                   blindBox.setCreateTime(DateUtils.currentMillis());
                   blindBox.setCollectionId(artCollection.getId());
                   blindBox.setCollectionCover(artCollection.getCollectionCover());
                   blindBox.setUserId(userJoinDropEntity.getUserId());
                   blindBox.setStatus(0);
                   blindBox.setCollectionName(artCollection.getCollectionName());

                   blindBoxEntityList.add(blindBox);
                   dropEntityList.add(userJoinDropEntity);
                   nftAmount--;
                   soldAmout++;
               }

               redisTemplate.opsForList().remove(RedisPubKeyConstant.getJoinDropRankingKey(collectionSoldSetting.getId()),0,userId);

           }



       }

       if (blindBoxEntityList.size()>0)
           this.saveOrUpdateBatch(blindBoxEntityList);
       if (dropEntityList.size()>0)
           userJoinDropService.saveOrUpdateBatch(dropEntityList);
       artCollection.setSoldNumber(soldAmout);
       artCollection.setPublishAmount(nftAmount);
       artCollectionService.saveOrUpdate(artCollection);
       collectionSoldSettingService.updateCollectionStock(artCollection);

       return true;

   }

   public CodeDataResp openBlindBox(BaseIdReq req){
       UserDto userDto = ShiroUtil.getPrincipalUser();
       if (userDto==null)
           return CodeDataResp.valueOfFailed("用户数据非法");
       UserEntity userEntity = userService.getById(userDto.getId());
       if (userEntity==null)
           return CodeDataResp.valueOfFailed("用户数据非法");
       CollectionBlindBoxEntity blindBox = super.getById(req.getId());
       if (blindBox==null)
           return CodeDataResp.valueOfFailed("盲盒不存在");
       if (blindBox!=null&&!blindBox.getUserId().equals(userEntity.getId()))
           return CodeDataResp.valueOfFailed("盲盒非法");
       if (blindBox!=null&&blindBox.getStatus()!=0)
           return CodeDataResp.valueOfFailed("盲盒已开");
       UserCollectionsEntity userCollectionsEntity = userCollectionsService.createUserCollectionByOpenBlindBox(userEntity,blindBox);
       blindBox.setUserCollectionId(userCollectionsEntity.getId());
       blindBox.setUpdateTime(DateUtils.currentMillis());
       blindBox.setStatus(1);
       blindBox.setUpdateTime(DateUtils.currentMillis());
       blindBox.setOpenTime(DateUtils.currentMillis());
       super.saveOrUpdate(blindBox);

       return CodeDataResp.valueOfSuccess(userCollectionsEntity);
   }

    public CodeDataResp transferBox(TranserCollectionReq transerCollectionReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        UserEntity userEntity = userService.getById(userDto.getId());
        if (userEntity==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        UserEntity tranferedUser = userService.getUserByPhone(transerCollectionReq.getUserPhone());
        if (tranferedUser==null)
            return CodeDataResp.valueOfFailed("转赠用户不存在");
        if (tranferedUser.getIdentityStatus()==0)
            return CodeDataResp.valueOfFailed("转赠用户未实名");
        CollectionBlindBoxEntity blindBox = super.getById(transerCollectionReq.getTransferThingId());
        if (blindBox==null)
            return CodeDataResp.valueOfFailed("盲盒不存在");
        if (blindBox!=null&&!blindBox.getUserId().equals(userEntity.getId()))
            return CodeDataResp.valueOfFailed("盲盒非法");
        if (blindBox!=null&&blindBox.getStatus()!=0)
            return CodeDataResp.valueOfFailed("盲盒已开");

        CollectionBlindBoxEntity transferBox = new CollectionBlindBoxEntity();
        transferBox.setCollectionId(blindBox.getCollectionId());
        transferBox.setUserId(tranferedUser.getId());
        transferBox.setStatus(0);
        transferBox.setCreateTime(DateUtils.currentMillis());
        transferBox.setCollectionName(blindBox.getCollectionName());
        transferBox.setCollectionCover(blindBox.getCollectionCover());
        transferBox.setTransfedId(blindBox.getTransfedId());
        transferBox.setMetaDataInfo(blindBox.getMetaDataInfo());

        super.save(transferBox);

        blindBox.setTransferTime(DateUtils.currentMillis());
        blindBox.setStatus(2);
        blindBox.setUpdateTime(DateUtils.currentMillis());

        super.saveOrUpdate(blindBox);

        return CodeDataResp.valueOfSuccess(blindBox);
    }

}
