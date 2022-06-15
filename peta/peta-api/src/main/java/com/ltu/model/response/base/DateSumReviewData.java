package com.ltu.model.response.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DateSumReviewData {
    @ApiModelProperty(value="日期")
    private String date;

    @ApiModelProperty(value="数量")
    private Integer amount;
}
