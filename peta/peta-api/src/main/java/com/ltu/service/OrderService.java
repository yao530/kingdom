package com.ltu.service;

import com.ltu.domain.mp_entity.OrderEntity;
import com.ltu.domain.mp_entity.PayRecordEntity;
import com.ltu.domain.mp_entity.UserCollectionsEntity;
import com.ltu.model.request.collectionOrder.CreateOrderReq;
import com.ltu.model.request.collectionOrder.OrderPageReq;
import com.ltu.model.request.collectionOrder.PayReq;
import com.ltu.model.response.base.CodeDataResp;


/**
 * <p>
 * 支付订单 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-04
 */

public interface OrderService extends BaseTService<OrderEntity> {


	/**
	 * 下单，拉起支付
	 * @param req
	 * @param ip
	 * @return
	 */
	  CodeDataResp   placeOrder(CreateOrderReq req);

	  CodeDataResp createOrderPayRecord(PayReq req);
	/**
	   * 支付回调
	   * @param payRecord
	   */
	 void payCallBack(PayRecordEntity payRecord);

	 void updateOrderByMintSuccess(UserCollectionsEntity userCollectionsEntity);

	 CodeDataResp getPage(OrderPageReq pageReqData);

	 CodeDataResp getOrders(OrderPageReq pageReqData);

}
