package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.domain.mp_entity.ArtCollectionEntity;
import com.ltu.domain.mp_entity.BannerEntity;
import com.ltu.domain.mp_entity.CollectionDetailEntity;
import com.ltu.mapper.CollectionDetailMapper;
import com.ltu.model.request.artCollections.ArtCollectionReq;
import com.ltu.service.CollectionDetailService;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.datetime.DateUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 故事篇章 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@Service
public class CollectionDetailServiceImpl extends BaseServiceImpl<CollectionDetailMapper, CollectionDetailEntity> implements CollectionDetailService {

   public void saveOrUpdate(ArtCollectionEntity artCollectionEntity,ArtCollectionReq artCollectionReq){

       CollectionDetailEntity collectionDetailEntity = getCollectionDetail(artCollectionEntity.getId());
       if (collectionDetailEntity==null){
           collectionDetailEntity = new CollectionDetailEntity();
           collectionDetailEntity.setCreateTime(DateUtils.currentSecs());
       }
       collectionDetailEntity.setCollectionId(artCollectionEntity.getId());
       collectionDetailEntity.setContext(artCollectionReq.getContext());
       collectionDetailEntity.setCollectionRights(artCollectionReq.getCollectionRights());
       collectionDetailEntity.setUpdateTime(DateUtils.currentSecs());
       collectionDetailEntity.setCollectionStyles(artCollectionReq.getCollectionStyles());
       collectionDetailEntity.setSoldTips(artCollectionReq.getSoldTips());
        super.saveOrUpdate(collectionDetailEntity);

   }

   public CollectionDetailEntity getCollectionDetail(Integer collectionId){

       QueryWrapper<CollectionDetailEntity> condition = new QueryWrapper<>();
       condition.eq("collection_id",collectionId);


       return super.getOne(condition);
   }


}
