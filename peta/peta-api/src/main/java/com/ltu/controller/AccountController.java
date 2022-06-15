package com.ltu.controller;

import java.util.List;


import com.ltu.mapper.AccountMapper;
import com.ltu.model.request.account.AccountPageReq;
import com.ltu.model.request.account.AccountReq;
import com.ltu.model.response.base.PageData;
import com.ltu.service.AccountService;
import com.ltu.service.impl.AccountServiceImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.base.CodeDataResp;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ltu.domain.mp_entity.AccountEntity;



/**
 * <p>
 * 角色管理 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */
@RestController
@RequestMapping("/accountEntity")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "管理员账号")
public class AccountController extends BaseController<AccountMapper, AccountEntity>{


    @Autowired
    AccountService accountService;


    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<PageData<AccountEntity>> getList(AccountPageReq pageReq) {
        return accountService.getList(pageReq);
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp edit(@RequestBody AccountReq req) {
        return accountService.saveOrUpdate(req);
    }

    @ApiOperation(value="分页获取代理商列表")
    @RequestMapping(value="/getAgentPageList", method= RequestMethod.POST)
    public CodeDataResp<PageData<AccountEntity>> getAgent(@RequestBody AccountPageReq pageReq) {
        return accountService.getSysAccount(pageReq);
    }

    @ApiOperation(value="获取代理商列表")
    @RequestMapping(value="/getAgentList", method= RequestMethod.POST)
    public CodeDataResp<List<AccountEntity>> getAgentList() {
        return accountService.getAgentList();
    }

    @ApiOperation(value="获取租户列表")
    @RequestMapping(value="/getTenantList", method= RequestMethod.POST)
    public CodeDataResp<PageData<AccountEntity>> getTenantList(AccountPageReq pageReq) {
        return accountService.getTenantList(pageReq);
    }



    @ApiOperation(value="获取当前账号信息")
    @RequestMapping(value="/getCurrentAccount", method= RequestMethod.POST)
    public CodeDataResp getCurrentAccount() {
        return accountService.getCurrentAccount();
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

