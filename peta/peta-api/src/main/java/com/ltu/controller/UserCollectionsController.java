package com.ltu.controller;

import java.util.List;

import com.ltu.model.request.artCollections.UserCollectionPageReq;
import com.ltu.service.UserCollectionsService;
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

import com.ltu.domain.mp_entity.UserCollectionsEntity;
import com.ltu.mapper.UserCollectionsMapper;


/**
 * <p>
 * 支付记录 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-26
 */
@RestController
@RequestMapping("/userCollections")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "用户藏品记录")
public class UserCollectionsController extends BaseServiceImpl<UserCollectionsMapper, UserCollectionsEntity>{

    @Autowired
    UserCollectionsService userCollectionsService;

    @ApiOperation(value="用户获取主AVATAR列表")
    @RequestMapping(value="/getMainCollection", method= RequestMethod.POST)
    public CodeDataResp<Page<UserCollectionsEntity>> getMainCollection(@RequestBody UserCollectionPageReq req ) {
    	return userCollectionsService.getUserMainAvatarCollections(req);
    }

    @ApiOperation(value="根据IP查询NFT合辑")
    @RequestMapping(value="/getAlumnCollections", method= RequestMethod.POST)
    public CodeDataResp getAlumnCollections(@RequestBody BaseIdReq req ) {
        return userCollectionsService.getAlumnCollections(req);
    }

    @ApiOperation(value="设置主AVATAR主展示")
    @RequestMapping(value="/setMainDisplay", method= RequestMethod.POST)
    public CodeDataResp<Page<UserCollectionsEntity>> getAllCollectionsByMainAvatar(@RequestBody BaseIdReq req ) {
        return userCollectionsService.setMainDisPlayByAvatar(req);
    }


 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.POST)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }


}

