package com.ltu.service;

import com.ltu.domain.mp_entity.PlatformInfosEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.model.request.platinfos.PlatFormReq;
import com.ltu.model.request.platinfos.PlatFormTypeReq;
import com.ltu.model.response.base.CodeDataResp;

/**
 * <p>
 * 平台信息配置 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-28
 */
public interface PlatformInfosService extends IService<PlatformInfosEntity> {


    CodeDataResp saveOrUpdate(PlatFormReq platFormReq);


    CodeDataResp getInfoByType(PlatFormTypeReq formTypeReq);

}
