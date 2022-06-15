package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.domain.mp_entity.CreatorApplySettingEntity;
import com.ltu.mapper.CreatorApplySettingMapper;

import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.CreatorApplySettingService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 创作者招募活动设置 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-18
 */
@Service
public class CreatorApplySettingServiceImpl extends BaseServiceImpl<CreatorApplySettingMapper, CreatorApplySettingEntity> implements CreatorApplySettingService {


    public CodeDataResp saveOrUpDate(CreatorApplySettingEntity req){
        if (StrUtils.isVaileNum(req.getId())){
            CreatorApplySettingEntity creatorApplySettingEntity = this.getById(req.getId());
            BeanMapper.copy(req,creatorApplySettingEntity);
            creatorApplySettingEntity.setUpdateTime(DateUtils.currentSecs());
            this.saveOrUpDate(creatorApplySettingEntity);
        }
        else {
            CreatorApplySettingEntity creatorApplySettingEntity = new CreatorApplySettingEntity();
            BeanMapper.copy(req,creatorApplySettingEntity);
            creatorApplySettingEntity.setCreateTime(DateUtils.currentSecs());
            this.saveOrUpDate(creatorApplySettingEntity);
        }
        return CodeDataResp.valueOfSuccessEmptyData();
    }


    public CreatorApplySettingEntity getNewSetting(){

        QueryWrapper<CreatorApplySettingEntity> condition = new QueryWrapper<>();
        condition.eq("status",1);
        return this.getOne(condition);
    }



}
