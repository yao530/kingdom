package com.ltu.model.request.versioncentre;

import com.ltu.model.request.base.BaseFilterPageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VersionCentrePageReq extends BaseFilterPageReq {
    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(value = "默认1，0禁用，1启用")
    private Integer status;

    @ApiModelProperty(value = "类型：1-热更新、2-全量建议更新、3-全量强制更新")
    private Integer type;

    @ApiModelProperty(value = "平台：1-安卓、2-苹果")
    private Integer platform;
}
