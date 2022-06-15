package com.ltu.service;

import com.ltu.domain.mp_entity.ArtCollectionEntity;
import com.ltu.domain.mp_entity.CollectionDetailEntity;
import com.ltu.model.request.artCollections.ArtCollectionReq;


/**
 * <p>
 * 故事篇章 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */

public interface CollectionDetailService extends BaseTService<CollectionDetailEntity> {

    void saveOrUpdate(ArtCollectionEntity artCollectionEntity,ArtCollectionReq artCollectionReq);
    CollectionDetailEntity getCollectionDetail(Integer collectionId);
}
