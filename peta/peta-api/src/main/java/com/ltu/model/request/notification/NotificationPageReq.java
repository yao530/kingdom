package com.ltu.model.request.notification;

import com.ltu.model.request.base.BaseFilterPageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NotificationPageReq extends BaseFilterPageReq {
    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "作者id")
    private String authorId;

    @ApiModelProperty(value="排序字段，默认为create_time", notes = "多字段用英文逗号隔开，多字段排序必须对应多个升降序字段")
    private String sortType = "create_time";
}
