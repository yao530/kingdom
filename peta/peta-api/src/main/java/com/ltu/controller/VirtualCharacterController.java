package com.ltu.controller;

import java.util.List;

import com.ltu.model.request.character.VirtualCharacterPageReq;
import com.ltu.model.request.character.VirtualCharacterReq;
import com.ltu.service.VirtualCharacterService;
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

import com.ltu.domain.mp_entity.VirtualCharacterEntity;
import com.ltu.mapper.VirtualCharacterMapper;


/**
 * <p>
 * 虚拟人物 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/virtualCharacter")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "虚拟人物")
public class VirtualCharacterController extends BaseServiceImpl<VirtualCharacterMapper, VirtualCharacterEntity>{

    @Autowired
    VirtualCharacterService virtualCharacterService;

    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<VirtualCharacterEntity>> getMePage(@RequestBody VirtualCharacterPageReq req ) {
    	return virtualCharacterService.getPage(req);
    }
    
    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<VirtualCharacterEntity>> getList(@RequestBody VirtualCharacterPageReq req) {
        return CodeDataResp.valueOfSuccess(virtualCharacterService.getList(req));
    }

    @ApiOperation(value="获取藏品所有创作者")
    @RequestMapping(value="/getCreators", method= RequestMethod.POST)
    public CodeDataResp getCreators(@RequestBody BaseIdReq baseIdReq) {
        return CodeDataResp.valueOfSuccess(virtualCharacterService.getCreatorsByArt(baseIdReq));
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp saveOrUpdate(@RequestBody VirtualCharacterReq req) {
        return virtualCharacterService.saveOrUpdate(req);

    }

 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.POST)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }

    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.GET)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }
    

}

