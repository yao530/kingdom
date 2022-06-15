package com.ltu.controller;

import java.util.List;
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

import com.ltu.domain.mp_entity.CollectionsAlbumEntity;
import com.ltu.mapper.CollectionsAlbumMapper;


/**
 * <p>
 * 用户藏品合辑 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-05-25
 */
@RestController
@RequestMapping("/collectionsAlbumEntity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "用户藏品合辑")
public class CollectionsAlbumController extends BaseServiceImpl<CollectionsAlbumMapper, CollectionsAlbumEntity>{


    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<CollectionsAlbumEntity>> getMePage(@RequestBody  BaseFilterPageReq<CollectionsAlbumEntity> req ) {
    	return super.getMePage(req);
    }

    
 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.GET)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }



}

