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

import com.ltu.domain.mp_entity.UserInviteRecordEntity;
import com.ltu.mapper.UserInviteRecordMapper;


/**
 * <p>
 * 用户邀请新用户记录 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */
@RestController
@RequestMapping("/userInviteRecord")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "用户邀请新用户记录")
public class UserInviteRecordController extends BaseServiceImpl<UserInviteRecordMapper, UserInviteRecordEntity>{


    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<UserInviteRecordEntity>> getMePage(@RequestBody  BaseFilterPageReq<UserInviteRecordEntity> req ) {
    	return super.getMePage(req);
    }
//
//    @ApiOperation(value="获取列表")
//    @RequestMapping(value="/getList", method= RequestMethod.POST)
//    public CodeDataResp<List<UserInviteRecordEntity>> getList(@RequestBody  BaseFilterPageReq<UserInviteRecordEntity> req ) {
//        return super.getMeList(req);
//    }



}

