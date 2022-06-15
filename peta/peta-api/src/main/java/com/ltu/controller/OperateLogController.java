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

import com.ltu.domain.mp_entity.OperateLogEntity;
import com.ltu.mapper.OperateLogMapper;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-04-17
 */
@RestController
@RequestMapping("/operateLog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "")
public class OperateLogController extends BaseServiceImpl<OperateLogMapper, OperateLogEntity>{


    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<OperateLogEntity>> getMePage(@RequestBody  BaseFilterPageReq<OperateLogEntity> req ) {
    	return super.getMePage(req);
    }
}

