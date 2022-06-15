package com.ltu.controller;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ltu.domain.mp_entity.PayRecordEntity;
import com.ltu.model.request.base.PageReqData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.domain.mp_entity.PayCallBackEntity;
import com.ltu.mapper.PayRecordMapper;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.payment.config.WxAppPayConfig;
import com.ltu.payment.util.XmlUtils;
import com.ltu.service.PayCallBackService;
import com.ltu.service.PayRecordService;
import com.ltu.service.impl.BaseServiceImpl;
import com.ltu.util.ext.wx.wxCore.WeChatUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 * 支付记录 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-02-21
 */
@Slf4j
@RestController
@RequestMapping("/payRecord")
@Api(tags= "支付记录")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PayRecordController extends BaseServiceImpl<PayRecordMapper, PayRecordEntity>{
	private final  PayCallBackService payCallBackService;
	private final  PayRecordService payRecordService;


    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<PayRecordEntity>> getMePage(@RequestBody  BaseFilterPageReq<PayRecordEntity> req ) {
    	return super.getMePage(req);
    }
    
    @ApiOperation(value="用户获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<PayRecordEntity>> getList(@RequestBody PageReqData req ) {
        return payRecordService.getPayrecords(req);
    }


 	/**
 	 * @date 2019年10月18日
 	 * @author 若尘
 	 * @Description 微信官方支付结果通知地址
 	 */
 	@ApiOperation(value = "微信官方支付结果通知地址")
 	@RequestMapping(value = "/wxNotify", method = { RequestMethod.POST,RequestMethod.GET })
 	public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		
 		StringBuilder sb = new StringBuilder();
 		BufferedReader in = request.getReader();// 获取正文内容
 		String line;
 		while ((line = in.readLine()) != null) {
 			sb.append(line);
 		}
 		String xml = sb.toString();
 		log.info("================收到微信回调，回调信息：\n {}",xml);
 		Map<String, String> result = XmlUtils.toMap(xml.getBytes(), "utf-8");
 		// 签名验证
 		String verifySign = result.get("sign");

 		result.remove("sign");
 		boolean verifyFlag = WeChatUtils.verifySign2(result, WxAppPayConfig.WxPayKey, verifySign);// 钱名验证
 		if (verifyFlag == false) {
 			log.error("50000", "签名验证失败");
 			// 先响应微信收到通知
 			WeChatUtils.responsePayMessage(response, "签名失败");
 			return;
 		}

 		boolean excludeRepeatFlag = WeChatUtils.wxPayMsgExcludeRepeat(result.get("out_trade_no"),
 				result.get("transaction_id"), result.get("time_end"));// 消息排重
 		if (excludeRepeatFlag) {
 			log.error("50000", "微信支付回调消息重复");
 			// 先响应微信收到通知
 			WeChatUtils.responsePayMessage(response, "消息重复");
 			return;
 		}

 		// 先响应微信收到通知
 		WeChatUtils.responsePayMessage(response, "OK");
 		// 执行回调业务
 		QueryWrapper<PayRecordEntity> wrapper=new QueryWrapper<PayRecordEntity>();
 		wrapper.lambda().eq(PayRecordEntity :: getOrderNo, result.get("out_trade_no")).last("limit 1");
		PayRecordEntity payRecord = payRecordService.getOne(wrapper);// 获取订单
 		PayCallBackEntity  wxCallBack = payCallBackService.createWXCallBack(result, payRecord);
 		payRecordService.payCallBack(payRecord);

 	}
 	
 	
 	/**
 	 * @date 2019年10月18日
 	 * @author 若尘
 	 * @Description 微信官方支付结果通知地址
 	 */
 	@ApiOperation(value = "微信官方支付结果通知地址")
 	@RequestMapping(value = "/wxH5Notify", method = { RequestMethod.POST,RequestMethod.GET })
 	public void wxH5Notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		
 		StringBuilder sb = new StringBuilder();
 		BufferedReader in = request.getReader();// 获取正文内容
 		String line;
 		while ((line = in.readLine()) != null) {
 			sb.append(line);
 		}
 		String xml = sb.toString();
 		log.info("================收到微信回调，回调信息：\n {}",xml);
 		JSONObject result = JSONObject.parseObject(xml);
 		// 签名验证
 		String verifySign = result.getString("sign");

 		result.remove("sign");
 		boolean verifyFlag = WeChatUtils.verifySign2(result, WxAppPayConfig.WxPayKeyV3, verifySign);// 钱名验证
 		if (verifyFlag == false) {
 			log.error("50000", "签名验证失败");
 			// 先响应微信收到通知
 			WeChatUtils.responsePayMessage(response, "签名失败");
 			return;
 		}

 		boolean excludeRepeatFlag = WeChatUtils.wxPayMsgExcludeRepeat(result.getString("out_trade_no"),
 				result.getString("transaction_id"), result.getString("time_end"));// 消息排重
 		if (excludeRepeatFlag) {
 			log.error("50000", "微信支付回调消息重复");
 			// 先响应微信收到通知
 			WeChatUtils.responsePayMessage(response, "消息重复");
 			return;
 		}

 		// 先响应微信收到通知
 		WeChatUtils.responsePayMessage(response, "OK");
 		// 执行回调业务
 		QueryWrapper<PayRecordEntity> wrapper=new QueryWrapper<PayRecordEntity>();
 		wrapper.lambda().eq(PayRecordEntity :: getOrderNo, result.get("out_trade_no")).last("limit 1");
		PayRecordEntity payRecord = payRecordService.getOne(wrapper);// 获取订单
 		PayCallBackEntity  wxCallBack = payCallBackService.createWXCallBack(result, payRecord);
 		payRecordService.payCallBack(payRecord);

 	}

}

