package com.ltu.model.request.banner;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class BannerCommonReq extends BaseIdReq{
    @ApiModelProperty(value = " banner_name:内置应用名称 ")
    private String bannerName;
    @ApiModelProperty(value = " banner_image:内置应用跳转地址 ")
    private String bannerImage;
    @ApiModelProperty(value = " location_type:内置应用模块：1首页 2实时监测 3风险预警 4数据统计 ")
    private Integer locationType=1;
    @ApiModelProperty(value = " banner_type:广告内容类型 1纯图片 2图文详情 3网址跳转 4其他 ")
    private Integer bannerType;
    @ApiModelProperty(value = " context:图文内容 ")
    private String context;
    @ApiModelProperty(value = " link_url:跳转网址 ")
    private String linkUrl;

    private Integer sort;

    @ApiModelProperty(value = " remark:备注 ")
    private String remark;
}
