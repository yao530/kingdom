package com.ltu.service.impl;

import com.ltu.domain.mp_entity.MetaEntity;
import com.ltu.mapper.MetaMapper;
import com.ltu.service.MetaService;
import com.ltu.util.common.StrUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 元宇宙 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-20
 */
@Service
public class MetaServiceImpl extends BaseServiceImpl<MetaMapper, MetaEntity> implements MetaService {

    private static final Integer MAIN_META=1;

    public MetaEntity getMeta(Integer metaId){
        if (StrUtils.isVaileNum(metaId))
            return this.getById(metaId);
        else
            return this.getById(MAIN_META);
    }
}
