package com.ltu.controller;

import java.util.List;

import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
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

import com.ltu.domain.mp_entity.MetaEntity;
import com.ltu.mapper.MetaMapper;


/**
 * <p>
 * 元宇宙 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-05-20
 */
@RestController
@RequestMapping("/meta")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "元宇宙")
public class MetaController extends BaseServiceImpl<MetaMapper, MetaEntity>{


    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<MetaEntity>> getMePage(@RequestBody  BaseFilterPageReq<MetaEntity> req ) {
    	return super.getMePage(req);
    }
    
    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<MetaEntity>> getList(@RequestBody  BaseFilterPageReq<MetaEntity> req ) {
        return super.getMeList(req);
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody MetaEntity req) {
        if (StrUtils.isVaileNum(req.getId())) req.setUpdateTime(DateUtils.currentSecs());
        else req.setCreateTime(DateUtils.currentSecs());
        return super.edit(req);
    }

    
 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.GET)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }

    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.GET)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }
    


}

