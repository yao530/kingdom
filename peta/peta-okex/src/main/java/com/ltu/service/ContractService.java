package com.ltu.service;

import com.ltu.domain.mp_entity.BlockchainLogsInfoEntity;
import com.ltu.domain.mp_entity.ContractMintEntity;
import com.ltu.domain.mp_entity.ContractTransferEntity;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.request.smartContract.TransferReq;
import org.web3j.protocol.core.methods.response.TransactionReceipt;


/**
 * <p>
 * 合约交互 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-12
 */
public interface ContractService  {

    void mint(ContractMintEntity mintReq) throws Exception;

    void transfer(ContractTransferEntity transferReq) throws Exception;


}
