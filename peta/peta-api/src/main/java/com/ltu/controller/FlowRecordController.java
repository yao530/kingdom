package com.ltu.controller;

import java.util.List;

import com.ltu.model.request.base.BaseIdAndStatusReq;
import com.ltu.model.request.character.FlowRecordPageReq;
import com.ltu.service.FlowRecordService;
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

import com.ltu.domain.mp_entity.FlowRecordEntity;
import com.ltu.mapper.FlowRecordMapper;


/**
 * <p>
 * 关注人物记录 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/flowRecord")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "关注人物记录")
public class FlowRecordController extends BaseServiceImpl<FlowRecordMapper, FlowRecordEntity>{

    @Autowired
    FlowRecordService flowRecordService;

    @ApiOperation(value="获取我的粉丝")
    @RequestMapping(value="/getFuns", method= RequestMethod.POST)
    public CodeDataResp<Page<FlowRecordEntity>> getFuns(@RequestBody FlowRecordPageReq pageReq ) {
    	return flowRecordService.findVirtualChapracterFuns(pageReq);
    }

    @ApiOperation(value="获取我关注的")
    @RequestMapping(value="/getFlows", method= RequestMethod.POST)
    public CodeDataResp<Page<FlowRecordEntity>> getFlows(@RequestBody FlowRecordPageReq pageReq ) {
        return flowRecordService.findUserFlows(pageReq);
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp saveOrUpdate(@RequestBody BaseIdAndStatusReq req) {
        return flowRecordService.saveOrUpdate(req);
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

