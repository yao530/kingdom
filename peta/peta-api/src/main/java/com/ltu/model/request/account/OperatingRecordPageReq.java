package com.ltu.model.request.account;

import com.ltu.model.request.base.BaseFilterPageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OperatingRecordPageReq extends BaseFilterPageReq {

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;
}
