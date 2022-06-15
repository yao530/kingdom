package com.ltu.service;

import com.ltu.domain.mp_entity.ContractMintEntity;
import com.ltu.model.request.smartContract.MintReq;

import java.util.List;


/**
 * <p>
 * 铸造任务 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-09
 */

public interface ContractMintService extends BaseTService<ContractMintEntity> {

    void createMintJob(MintReq mintReq) throws Exception;

    List<ContractMintEntity> getUnfinshMint();
}
