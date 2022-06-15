package com.ltu.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.domain.mp_entity.PayRecordEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.model.request.base.PageReqData;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.UserService;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ltu.mapper.PayRecordMapper;
import com.ltu.service.PayRecordService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 支付记录 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-02-21
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayRecordServiceImpl extends BaseServiceImpl<PayRecordMapper, PayRecordEntity> implements PayRecordService {


	@Autowired
	UserService userService;

	@Override
	public void payCallBack(PayRecordEntity payRecord) {
		//更新PayRecord Order的状态， 设备的过期状态 
		//延长设备过期时间，如果设备的过期时间小于等于当前时间则，以当前时间为准， 如果设备没有过期则在该时间点上延期
		Date updateDate= new Date();
		try {
			payRecord.setStatus(1)
			.setStatusDesc("支付成功")
			.setUpdateTime(DateUtils.currentSecs());
			//orderService.payCallBack(payRecord);
			payRecord.setHandleStatus(1);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			this.updateById(payRecord);
		}
		
		
	}


	public CodeDataResp getPayrecords(PageReqData pageReqData){

		UserDto userDto = ShiroUtil.getPrincipalUser();
		UserEntity userEntity = userService.getById(userDto.getId());
		pageReqData.setSortOrder("desc");
		pageReqData.setSortType("sort");
		Page<PayRecordEntity> page = MpPageUtil.getCommonPage(pageReqData);
		QueryWrapper<PayRecordEntity> condition = new QueryWrapper<>();
		//condition.eq("status",1);
		condition.eq("user_id",userEntity.getId());


		return CodeDataResp.valueOfSuccessEmptyData();
	}

   
}
