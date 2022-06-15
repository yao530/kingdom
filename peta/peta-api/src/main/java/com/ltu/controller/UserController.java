package com.ltu.controller;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import com.ltu.domain.mp_entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ltu.config.shiro.ShiroUtil;
import com.ltu.constant.CommonConstant;
import com.ltu.mapper.UserMapper;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.user.UpdateUserInfoReq;
import com.ltu.model.request.user.UserPageReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.dto.UserBaseInfoItem;
import com.ltu.model.response.dto.UserInfoItem;
import com.ltu.service.UserService;
import com.ltu.service.impl.BaseServiceImpl;
import com.ltu.util.BeanMapper;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author vk@rongding
 * @since 2020-08-15
 */
@RestController
@RequestMapping("/user") 
@Api(tags = "B-用户模块")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController  extends BaseServiceImpl<UserMapper, UserEntity>{

    private final UserService userService;

    @ApiOperation(value="G:获取用户基础信息列表")
    @RequestMapping(value="/getBaseInfoList", method= RequestMethod.POST)
    public CodeDataResp<UserBaseInfoItem> getBaseInfoList(@RequestBody UserPageReq pageReq) {
        return BeanMapper.copyRespDataList(userService.getList(pageReq), UserBaseInfoItem.class);
    }

    @ApiOperation(value="MP:获取权益用户")
    @RequestMapping(value="/getSysUsers", method= RequestMethod.POST)
    public CodeDataResp<List<UserEntity>> getSysUsers() {
        return CodeDataResp.valueOfSuccess(userService.getCreatorUsers());
    }


    @ApiOperation(value="MP:获取用户列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<UserEntity> getList(@RequestBody UserPageReq pageReq) {
        return userService.getList(pageReq);
    }

    @ApiOperation(value="MP:获取用户完整信息")
    @RequestMapping(value="/getDetail", method= RequestMethod.POST)
    public CodeDataResp getDetail() {
        BaseIdReq baseIdReq = new BaseIdReq( ShiroUtil.getPrincipalUserId());
        return BeanMapper.copyRespDataItem(userService.getDetail(baseIdReq), UserBaseInfoItem.class.getName());
    }

    @ApiOperation(value="G:实名认证")
    @RequestMapping(value="/submitIdentityInfo", method= RequestMethod.POST)
    public CodeDataResp submitIdentityInfo(@RequestBody UpdateUserInfoReq userInfoReq) {
        return  userService.updateUserInfo(userInfoReq);
    }

    
    @ApiOperation(value="删除")
    @RequestMapping(value="/remove/{id}", method= RequestMethod.GET)
    public CodeDataResp remove(@PathVariable(name="id") String id) {
    				userService.removeById(id);
        return  CodeDataResp.valueOfSuccessEmptyData();
    }
    
}

