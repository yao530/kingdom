package com.ltu.model.response.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StatusSumReviewData {

    @ApiModelProperty(value="ηΆζ")
    private Integer status;

    @ApiModelProperty(value="ζ°ι")
    private Integer amount;
}
