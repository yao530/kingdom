package com.ltu.controller;

import java.util.List;

import com.ltu.model.response.dto.CollectionSettingDetailDto;
import com.ltu.service.CollectionSoldSettingService;
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

import com.ltu.domain.mp_entity.CollectionSoldSettingEntity;
import com.ltu.mapper.CollectionSoldSettingMapper;


/**
 * <p>
 * 藏品销售设置 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */
@RestController
@RequestMapping("/collectionSoldSetting")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "藏品销售设置")
public class CollectionSoldSettingController extends BaseServiceImpl<CollectionSoldSettingMapper, CollectionSoldSettingEntity>{


    @Autowired
    CollectionSoldSettingService collectionSoldSettingService;

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody CollectionSettingDetailDto collectionSettingDetailDto) {
        return collectionSoldSettingService.creatSetting(collectionSettingDetailDto);
    }

    
 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.POST)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return CodeDataResp.valueOfSuccess(collectionSoldSettingService.getCollectionSettingDetail(baseIdReq));
    }



}

