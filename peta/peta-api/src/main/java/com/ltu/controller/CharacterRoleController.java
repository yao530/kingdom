package com.ltu.controller;

import java.util.List;

import com.ltu.domain.mp_entity.StoryEntity;
import com.ltu.model.request.character.CharacterRoleListReq;
import com.ltu.model.request.character.CharacterRoleReq;
import com.ltu.model.request.story.StoryPageReq;
import com.ltu.service.CharacterRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.impl.BaseServiceImpl;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ltu.domain.mp_entity.CharacterRoleEntity;
import com.ltu.mapper.CharacterRoleMapper;


/**
 * <p>
 * 人物角色类型 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/characterRole")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "人物角色类型")
public class CharacterRoleController extends BaseServiceImpl<CharacterRoleMapper, CharacterRoleEntity>{

    @Autowired
    CharacterRoleService characterRoleService;

    @ApiOperation(value="获取数据")
    @RequestMapping(value="/getPage", method= RequestMethod.POST)
    public CodeDataResp<Page<StoryEntity>> getPage(@RequestBody StoryPageReq req ) {
        return characterRoleService.getPage(req);
    }
    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<CharacterRoleEntity>> getList() {
        return CodeDataResp.valueOfSuccess(characterRoleService.getList());
    }
    @ApiOperation(value="获取开放角色列表")
    @RequestMapping(value="/getOpenRoleList", method= RequestMethod.POST)
    public CodeDataResp<List<CharacterRoleEntity>> getOpenRoleList() {
        return CodeDataResp.valueOfSuccess(characterRoleService.getOpenRoleList());
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp saveOrUpdate(@RequestBody CharacterRoleReq req) {
        return characterRoleService.saveOrUpdate(req);
    }


}

