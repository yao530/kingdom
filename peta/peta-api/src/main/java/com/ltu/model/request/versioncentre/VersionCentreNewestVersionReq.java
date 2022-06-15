package com.ltu.model.request.versioncentre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VersionCentreNewestVersionReq {
    @NotNull(message = "平台值不能为空")
    @ApiModelProperty(value = "传入平台值：1安卓，2苹果")
    private Integer platform;
}
