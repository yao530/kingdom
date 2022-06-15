package com.ltu.service;

import com.ltu.domain.mp_entity.RoleEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.model.request.account.RoleCommonReq;
import com.ltu.model.request.base.BaseIdReq;

import com.ltu.model.response.base.CodeDataResp;

/**
 * <p>
 * 角色管理 服务类
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */

public interface RoleService extends IService<RoleEntity> {

    /**
     * 获取记录列表：代理商获取租户角色 超级管理员获取代理商、租户
     * @param
     * @return
     */
    CodeDataResp getList();

    /**
     * 获取记录列表:子管理员角色、员工角色
     * @param
     * @return
     */
    CodeDataResp getChildRole();

    CodeDataResp getSystemRoles();

    RoleEntity getRoleByType(Integer roleType);

    /**
     * 保存或更新
     * @param req
     * @return
     */
    CodeDataResp saveOrUpdate(RoleCommonReq req);

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
