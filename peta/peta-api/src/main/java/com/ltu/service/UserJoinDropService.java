package com.ltu.service;

import com.ltu.domain.mp_entity.ArtCollectionEntity;
import com.ltu.domain.mp_entity.CollectionSoldSettingEntity;
import com.ltu.domain.mp_entity.UserJoinDropEntity;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;


/**
 * <p>
 * 用户藏品抽签记录 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */

public interface UserJoinDropService extends BaseTService<UserJoinDropEntity> {

    CodeDataResp joinCollectionDrop(BaseIdReq idReq);

    UserJoinDropEntity getUserDropRecord(Integer collectionDropSettingId,Integer userId);

    UserJoinDropEntity getUserDropRecordByCollection(Integer collectionId,Integer userId);
    /**
     * @desc 统计并给用户发放空投藏品
     */

    void countAriDropAuthUsers(ArtCollectionEntity artCollection, CollectionSoldSettingEntity collectionSoldSetting);

    void countBookedDraw(ArtCollectionEntity artCollection,CollectionSoldSettingEntity collectionSoldSetting);
}
