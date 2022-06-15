package com.ltu.service;

import com.ltu.domain.mp_entity.ApplyCreatorEntity;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;
import com.ltu.model.request.character.ApplyCreatorReq;
import com.ltu.model.request.character.CheckCreatorReq;
import com.ltu.model.response.base.CodeDataResp;


/**
 * <p>
 * 创作者申请 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-16
 */

public interface ApplyCreatorService extends BaseTService<ApplyCreatorEntity> {


    CodeDataResp saveOrUpdate(ApplyCreatorReq applyCreatorReq);

    CodeDataResp checkApplyCreator(CheckCreatorReq req);

    CodeDataResp getPage(PageReqData pageReqData);

    CodeDataResp getUserApply(BaseIdReq req);

}
