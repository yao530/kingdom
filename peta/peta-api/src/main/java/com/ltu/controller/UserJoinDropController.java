package com.ltu.controller;

import java.util.List;

import com.ltu.service.UserJoinDropService;
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

import com.ltu.domain.mp_entity.UserJoinDropEntity;
import com.ltu.mapper.UserJoinDropMapper;


/**
 * <p>
 * 用户藏品抽签记录 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */
@RestController
@RequestMapping("/userJoinDrop")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "用户藏品抽签记录")
public class UserJoinDropController extends BaseServiceImpl<UserJoinDropMapper, UserJoinDropEntity>{

    @Autowired
    UserJoinDropService userJoinDropService;

    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<UserJoinDropEntity>> getMePage(@RequestBody  BaseFilterPageReq<UserJoinDropEntity> req ) {
    	return super.getMePage(req);
    }

    @ApiOperation(value="用户参加抽签活动")
    @RequestMapping(value="/joinCollectionDrop", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody BaseIdReq req) {
        return userJoinDropService.joinCollectionDrop(req);
    }


}

