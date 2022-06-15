package com.ltu.controller;

import java.util.List;

import com.ltu.mapper.RoleMapper;

import com.ltu.model.request.account.RoleCommonReq;
import com.ltu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ltu.domain.mp_entity.RoleEntity;

import javax.validation.Valid;


/**
 * <p>
 * 角色管理 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/roleEntity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "角色管理")
public class RoleController extends BaseController<RoleMapper, RoleEntity>{

    private final  RoleService roleService;

    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<RoleEntity>> getList() {
        return roleService.getList();
    }

    @ApiOperation(value="获取子管理员列表")
    @RequestMapping(value="/getChildList", method= RequestMethod.POST)
    public CodeDataResp<List<RoleEntity>> getChildList() {
        return roleService.getChildRole();
    }

    @ApiOperation(value="获取系统角色")
    @RequestMapping(value="/getSystemRoles", method= RequestMethod.POST)
    public CodeDataResp<List<RoleEntity>> getSystemRoles() {
        return roleService.getSystemRoles();
    }

    @ApiOperation(value="获取角色路由信息")
    @RequestMapping(value="/getRoleroutes", method= RequestMethod.POST)
    public CodeDataResp getRoleroutes(@Valid @RequestBody BaseIdReq req) {
        return super.getDetail(req);
    }


    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdata", method= RequestMethod.POST)
    public CodeDataResp edit(@Valid @RequestBody RoleCommonReq req) {
        return roleService.saveOrUpdate(req);
    }


    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.POST)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return roleService.remove(baseIdReq);
    }
    


}

