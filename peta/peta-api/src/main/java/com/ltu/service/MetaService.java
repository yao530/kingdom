package com.ltu.service;

import com.ltu.domain.mp_entity.MetaEntity;
import com.ltu.model.response.base.CodeDataResp;


/**
 * <p>
 * 元宇宙 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-20
 */

public interface MetaService extends BaseTService<MetaEntity> {

    MetaEntity getMeta(Integer metaId);

}
