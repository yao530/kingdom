package com.ltu.service;

import com.ltu.domain.mp_entity.*;
import com.ltu.model.request.artCollections.BlindBoxPageReq;
import com.ltu.model.request.artCollections.TranserCollectionReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;

import java.util.List;


/**
 * <p>
 * 用户盲盒 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-06-02
 */

public interface CollectionBlindBoxService extends BaseTService<CollectionBlindBoxEntity> {


    void createBox(OrderEntity order);

    CodeDataResp openBlindBox(BaseIdReq req);

    CodeDataResp transferBox(TranserCollectionReq transerCollectionReq);


    Boolean createBoxByAirDrop(List<Integer> userIds, List<UserJoinDropEntity> joinDropEntityList, ArtCollectionEntity artCollection, CollectionSoldSettingEntity collectionSoldSetting);

    CodeDataResp getUserBoxs(BlindBoxPageReq pageReq);


}
