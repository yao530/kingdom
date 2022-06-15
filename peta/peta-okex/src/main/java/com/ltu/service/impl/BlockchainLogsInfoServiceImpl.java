package com.ltu.service.impl;

import com.ltu.domain.mp_entity.BlockchainLogsInfoEntity;
import com.ltu.mapper.BlockchainLogsInfoMapper;
import com.ltu.service.BlockchainLogsInfoService;
import com.ltu.util.datetime.DateUtils;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

/**
 * <p>
 * 区块链请求信息 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-12
 */
@Service
public class BlockchainLogsInfoServiceImpl extends BaseServiceImpl<BlockchainLogsInfoMapper, BlockchainLogsInfoEntity> implements BlockchainLogsInfoService {


    public void createTransationInfo(String methodName,String contractAddress,TransactionReceipt transactionReceipt){
        BlockchainLogsInfoEntity blockchainLogsInfoEntity = new BlockchainLogsInfoEntity();

        blockchainLogsInfoEntity.setStatus(transactionReceipt.isStatusOK()?1:0);
        blockchainLogsInfoEntity.setTransactionHash(transactionReceipt.getTransactionHash());
        blockchainLogsInfoEntity.setBlockNumber(transactionReceipt.getBlockNumber().intValue());//
        blockchainLogsInfoEntity.setBlockHash(transactionReceipt.getBlockHash());
        blockchainLogsInfoEntity.setSmartContractAddress(transactionReceipt.getContractAddress());
        blockchainLogsInfoEntity.setGasUsed(transactionReceipt.getGasUsed().intValue());//
        blockchainLogsInfoEntity.setCumulativeGasUsed(transactionReceipt.getCumulativeGasUsed().intValue());//
        blockchainLogsInfoEntity.setTransactionIndex(transactionReceipt.getTransactionIndex().toString());//
        blockchainLogsInfoEntity.setCreateTime(DateUtils.currentSecs());
        blockchainLogsInfoEntity.setLogs(transactionReceipt.getLogs().toString());
        blockchainLogsInfoEntity.setFromAddress(transactionReceipt.getFrom());
        blockchainLogsInfoEntity.setToAddress(transactionReceipt.getTo());
        blockchainLogsInfoEntity.setMethodname(methodName);
        super.saveOrUpdate(blockchainLogsInfoEntity);
   }



}
