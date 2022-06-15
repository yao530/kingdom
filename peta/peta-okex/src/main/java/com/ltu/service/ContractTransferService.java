package com.ltu.service;

import com.ltu.domain.mp_entity.ContractTransferEntity;
import com.ltu.model.request.smartContract.TransferReq;


/**
 * <p>
 * nft合约交易记录 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-10
 */

public interface ContractTransferService extends BaseTService<ContractTransferEntity> {

    void saveTransferRecord(TransferReq transferReq) throws Exception;

}
