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

import com.ltu.domain.mp_entity.BlockchainLogsInfoEntity;
import com.ltu.mapper.BlockchainLogsInfoMapper;


/**
 * <p>
 * 区块链请求信息 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-04-12
 */
@RestController
@RequestMapping("/blockchainLogsInfoEntity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "区块链请求信息")
public class BlockchainLogsInfoController extends BaseServiceImpl<BlockchainLogsInfoMapper, BlockchainLogsInfoEntity>{
    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<BlockchainLogsInfoEntity>> getMePage(@RequestBody  BaseFilterPageReq<BlockchainLogsInfoEntity> req ) {
    	return super.getMePage(req);
    }
    
    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<BlockchainLogsInfoEntity>> getList(@RequestBody  BaseFilterPageReq<BlockchainLogsInfoEntity> req ) {
        return super.getMeList(req);
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/edit", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody BlockchainLogsInfoEntity req) {
        return super.edit(req);
    }
    @ApiOperation(value="批量修改数据")
    @RequestMapping(value="/edits", method= RequestMethod.POST)
    public CodeDataResp edits(@RequestBody List<BlockchainLogsInfoEntity> reqs) {
    	super.saveOrUpdateBatch(reqs);
    	return   CodeDataResp.valueOfSuccessEmptyData();
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
    
     @ApiOperation(value="删除s")
    @RequestMapping(value="/removes", method= RequestMethod.POST)
    public CodeDataResp removes(@RequestBody List<Integer> ids) {
    		super.removeByIds(ids);
        return CodeDataResp.valueOfSuccessEmptyData();
    }

}

