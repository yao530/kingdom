package com.ltu.controller;


import com.alibaba.fastjson.JSON;
import com.ltu.config.shiro.ShiroUserService;
import com.ltu.config.shiro.dto.LoginReq;
import com.ltu.config.shiro.dto.MyUsernamepwdToken;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.rabbitmq.RabbitConstants;

import java.util.UUID;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.ltu.domain.mp_entity.TransferCollectionRecordEntity;
import com.ltu.model.request.smartContract.MintReq;
import com.ltu.model.request.smartContract.RegistBlockChainAddressReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.service.TransferCollectionRecordService;
import com.ltu.service.UserCollectionsService;
import org.apache.http.HttpStatus;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ltu.model.response.CodeResp;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 *
 */
@Api(value="公共模块")
@RestController
@RequestMapping("/demo")
public class IndexController { 
	

	@Autowired
	ShiroUserService  userService;
	
	@Autowired
	UserService  userDaoImpl;
	@Resource
	private RabbitTemplate rabbitTemplate;

	@Autowired
	UserCollectionsService userCollectionsService;
	@Autowired
	TransferCollectionRecordService transferCollectionRecordService;
	
	
	@ApiOperation(value="测试死信消息")
	@RequestMapping(value="/deadMsg",method=RequestMethod.POST)
	public String deadMsg(@RequestBody TransferReq transferReq) {
		String message="超时消息";//0xd9145CCE52D386f254917e481eB44e9943F39138

		TransferCollectionRecordEntity transferCollectionRecord = transferCollectionRecordService.getById(15);
		userCollectionsService.updataTransferRecord(transferCollectionRecord);

		//超时消息
		// rabbitTemplate.convertAndSend(RabbitConstants.FANOUT_DEAD_QUEUE4, RabbitConstants.MINT_DEAD_QUEUE , JSON.toJSONString(transferReq));
		return "消息发送成功";
	}


    @ApiOperation(value="测试接口认证的情况")
  	@RequestMapping(value="/authTest",method=RequestMethod.POST)
  	public CodeResp authTest() {
    	
  		return CodeDataResp.valueOfSuccess("认证用户，访问接口成功");
  	}
    
    @RequiresRoles(value={"staff","admin"},logical=Logical.OR)
    @ApiOperation(value="测试接口认证的情况")
   	@RequestMapping(value="/roleTest",method=RequestMethod.POST)
   	public CodeResp roleTest() {
   		return CodeDataResp.valueOfSuccess("必须拥有员工或者admin角色才能访问");
   	}
  
    
    @ApiOperation(value="测试接口认证的情况")
   	@RequestMapping(value="/adminTest",method=RequestMethod.POST)
    @RequiresRoles(value={"staff"})
   	public CodeResp adminTest() {
   	try {
			return CodeDataResp.valueOfSuccess("拥有staff角色,访问成功");
   		} catch ( AuthorizationException   e) {
   			return new CodeResp(HttpStatus.SC_UNAUTHORIZED, "角色权限不足");
   		}
		
   	}
    
    
    @ApiOperation(value="测试接口认证的情况")
    @RequiresRoles(value={"manager","admin"},logical=Logical.AND)
   	@RequestMapping(value="/managerTest",method=RequestMethod.POST)
   	public CodeResp managerTest() {
   		return CodeDataResp.valueOfSuccess("认证用户，访问接口成功");
   	}

}