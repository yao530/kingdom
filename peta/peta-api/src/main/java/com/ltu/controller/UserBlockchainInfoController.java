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

import com.ltu.domain.mp_entity.UserBlockchainInfoEntity;
import com.ltu.mapper.UserBlockchainInfoMapper;


/**
 * <p>
 * 用户区块链信息 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-04-06
 */
@RestController
@RequestMapping("/userBlockchainInfoEntity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "用户区块链信息")
public class UserBlockchainInfoController extends BaseServiceImpl<UserBlockchainInfoMapper, UserBlockchainInfoEntity>{
    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<UserBlockchainInfoEntity>> getMePage(@RequestBody  BaseFilterPageReq<UserBlockchainInfoEntity> req ) {
    	return super.getMePage(req);
    }
    
    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<UserBlockchainInfoEntity>> getList(@RequestBody  BaseFilterPageReq<UserBlockchainInfoEntity> req ) {
        return super.getMeList(req);
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/edit", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody UserBlockchainInfoEntity req) {
        return super.edit(req);
    }
    @ApiOperation(value="批量修改数据")
    @RequestMapping(value="/edits", method= RequestMethod.POST)
    public CodeDataResp edits(@RequestBody List<UserBlockchainInfoEntity> reqs) {
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

