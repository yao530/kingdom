package com.ltu.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.domain.mp_entity.ArtCollectionEntity;
import com.ltu.domain.mp_entity.PlatformInfosEntity;
import com.ltu.mapper.PlatformInfosMapper;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.platinfos.PlatFormReq;
import com.ltu.model.request.platinfos.PlatFormTypeReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.PlatformInfosService;
import com.ltu.service.impl.BaseServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 平台信息配置 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-28
 */
@RestController
@RequestMapping("/platformInfos")
@Api(tags= "平台设置")
public class PlatformInfosController extends BaseServiceImpl<PlatformInfosMapper, PlatformInfosEntity> {


    @Autowired
    PlatformInfosService platformInfosService;



    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<PlatformInfosEntity>> getMePage(@RequestBody BaseFilterPageReq<PlatformInfosEntity> req ) {
        return super.getMePage(req);
    }


    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp saveOrUpdate(@RequestBody PlatFormReq req) {
        return platformInfosService.saveOrUpdate(req);
    }


    @ApiOperation(value="获取平台设置")
    @RequestMapping(value="/getInfos", method= RequestMethod.POST)
    public CodeDataResp<PlatformInfosEntity> getInfos(@RequestBody PlatFormTypeReq req) {
        return platformInfosService.getInfoByType(req);
    }

    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.GET)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }


}

