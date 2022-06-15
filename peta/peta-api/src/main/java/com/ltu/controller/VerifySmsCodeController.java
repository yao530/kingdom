package com.ltu.controller;

import com.ltu.model.request.smscode.GetSmsCodeReq;
import com.ltu.model.request.smscode.VerifySmsCodeReq;
import com.ltu.util.ext.kaptcha.KaptchaUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ltu.service.VerifySmsCodeService;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ltu.model.response.base.CodeDataResp;
import javax.validation.Valid;

/**
 * <p>
 *  前端控制器
 * </p>
 */
@RestController
@RequestMapping("/verifySmsCode")
@Api(tags = "A-短信校验码模块")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VerifySmsCodeController {
    private final VerifySmsCodeService verifySmsCodeService;

    @ApiOperation(value="G:获取短信验证码", notes = "图形验证码在/kaptcha/getCode获取，请求成功后尚无真实短信，可从返回数据smsCode字段获取短信码")
    @RequestMapping(value="/getSmsCode", method= RequestMethod.POST)
    public CodeDataResp getSmsCode(@Valid @RequestBody GetSmsCodeReq req) {
        //检查验证码
//        if (!KaptchaUtil.checkVerifyCode(req.getKaptcha(), req.getKaptchaUUID()))
//             return CodeDataResp.valueOfFailed("验证码错误");

        return verifySmsCodeService.getSmsCode(req) ?
                CodeDataResp.valueOfSuccessEmptyData() : CodeDataResp.valueOfFailed("Failed.");
    }

    @ApiOperation(value="G:校验短信码", notes = "status=200是成功，其他都是验证失败，失败可展示message字段")
    @RequestMapping(value="/verifySmsCode", method= RequestMethod.POST)
    public CodeDataResp verifySmsCode(@Valid @RequestBody VerifySmsCodeReq verifySmsCodeReq) {
        return verifySmsCodeService.verifySmsCode(verifySmsCodeReq) ?
                CodeDataResp.valueOfSuccessEmptyData() : CodeDataResp.valueOfFailed("Failed.");
    }
}

