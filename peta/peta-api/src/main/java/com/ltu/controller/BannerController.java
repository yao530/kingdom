package com.ltu.controller;

import com.ltu.mapper.BannerMapper;

import com.ltu.model.request.banner.BannerCommonReq;
import com.ltu.model.request.banner.BannerPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.ltu.model.response.base.CodeDataResp;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ltu.domain.mp_entity.BannerEntity;



/**
 * <p>
 * 广告banner 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2021-12-02
 */
@RestController
@RequestMapping("/bannerEntity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api( "广告banner")
public class BannerController extends BaseController<BannerMapper, BannerEntity>{


    private final BannerService bannerService;

    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<Page<BannerEntity>> getList(@RequestBody BannerPageReq req ) {
        return bannerService.getList(req);
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/edit", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody BannerCommonReq req) {
        return bannerService.saveOrUpdate(req);
    }

 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.GET)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }

    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.POST)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }


}

