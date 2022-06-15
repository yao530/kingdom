package com.ltu.model.request.user;

import com.ltu.model.request.base.BaseFilterPageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPageReq extends BaseFilterPageReq {

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "用户类型 1普通客户 2内部员工")
    private Integer userType;



    @ApiModelProperty(value="排序字段，默认为create_time", notes = "多字段用英文逗号隔开，多字段排序必须对应多个升降序字段")
    private String sortType = "create_time";
}
