package com.ltu.model.response.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DocumentListItem {

    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="内容")
    private String content;
}
