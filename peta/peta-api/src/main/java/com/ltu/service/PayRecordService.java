package com.ltu.service;


import com.ltu.domain.mp_entity.PayRecordEntity;
import com.ltu.model.request.base.PageReqData;
import com.ltu.model.response.base.CodeDataResp;

/**
 * <p>
 * 支付记录 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-02-21
 */

public interface PayRecordService extends BaseTService<PayRecordEntity> {

	void payCallBack(PayRecordEntity payRecord);

 	CodeDataResp getPayrecords(PageReqData pageReqData);
}
