package com.ltu.controller;

import java.util.List;

import com.ltu.service.TransferCollectionRecordService;
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

import com.ltu.domain.mp_entity.TransferCollectionRecordEntity;
import com.ltu.mapper.TransferCollectionRecordMapper;


/**
 * <p>
 * 支付记录 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/transferCollection")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "支付记录")
public class TransferCollectionRecordController extends BaseServiceImpl<TransferCollectionRecordMapper, TransferCollectionRecordEntity>{


    @Autowired
    TransferCollectionRecordService transferCollectionRecordService;

    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<TransferCollectionRecordEntity>> getMePage(@RequestBody  BaseFilterPageReq<TransferCollectionRecordEntity> req ) {
    	return super.getMePage(req);
    }
    


    @ApiOperation(value="发起转赠")
    @RequestMapping(value="/makeTransfer", method= RequestMethod.POST)
    public CodeDataResp makeTransfer(@RequestBody BaseIdReq req) {
        return transferCollectionRecordService.makeTransfer(req);
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

