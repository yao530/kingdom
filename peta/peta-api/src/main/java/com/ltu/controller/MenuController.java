package com.ltu.controller;

import java.util.List;

import com.ltu.mapper.MenuMapper;
import com.ltu.model.request.account.MenuReq;
import com.ltu.model.response.MenuResp;
import com.ltu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ltu.domain.mp_entity.MenuEntity;



/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/menuEntity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api( tags = "菜单管理")
public class MenuController extends BaseController<MenuMapper, MenuEntity>{


    private final MenuService menuService;

    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<MenuResp>> getList() {
        return menuService.getMenuList();
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/edit", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody MenuReq req) {
        return menuService.saveOrUpdate(req);
    }



    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.POST)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return menuService.remove(baseIdReq);
    }



}

