package com.ltu.controller;

import java.util.List;

import com.ltu.aop.OperationLogAnnotation;
import com.ltu.model.request.artCollections.ArtCollectionPageReq;
import com.ltu.model.request.artCollections.ArtCollectionReq;
import com.ltu.model.request.base.PageReqData;
import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.service.ArtCollectionService;
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

import com.ltu.domain.mp_entity.ArtCollectionEntity;
import com.ltu.mapper.ArtCollectionMapper;


/**
 * <p>
 * 艺术藏品 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/artCollection")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "艺术藏品")
public class ArtCollectionController extends BaseServiceImpl<ArtCollectionMapper, ArtCollectionEntity>{


    @Autowired
    ArtCollectionService artCollectionService;

    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<ArtCollectionEntity>> getMePage(@RequestBody ArtCollectionPageReq pageReqData ) {
    	return artCollectionService.getPage(pageReqData);
    }

    @ApiOperation(value="艺术家获取列表")
    @RequestMapping(value="/getPage", method= RequestMethod.POST)
    public CodeDataResp<Page<ArtCollectionEntity>> getPage(@RequestBody PageReqData req ) {
        return artCollectionService.creatorGetPage(req);
    }


    @ApiOperation(value="首页获取藏品列表")
    @RequestMapping(value="/getPageList", method= RequestMethod.POST)
    public CodeDataResp<Page<ArtCollectionEntity>> getPageList(@RequestBody ArtCollectionPageReq req ) {
        return artCollectionService.getArtCollections(req);
    }



    @ApiOperation(value="新增/保存")
    @OperationLogAnnotation(operModul = "艺术藏品-新增修改",operDesc = "修改藏品信息")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp saveOrUpdate(@RequestBody ArtCollectionReq req) {
        return artCollectionService.saveOrUpdate(req);
    }

    @ApiOperation(value="铸造")
    @RequestMapping(value="/mint", method= RequestMethod.POST)
    public CodeDataResp mint(@RequestBody AdminMintReq req) {
        return artCollectionService.mint(req);
    }

    @ApiOperation(value="获取故事下的相关藏品")
    @RequestMapping(value="/getStoryArtCollections", method= RequestMethod.POST)
    public CodeDataResp getStoryArtCollections(@RequestBody BaseIdReq baseIdReq) {
        return CodeDataResp.valueOfSuccess(artCollectionService.getStoryArts(baseIdReq));
    }

    @ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.POST)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return CodeDataResp.valueOfSuccess(artCollectionService.getArtDetail(baseIdReq));
    }


    @ApiOperation(value="删除")
    @OperationLogAnnotation(operModul = "艺术藏品-删除藏品",operDesc = "删除信息")
    @RequestMapping(value="/remove", method= RequestMethod.POST)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }
    

}

