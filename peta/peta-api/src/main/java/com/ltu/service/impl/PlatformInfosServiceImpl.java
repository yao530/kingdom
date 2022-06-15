package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.domain.mp_entity.PlatformInfosEntity;
import com.ltu.domain.mp_entity.UserCollectionsEntity;
import com.ltu.mapper.PlatformInfosMapper;
import com.ltu.model.request.platinfos.PlatFormReq;
import com.ltu.model.request.platinfos.PlatFormTypeReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.PlatformInfosService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 平台信息配置 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-28
 */
@Service
public class PlatformInfosServiceImpl extends ServiceImpl<PlatformInfosMapper, PlatformInfosEntity> implements PlatformInfosService {



    public CodeDataResp saveOrUpdate(PlatFormReq platFormReq){

        if (StrUtils.isVaileNum(platFormReq.getId())){
            PlatformInfosEntity platformInfosEntity = super.getById(platFormReq.getId());
            if (platformInfosEntity==null)
                return CodeDataResp.valueOfFailed("数据非法");
            BeanMapper.copy(platFormReq,platformInfosEntity);
            platformInfosEntity.setUpdateTime(DateUtils.currentSecs());

            super.saveOrUpdate(platformInfosEntity);

        }
        else {
            PlatformInfosEntity platformInfosEntity = new PlatformInfosEntity();
            if (platformInfosEntity==null)
                return CodeDataResp.valueOfFailed("数据非法");
            BeanMapper.copy(platFormReq,platformInfosEntity);
            platformInfosEntity.setCreateTime(DateUtils.currentSecs());

            super.saveOrUpdate(platformInfosEntity);
        }
        return CodeDataResp.valueOfSuccessEmptyData();
    }


    public CodeDataResp getInfoByType(PlatFormTypeReq formTypeReq){

        QueryWrapper<PlatformInfosEntity> condition = new QueryWrapper<>();

        condition.eq("type",formTypeReq.getType());
        return CodeDataResp.valueOfSuccess(super.getOne(condition));
    }





}
