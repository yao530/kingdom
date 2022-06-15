package com.ltu.model.request.websiteinfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@ApiModel(value="WebsiteInfo对象", description="")
@Data
public class WebsiteInfoCommonReq {
    @ApiModelProperty(value = "网站名称")
    private String title;

    @ApiModelProperty(value = "网站logo主图，导航栏左上角显示")
    private String logoMain;

    @ApiModelProperty(value = "登陆页logo图，用于登陆页显示")
    private String logoLogin;

    @ApiModelProperty(value = "ico小图标，用于浏览器标签显示")
    private String logoIco;

    @ApiModelProperty(value = "备案号")
    private String beianNo;

    @ApiModelProperty(value = "版权信息")
    private String copyright;

    @ApiModelProperty(value = "客服电话")
    private String servicePhoneNum;

    @ApiModelProperty(value = "微信二维码")
    private String qrcodeWx;

    @ApiModelProperty(value = "APP二维码")
    private String qrcodeApp;

    @ApiModelProperty(value = "网站关键字，用于SEO优化")
    private String keywords;
}
