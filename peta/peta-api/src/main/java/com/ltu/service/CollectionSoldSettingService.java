package com.ltu.service;

import com.ltu.domain.mp_entity.ArtCollectionEntity;
import com.ltu.domain.mp_entity.CollectionSoldSettingEntity;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.dto.CollectionSettingDetailDto;


/**
 * <p>
 * 藏品销售设置 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */

public interface CollectionSoldSettingService extends BaseTService<CollectionSoldSettingEntity> {

    CodeDataResp creatSetting(CollectionSettingDetailDto collectionSettingDetailDto);

    /**
     * @desc 更新nft 库存
     **/
    void updateCollectionStock(ArtCollectionEntity collectionEntity);

    CollectionSettingDetailDto getCollectionSettingDetail(BaseIdReq baseIdReq);

    CollectionSoldSettingEntity getCollectionSetting(Integer collectionId);
    /**
     * @desc 更新藏品的销售状态
     **/
    void updateCollectionSetting(Integer collectionId,String updateStr);
}
