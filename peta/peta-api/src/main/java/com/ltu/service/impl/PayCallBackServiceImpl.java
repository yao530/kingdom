package com.ltu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ltu.domain.mp_entity.PayCallBackEntity;
import com.ltu.domain.mp_entity.PayRecordEntity;
import com.ltu.mapper.PayCallBackMapper;
import com.ltu.payment.enums.PayWayDict;
import com.ltu.service.PayCallBackService;

import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 微信支付宝回调记录 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-02-21
 */
@Service
public class PayCallBackServiceImpl extends BaseServiceImpl<PayCallBackMapper, PayCallBackEntity>
		implements PayCallBackService {

	@Override
	public PayCallBackEntity createWXCallBack(Map<String, String> map, PayRecordEntity pay) {

		PayCallBackEntity wxCallBack = new PayCallBackEntity();
		wxCallBack.setCallBackType(PayWayDict.WxPayway.getIndex());
		wxCallBack.setUserId(pay.getUserId());
		wxCallBack.setPayRecordId(pay.getId());
		wxCallBack.setTransactionId(map.get("transaction_id"));
		wxCallBack.setMchId(map.get("mch_id"));
		wxCallBack.setOrderNo(map.get("out_trade_no"));
		wxCallBack.setTimeEnd(map.get("time_end"));
		wxCallBack.setAppid(map.get("appid"));
		wxCallBack.setResultCode(map.get("result_code"));
		wxCallBack.setOpenid(map.get("openid"));
		wxCallBack.setTotalFee(Integer.parseInt(map.get("total_fee")));
		
		this.save(wxCallBack);
		return wxCallBack;
	}

	@Override
	public PayCallBackEntity createWXCallBack(JSONObject map, PayRecordEntity pay) {
		PayCallBackEntity wxCallBack = new PayCallBackEntity();
		wxCallBack.setCallBackType(PayWayDict.WxPayway.getIndex());
		wxCallBack.setUserId(pay.getUserId());
		wxCallBack.setPayRecordId(pay.getId());
		wxCallBack.setTransactionId(map.getString("transaction_id"));
		wxCallBack.setMchId(map.getString("mch_id"));
		wxCallBack.setOrderNo(map.getString("out_trade_no"));
		wxCallBack.setTimeEnd(map.getString("time_end"));
		wxCallBack.setAppid(map.getString("appid"));
		wxCallBack.setResultCode(map.getString("result_code"));
		wxCallBack.setOpenid(map.getString("openid"));
		wxCallBack.setTotalFee(Integer.parseInt(map.getString("total_fee")));
		
		this.save(wxCallBack);
		return wxCallBack;
	}
}
