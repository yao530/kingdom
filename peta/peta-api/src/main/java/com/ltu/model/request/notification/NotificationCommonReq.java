package com.ltu.model.request.notification;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class NotificationCommonReq {
    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容存储在MongoDB，用key读取")
    private String content;

    @ApiModelProperty(value = "作者id")
    private String authorId;

    @ApiModelProperty(value = "作者")
    private String author;
}
