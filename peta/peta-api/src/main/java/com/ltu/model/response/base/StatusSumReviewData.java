package com.ltu.model.response.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatusSumReviewData {

    @ApiModelProperty(value="状态")
    private Integer status;

    @ApiModelProperty(value="数量")
    private Integer amount;
}
