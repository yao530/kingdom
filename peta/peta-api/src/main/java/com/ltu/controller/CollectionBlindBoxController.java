package com.ltu.controller;

import java.util.List;

import com.ltu.model.request.artCollections.BlindBoxPageReq;
import com.ltu.model.request.artCollections.TranserCollectionReq;
import com.ltu.service.CollectionBlindBoxService;
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

import com.ltu.domain.mp_entity.CollectionBlindBoxEntity;
import com.ltu.mapper.CollectionBlindBoxMapper;


/**
 * <p>
 * 用户盲盒 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-06-02
 */
@RestController
@RequestMapping("/collectionBlind")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "用户盲盒")
public class CollectionBlindBoxController extends BaseServiceImpl<CollectionBlindBoxMapper, CollectionBlindBoxEntity>{

   @Autowired
    CollectionBlindBoxService collectionBlindBoxService;

    @ApiOperation(value="获取用户盲盒列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<CollectionBlindBoxEntity>> getMePage(@RequestBody BlindBoxPageReq pageReq ) {
    	return collectionBlindBoxService.getUserBoxs(pageReq);
    }

    @ApiOperation(value="打开盲盒")
    @RequestMapping(value="/openBox", method= RequestMethod.POST)
    public CodeDataResp openBox(@RequestBody BaseIdReq req) {
        return collectionBlindBoxService.openBlindBox(req);
    }
    @ApiOperation(value="转赠盲盒")
    @RequestMapping(value="/transformBox", method= RequestMethod.POST)
    public CodeDataResp transformBox(@RequestBody TranserCollectionReq req) {
        return collectionBlindBoxService.transferBox(req);
    }

 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.GET)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }



}

