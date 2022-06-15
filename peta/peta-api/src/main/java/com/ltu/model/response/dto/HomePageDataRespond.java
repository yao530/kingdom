package com.ltu.model.response.dto;

import com.ltu.model.response.base.DateSumReviewData;
import com.ltu.model.response.base.StatusSumReviewData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class HomePageDataRespond {
    @ApiModelProperty(value="客户数量")
    private Integer customerTotalAmount;

    @ApiModelProperty(value="特别关注客户")
    private Integer customerSpecial;

    @ApiModelProperty(value="新增客户")
    private Integer customerNew;

    @ApiModelProperty(value="商机成功率")
    private BigDecimal opportunitySuccessRate;

    @ApiModelProperty(value="进行中商机")
    private Integer activeOpportunity;

    @ApiModelProperty(value="进行中商机金额")
    private BigDecimal activeOpportunityPriceAmount;

    @ApiModelProperty(value="业务员数量")
    private Integer sellerAmount;

    @ApiModelProperty(value="拜访次数")
    private Integer dropInTimes;

    @ApiModelProperty(value="报价次数")
    private Integer quotedTimes;

    @ApiModelProperty(value="商机概况列表")
    List<StatusSumReviewData> opportunityReviewData;

    @ApiModelProperty(value="客户拜访情况--折线图")
    List<DateSumReviewData> dropInReviewData;
}
