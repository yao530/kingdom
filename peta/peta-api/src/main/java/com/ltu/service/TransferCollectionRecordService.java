package com.ltu.service;

import com.ltu.domain.mp_entity.TransferCollectionRecordEntity;
import com.ltu.domain.mp_entity.UserCollectionsEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.model.response.base.CodeDataResp;

import java.util.List;


/**
 * <p>
 * 支付记录 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */

public interface TransferCollectionRecordService extends BaseTService<TransferCollectionRecordEntity> {

    CodeDataResp makeTransfer(BaseIdReq baseIdReq);
    List<TransferCollectionRecordEntity> findCollectionTransferRecord(String collectiobCode);
    void mintUserCollection(UserEntity toUser, UserCollectionsEntity userCollectionsEntity, Integer transferType);
}
