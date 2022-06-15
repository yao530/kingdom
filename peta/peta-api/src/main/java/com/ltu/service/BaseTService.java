package com.ltu.service;


import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.request.base.PageReqData;

/**
 * <p>
 * 水箱属性状态 服务类
 * </p>
 *
 * @author ruochen@cxmx123
 * @since 2021-10-08
 */

public interface BaseTService<T> extends IService<T> {

    /**
     * 获取记录列表
     * @param req
     * @return
     */
    CodeDataResp<Page<T>> getMePage(BaseFilterPageReq<T>  req);
    
    CodeDataResp<List<T>> getMeList(BaseFilterPageReq<T> req); 
    /**
     * 保存或更新
     * @param req
     * @return
     */
    CodeDataResp edit(T req);

    /**
     * 获取记录详情
     * @param req
     * @return
     */
    CodeDataResp<T> getDetail(BaseIdReq req);

    /**
     * 删除记录
     * @param req
     * @return
     */
    CodeDataResp remove(BaseIdReq req);

}
