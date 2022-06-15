package com.ltu.listenre.rabbitmq;

import com.alibaba.fastjson.JSON;

import com.ltu.domain.mp_entity.ContractTransferEntity;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.request.smartContract.RegistBlockChainAddressReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.service.ContractMintService;
import com.ltu.service.ContractService;
import com.ltu.service.ContractTransferService;
import com.ltu.service.UserBlockchainInfoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ltu.constant.rabbitmq.RabbitConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMqHandler {


	@Autowired
	UserBlockchainInfoService userBlockchainInfoService;


	@Autowired
	ContractMintService contractMintService;
	@Autowired
	ContractTransferService contractTransferService;
	@Autowired
	ContractService contractService;

//	@RabbitListener(queues=RabbitConstants.QUEUE_LOG_PRINT)
//	public void queueLogPrintHandler(String message){
//		log.info("广发消息:[{}]",message);
//	}
//	
//	@RabbitListener(queues=RabbitConstants.TOPIC_ROUTING_KEY)
//	public void  queueTopicHandler(String message){
//		log.info("匹配的主题消息:[{}]",message);
//		//这里做订阅主题模式下的的业务逻辑
//	}

	/**
	 * @desc 用户钱包注册 处理
	 **/
	@RabbitListener(queues=RabbitConstants.DEAD_QUEUE)
	public void  queueDeadHandler(String message){
		log.info("注册用户的死信消息:[{}]",message);
		//这里做订阅主题模式下的的业务逻辑
		RegistBlockChainAddressReq req = JSON.parseObject(message,RegistBlockChainAddressReq.class);
		userBlockchainInfoService.saveOrUpdate(req);

	}

	/**
	 * @desc 铸造nft 先记录铸造任务 按批铸造
	 **/
	@RabbitListener(queues=RabbitConstants.MINT_DEAD_QUEUE)
	public void  queueDeadHandlerMint(String message){
		log.info("进入铸造任务死信队列的消息:[{}]",message);
		MintReq mintReq = JSON.parseObject(message,MintReq.class);
		try {
			contractMintService.createMintJob(mintReq);
		}catch (Exception e){
		}
	}


	/**
	 * @desc 交易nft 先记录交易记录 防止交易失败记录重查
	 **/
	@RabbitListener(queues=RabbitConstants.TRANSFER_DEAD_QUEUE)
	public void  queueDeadHandlerTransfer(String message){
		log.info("进入交易nft死信队列的消息:[{}]",message);
		TransferReq transferReq = JSON.parseObject(message,TransferReq.class);
		try {
			contractTransferService.saveTransferRecord(transferReq);
		}catch (Exception e){
		}
	}

	/**
	 * @desc 交易nft失败检查
	 **/
	@RabbitListener(queues=RabbitConstants.OKEX_TRANSFER_DEAD_QUEUE)
	public void  queueFailHandlerTransfer(String message){
		log.info("进入重复发起交易nft死信队列的消息:[{}]",message);
		ContractTransferEntity transferReq = JSON.parseObject(message,ContractTransferEntity.class);
		try {
			contractService.transfer(transferReq);
		}catch (Exception e){
		}
	}
}
