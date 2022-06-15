package com.ltu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.domain.mp_entity.*;
import com.ltu.model.request.banner.BannerCommonReq;
import com.ltu.model.request.banner.BannerPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.request.smartContract.RegistBlockChainAddressReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.model.response.base.CodeDataResp;


/**
 * <p>
 *   区块链队列服务类
 * </p>
 *
 * @author 若尘
 * @since 2021-12-02
 */

public interface ContractSquService  {

    /**
     * @desc 创建用户区块链信息
     * @param userEntity
     **/
    void createUserBlockchainInfo(UserEntity userEntity);
    /**
     * @desc 创建用户区块链信息成功回调
     * @param req
     **/
    void buildUserBlockChainInfoSuccess(RegistBlockChainAddressReq req);
    /**
     * @desc 铸造nft
     * @param
     **/
    void mintArtCollection(ArtCollectionEntity artCollectionEntity, AdminMintReq adminMintReq);
    /**
     * @desc 铸造nft成功回调
     * @param
     **/
    void mintSuccess(MintReq mintReq);
    /**
     * @desc 交易nft
     * @param
     **/
    void transferNft(TransferCollectionRecordEntity transferCollectionRecordEntity,UserEntity userEntity);
    /**
     * @desc 交易nft 成功回调
     * @param
     **/
    void transferNftSuccess(TransferReq transferReq);


}
