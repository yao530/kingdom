package com.ltu.service;

import com.ltu.domain.mp_entity.MenuEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.model.request.account.MenuReq;
import com.ltu.model.request.base.BaseIdReq;

import com.ltu.model.response.base.CodeDataResp;

import java.util.List;


/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */

public interface MenuService extends IService<MenuEntity> {

    /**
     * 获取记录列表
     * @param req
     * @return
     */
    List<MenuEntity> getList();

    CodeDataResp getMenuList();

    List<MenuEntity> getList(List<Integer> ids);

    /**
     * 保存或更新
     * @param req
     * @return
     */
    CodeDataResp saveOrUpdate(MenuReq req);

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
