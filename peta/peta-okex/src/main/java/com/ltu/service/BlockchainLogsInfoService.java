package com.ltu.service;

import com.ltu.domain.mp_entity.BlockchainLogsInfoEntity;
import org.web3j.protocol.core.methods.response.TransactionReceipt;


/**
 * <p>
 * 区块链请求信息 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-12
 */

public interface BlockchainLogsInfoService extends BaseTService<BlockchainLogsInfoEntity> {

    void createTransationInfo(String methodName, String contractAddress, TransactionReceipt transactionReceipt);
 
}
