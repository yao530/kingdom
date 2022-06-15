package com.ltu.controller;

import java.util.List;

import com.ltu.domain.mp_entity.CreatorApplySettingEntity;
import com.ltu.model.response.CodeResp;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.CreatorApplySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;

import com.ltu.service.impl.BaseServiceImpl;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


import com.ltu.mapper.CreatorApplySettingMapper;


/**
 * <p>
 * 创作者招募活动设置 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-05-18
 */
@RestController
@RequestMapping("/creatorApplySetting")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "创作者招募活动设置")
public class CreatorApplySettingController extends BaseServiceImpl<CreatorApplySettingMapper, CreatorApplySettingEntity>{

    @Autowired
    CreatorApplySettingService creatorApplySettingService;

    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<CreatorApplySettingEntity>> getMePage(@RequestBody  BaseFilterPageReq<CreatorApplySettingEntity> req ) {
    	return super.getMePage(req);
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp create(@RequestBody CreatorApplySettingEntity req) {

        return creatorApplySettingService.saveOrUpDate(req);
    }

 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getSetting", method= RequestMethod.POST)
    public CodeDataResp getDetail() {
        return CodeDataResp.valueOfSuccess(creatorApplySettingService.getNewSetting());
    }

    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.POST)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }

}

