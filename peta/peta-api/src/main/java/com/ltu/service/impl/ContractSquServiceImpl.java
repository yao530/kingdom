package com.ltu.service.impl;

import com.alibaba.fastjson.JSON;

import com.ltu.constant.NFTConstant;
import com.ltu.constant.rabbitmq.RabbitConstants;
import com.ltu.domain.mp_entity.*;
import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.request.smartContract.RegistBlockChainAddressReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.datetime.DateUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  区块链队列服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2021-12-02
 */
@Service
public class ContractSquServiceImpl  implements ContractSquService {



    @Resource
    private RabbitTemplate rabbitTemplate;

    @Autowired
    UserService userService;
    @Autowired
    ArtCollectionService artCollectionService;
    @Autowired
    UserCollectionsService userCollectionsService;
    @Autowired
    TransferCollectionRecordService transferCollectionRecordService;

    /**
     * @desc 创建用户区块链信息
     * @param userEntity
     **/
    public void createUserBlockchainInfo(UserEntity userEntity){

        RegistBlockChainAddressReq req = new RegistBlockChainAddressReq();
        req.setUserId(userEntity.getId());
        req.setPhone(userEntity.getMobilePhone());
        req.setStatus(0);
        req.setResultDesc("创建用户钱包信息");
        rabbitTemplate.convertAndSend(RabbitConstants.FANOUT_DEAD_QUEUE, RabbitConstants.DEAD_QUEUE , JSON.toJSONString(req));

    }

    /**
     * @desc 创建用户区块链信息成功回调 记录用户钱包地址
     * @param req
     **/
    public  void buildUserBlockChainInfoSuccess(RegistBlockChainAddressReq req){
        if (req!=null&&req.getStatus()==1){
            UserEntity userEntity = userService.getById(req.getUserId());
            if (userEntity!=null){
                userEntity.setUserBlockchainAddress(req.getSmartContractAddress());
                userEntity.setUpdateTime(DateUtils.currentMillis());
                userService.saveOrUpdate(userEntity);
            }
        }
    }


    /**
     * @desc 铸造nft
     * @param
     **/
    public void mintArtCollection(ArtCollectionEntity artCollectionEntity,AdminMintReq adminMintReq){
        MintReq mintReq = new MintReq();
        mintReq.setSmartContractAddress(artCollectionEntity.getSmartContractAddress());
        mintReq.setMintAmount(adminMintReq.getMintAmount());
        mintReq.setThingId(artCollectionEntity.getId());
        mintReq.setContractType(artCollectionEntity.getCollectionType()==NFTConstant.NFT_TYPE_STORY_COPYRIGHT?2:1);
        mintReq.setBaseUri(adminMintReq.getPrefixFileUrl());
        mintReq.setMintDesc("铸造nft");

        rabbitTemplate.convertAndSend(RabbitConstants.FANOUT_DEAD_QUEUE4, RabbitConstants.MINT_DEAD_QUEUE , JSON.toJSONString(mintReq));

    }

    /**
     * @desc 铸造nft成功回调
     * @param
     **/
    public void mintSuccess(MintReq mintReq){
        ArtCollectionEntity artCollectionEntity = artCollectionService.getById(mintReq.getThingId());
        if (artCollectionEntity!=null){
            artCollectionEntity.setMintStatus(NFTConstant.MINT_STATUS_SUSSCESS_MINT);
            artCollectionEntity.setUpdateTime(DateUtils.currentMillis());
            artCollectionService.saveOrUpdate(artCollectionEntity);
        }
    }

    /**
     * @desc 交易nft
     * @param
     **/
    public void transferNft(TransferCollectionRecordEntity transferCollectionRecordEntity,UserEntity userEntity){
        TransferReq transferReq = new TransferReq();
        BeanMapper.copy(transferCollectionRecordEntity,transferReq);
        transferReq.setTransferCollectionId(transferCollectionRecordEntity.getId());
        if (userEntity!=null)
            transferReq.setFromUserPhone(userEntity.getMobilePhone());
        transferReq.setResultDesc("发起nft交易");
        rabbitTemplate.convertAndSend(RabbitConstants.FANOUT_DEAD_QUEUE3, RabbitConstants.TRANSFER_DEAD_QUEUE , JSON.toJSONString(transferReq));
    }

    /**
     * @desc 交易nft 成功回调
     * @param
     **/
    public  void transferNftSuccess(TransferReq transferReq){
        TransferCollectionRecordEntity transferCollectionRecordEntity= transferCollectionRecordService.getById(transferReq.getTransferCollectionId());
        if (transferCollectionRecordEntity!=null) {
            transferCollectionRecordEntity.setNftId(transferReq.getNftId());
            transferCollectionRecordEntity.setNftIdAmount(transferReq.getNftIdAmount());
            transferCollectionRecordEntity.setTxHash(transferReq.getTxHash());
            transferCollectionRecordEntity.setFromAddress(transferReq.getFromAddress());
            transferCollectionRecordEntity.setContractTransferId(transferReq.getContractTransferId());
            transferCollectionRecordEntity.setTransferedTime(DateUtils.currentMillis());
            transferCollectionRecordEntity.setMintStatus(transferReq.getMintStatus());
            transferCollectionRecordService.saveOrUpdate(transferCollectionRecordEntity);
            if (transferCollectionRecordEntity.getMintStatus().intValue()==NFTConstant.MINT_STATUS_SUSSCESS_MINT)
                userCollectionsService.updataTransferRecord(transferCollectionRecordEntity);
        }

    }


}
