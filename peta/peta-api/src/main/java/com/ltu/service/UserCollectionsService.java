package com.ltu.service;

import com.ltu.domain.mp_entity.*;
import com.ltu.model.request.artCollections.ArtCollectionPageReq;
import com.ltu.model.request.artCollections.UserCollectionPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.model.response.base.CodeDataResp;
import org.springframework.core.annotation.Order;

import java.util.List;


/**
 * <p>
 * 支付记录 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-26
 */

public interface UserCollectionsService extends BaseTService<UserCollectionsEntity> {


    CodeDataResp getUserMainAvatarCollections(UserCollectionPageReq pageReq);

    CodeDataResp getAlumnCollections(BaseIdReq baseIdReq);

    CodeDataResp setMainDisPlayByAvatar(BaseIdReq baseIdReq);

    UserCollectionsEntity createUserCollection(UserEntity userEntity,OrderEntity order);

    UserCollectionsEntity createUserCollectionByOpenBlindBox(UserEntity userEntity,CollectionBlindBoxEntity collectionBlindBox);

    Boolean createAirDropCollection(List<Integer> userIds,
                                 List<UserJoinDropEntity> joinDropEntityList,
                                 ArtCollectionEntity artCollection,
                                 CollectionSoldSettingEntity collectionSoldSetting);

    void updataTransferRecord(TransferCollectionRecordEntity transferCollectionRecordEntity);

}
