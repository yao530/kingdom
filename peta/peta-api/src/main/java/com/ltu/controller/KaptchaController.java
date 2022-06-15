package com.ltu.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.ltu.constant.CommonConstant;
import com.ltu.util.redis.RedistUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

@Api(tags="A-图形验证码模块")
@RequestMapping("/kaptcha")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KaptchaController {

    private final Producer captchaProducer;
    private final RedistUtil redistUtil;

    @ApiOperation(value="G:获取图形验证码", notes = "返回图片内容，可以直接放到src标签里面")
    @RequestMapping(value="/getCode",method= RequestMethod.GET)
    public void getKaptchaImage(HttpServletResponse response) throws Exception {
        Session session = SecurityUtils.getSubject().getSession();
        //生成验证码
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @ApiOperation(value="G:通过UUID获取图形验证码", notes = "返回图片内容，可以直接放到src标签里面")
    @RequestMapping(value="/getCodeByUUID",method=RequestMethod.GET)
    public void getKaptchaImageByUUID(@RequestParam("uuid")String uuidStr, HttpServletResponse response) throws Exception {
        //生成验证码
        String capText = captchaProducer.createText();
        redistUtil.setValue(CommonConstant.KAPTCHA_REDIS_DEFAULT_PREFIX+uuidStr,
                capText,
                Long.valueOf(CommonConstant.DEFAULT_KAPTCHA_EXPIRE_TIME));

        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

}
