package com.ltu.service;

import com.ltu.domain.mp_entity.FlowRecordEntity;
import com.ltu.model.request.base.BaseIdAndStatusReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.character.FlowRecordPageReq;
import com.ltu.model.response.base.CodeDataResp;


/**
 * <p>
 * 关注人物记录 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */

public interface FlowRecordService extends BaseTService<FlowRecordEntity> {

    CodeDataResp saveOrUpdate(BaseIdAndStatusReq baseIdReq);


    CodeDataResp findVirtualChapracterFuns(FlowRecordPageReq pageReq);

    CodeDataResp findUserFlows(FlowRecordPageReq pageReq);

 
}
