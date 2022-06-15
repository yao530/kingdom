package com.ltu.service;

import com.ltu.domain.mp_entity.UserBlockchainInfoEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.request.smartContract.RegistBlockChainAddressReq;


/**
 * <p>
 * 用户区块链信息 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-06
 */

public interface UserBlockchainInfoService extends BaseTService<UserBlockchainInfoEntity> {

    void saveOrUpdate(RegistBlockChainAddressReq req);


}
