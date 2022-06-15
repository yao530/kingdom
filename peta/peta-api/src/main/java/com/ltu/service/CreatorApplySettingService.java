package com.ltu.service;

import com.ltu.domain.mp_entity.CreatorApplySettingEntity;
import com.ltu.model.response.base.CodeDataResp;


/**
 * <p>
 * 创作者招募活动设置 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-18
 */

public interface CreatorApplySettingService extends BaseTService<CreatorApplySettingEntity> {


    CodeDataResp saveOrUpDate(CreatorApplySettingEntity req);

    CreatorApplySettingEntity getNewSetting();
}
