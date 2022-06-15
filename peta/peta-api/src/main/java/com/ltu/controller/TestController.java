package com.ltu.controller;

import com.ltu.config.shiro.ShiroUtil;
import com.ltu.model.response.CodeResp;
import com.ltu.model.response.base.CodeDataResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="Z-测试模块")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {
    @ApiOperation(value="G:测试")
    @RequestMapping(value="/index",method= RequestMethod.GET)
    public String index() {
        return "服务正常运行中";
    }

    @ApiOperation(value="G:返回无权限信息")
    @RequestMapping(value="/info403",method=RequestMethod.GET)
    public CodeDataResp info403() {
        return CodeDataResp.valueOfErrorUnAuthorized();
    }

    @ApiOperation(value="G:测试登陆情况")
    @RequestMapping(value="/authTest",method=RequestMethod.POST)
    public CodeResp authTest() {
        ShiroUtil.getPrincipalUser();
        return CodeDataResp.valueOfSuccess("认证用户，访问接口成功");
    }

    @ApiOperation(value="G:测试uuid session", notes = "登录后请求此接口，会返回登陆者的userId")
    @RequestMapping(value="/uuidSessionTest",method=RequestMethod.POST)
    public CodeResp uuidSessionTest() {
        return CodeDataResp.valueOfSuccess(ShiroUtil.getPrincipalUserId());
    }
}
