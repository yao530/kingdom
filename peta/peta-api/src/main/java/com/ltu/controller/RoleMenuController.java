package com.ltu.controller;

import java.util.List;

import com.ltu.mapper.RoleMenuMapper;

import com.ltu.model.request.account.FindRoleMenuReq;
import com.ltu.model.request.account.RoleMenuReq;
import com.ltu.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.ltu.model.response.base.CodeDataResp;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ltu.domain.mp_entity.RoleMenuEntity;

import javax.validation.Valid;


/**
 * <p>
 * 角色菜单管理 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/roleMenuEntity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api( tags = "角色菜单管理")
public class RoleMenuController extends BaseController<RoleMenuMapper, RoleMenuEntity>{


    private final RoleMenuService roleMenuService;

    @ApiOperation(value="登录获取菜单列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<RoleMenuEntity>> getList() {
        return roleMenuService.getList();
    }

    @ApiOperation(value="获取角色的菜单权限列表")
    @RequestMapping(value="/getRoleList", method= RequestMethod.POST)
    public CodeDataResp<List<RoleMenuEntity>> getRoleList(@Valid @RequestBody FindRoleMenuReq roleMenuReq) {
        return roleMenuService.getList(roleMenuReq);
    }

    @ApiOperation(value="修改编辑权限")
    @RequestMapping(value="/editRoleMenu", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody RoleMenuReq req) {
        return roleMenuService.saveOrUpdate(req);
    }


    @ApiOperation(value="批量修改数据")
    @RequestMapping(value="/edits", method= RequestMethod.POST)
    public CodeDataResp edits(@RequestBody List<RoleMenuEntity> reqs) {
    	super.saveOrUpdateBatch(reqs);
    	return   CodeDataResp.valueOfSuccessEmptyData();
    }
    



}

