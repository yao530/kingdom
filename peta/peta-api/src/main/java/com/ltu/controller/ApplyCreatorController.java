package com.ltu.controller;

import java.util.List;

import com.ltu.model.request.base.PageReqData;
import com.ltu.model.request.character.ApplyCreatorReq;
import com.ltu.model.request.character.CheckCreatorReq;
import com.ltu.service.ApplyCreatorService;
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

import com.ltu.domain.mp_entity.ApplyCreatorEntity;
import com.ltu.mapper.ApplyCreatorMapper;


/**
 * <p>
 * 创作者申请 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-04-16
 */
@RestController
@RequestMapping("/applyCreator")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "创作者申请")
public class ApplyCreatorController extends BaseServiceImpl<ApplyCreatorMapper, ApplyCreatorEntity>{


    @Autowired
    ApplyCreatorService applyCreatorService;

    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<ApplyCreatorEntity>> getMePage(@RequestBody PageReqData req ) {
    	return applyCreatorService.getPage(req);
    }

    @ApiOperation(value="申请创作者")
    @RequestMapping(value="/apply", method= RequestMethod.POST)
    public CodeDataResp applyCreator(@RequestBody ApplyCreatorReq req) {
        return applyCreatorService.saveOrUpdate(req);
    }

    @ApiOperation(value="审核")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp checkCreator(@RequestBody CheckCreatorReq req) {
        return applyCreatorService.checkApplyCreator(req);
    }

    @ApiOperation(value="获取用户的申请")
    @RequestMapping(value="/getUserApply", method= RequestMethod.POST)
    public CodeDataResp getUserApply(@RequestBody BaseIdReq baseIdReq) {
        return applyCreatorService.getUserApply(baseIdReq);
    }

 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.POST)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }

    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.POST)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }
    


}

