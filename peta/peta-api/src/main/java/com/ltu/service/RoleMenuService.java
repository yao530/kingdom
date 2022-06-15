package com.ltu.service;

import com.ltu.config.shiro.dto.UserDto;
import com.ltu.domain.mp_entity.AccountEntity;
import com.ltu.domain.mp_entity.MenuEntity;
import com.ltu.domain.mp_entity.RoleMenuEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.model.request.account.FindRoleMenuReq;
import com.ltu.model.request.account.RoleMenuReq;
import com.ltu.model.request.base.BaseIdReq;

import com.ltu.model.response.base.CodeDataResp;

import java.util.List;

/**
 * <p>
 * 角色菜单管理 服务类
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */

public interface RoleMenuService extends IService<RoleMenuEntity> {

    /**
     * 登录获取角色菜单记录列表
     * @param req
     * @return
     */
    CodeDataResp getList();

    /**
     * 根据角色ID获取菜单
     * @param
     * @return
     */
    CodeDataResp getList(FindRoleMenuReq roleMenuReq);



    /**
     * 保存或更新
     * @param req
     * @return
     */
    CodeDataResp saveOrUpdate(RoleMenuReq roleMenuReq);

    /**
     * 创建原始菜单
     * @param accountEntity
     * @return
     */
    CodeDataResp creatMenu(AccountEntity accountEntity);

    void createNewMenus(MenuEntity menuEntity, UserDto userDto);

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
