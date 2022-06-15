package com.ltu.service;

import com.ltu.domain.mp_entity.BannerEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.model.request.banner.BannerCommonReq;
import com.ltu.model.request.banner.BannerPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;


/**
 * <p>
 * 广告banner 服务类
 * </p>
 *
 * @author 若尘
 * @since 2021-12-02
 */

public interface BannerService extends IService<BannerEntity> {

    /**
     * 获取记录列表
     * @param req
     * @return
     */
    CodeDataResp getList(BannerPageReq req);

    /**
     * 保存或更新
     * @param req
     * @return
     */
    CodeDataResp saveOrUpdate(BannerCommonReq req);

    /**
     * 获取记录详情
     * @param req
     * @return
     */
    CodeDataResp getDetail(BaseIdReq req);

    /**
     * 删除记录
     * @param req
     * @return
     */
    CodeDataResp remove(BaseIdReq req); 
}
