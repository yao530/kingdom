package com.ltu.service;

import com.ltu.domain.mp_entity.ContractMintEntity;
import com.ltu.domain.mp_entity.ContractTransferEntity;
import com.ltu.domain.mp_entity.MintToTransferEntity;
import com.ltu.model.response.base.MintItemDto;

import java.util.List;


/**
 * <p>
 * 平台交易nft记录 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-11
 */

public interface MintToTransferService extends BaseTService<MintToTransferEntity> {

    void createMintSusscessRecord(ContractMintEntity contractMintEntity, List<MintItemDto> mintItemDtos);

    MintToTransferEntity getNftId(ContractTransferEntity transferReq);
}
