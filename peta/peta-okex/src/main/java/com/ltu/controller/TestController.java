package com.ltu.controller;


import com.ltu.domain.mp_entity.ContractTransferEntity;
import com.ltu.domain.mp_entity.TransferCollectionRecordEntity;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.model.response.base.MintItemDto;
import com.ltu.service.*;
import com.ltu.util.HexUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.web3j.abi.TypeReference;
import com.ltu.model.response.base.CodeDataResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Api(tags="Z-测试模块")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    @Autowired
    BlockchainPubService blockchainPubService;

    @Autowired
    UserBlockchainInfoService userBlockchainInfoService;


    @Autowired
    ContractMintService contractMintService;

    @Autowired
    ContractTransferService contractTransferService;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ContractService contractService;

    @ApiOperation(value="G:测试")
    @RequestMapping(value="/index",method= RequestMethod.GET)
    public String index() {

        MintReq mintReq = new MintReq();
        mintReq.setSmartContractAddress("0xABFCD8115005f1B56207a0A351bFA7c97Fd865B9");
        mintReq.setMintAmount(100);
        mintReq.setThingId(7);
        mintReq.setMainMint(1);
        mintReq.setMintDesc("测试mint");

        try {
          contractMintService.createMintJob(mintReq);
        }catch (Exception e){

        }


       // blockchainPubService.getTotalSupply(mintReq.getSmartContractAddress());

        return  "成功";
    }

    @ApiOperation(value="G:交易测试")
    @RequestMapping(value="/testTransfer",method= RequestMethod.GET)
    public String testTransfer() {

        TransferReq transferReq = new TransferReq();
        transferReq.setTransferType(1);
        transferReq.setSmartContractAddress("0xABFCD8115005f1B56207a0A351bFA7c97Fd865B9");
        transferReq.setToAddress("0x9115ec17d1d7acc15b9be5fbeb219ee39f392e10");

        ContractTransferEntity contractTransfer = contractTransferService.getById(19);

        try {
//            contractTransferService.saveTransferRecord(transferReq);
            contractService.transfer(contractTransfer);
        }catch (Exception e){

        }

        return "交易成功";
    }
    @ApiOperation(value="G:返回无权限信息")
    @RequestMapping(value="/info403",method=RequestMethod.GET)
    public CodeDataResp info403() {
        return CodeDataResp.valueOfErrorUnAuthorized();
    }


}
