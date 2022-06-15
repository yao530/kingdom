package com.ltu.model.request.account;

import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccountPageReq extends PageReqData {
    @ApiModelProperty(value="登录账号")
    private String userName;

    @ApiModelProperty(value="手机号")
    private String mobile;

    @ApiModelProperty(value="角色")
    private String roleId;

    @ApiModelProperty(value="排序字段，默认为create_time", notes = "多字段用英文逗号隔开，多字段排序必须对应多个升降序字段")
    private String sortType = "create_time";
}
