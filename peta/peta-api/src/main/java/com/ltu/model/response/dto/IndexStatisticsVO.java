package com.ltu.model.response.dto;

import java.util.List;

import com.ltu.model.request.CommonRequest;
import com.ltu.model.response.reagent.CustomerCountDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("首页统计数据")
public class IndexStatisticsVO extends CommonRequest {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value="特别关注客户数，旧：focusCustomers")
    private int customerOnSpecialFocusNum;
    @ApiModelProperty(value="拜访次数，旧：seeCustomers")
    private int dropInTimes;
    @ApiModelProperty(value="进行中的商机数，旧：ongoingAmount")
    private int oppProcessingNum;
    @ApiModelProperty(value="进行中的商机金额，分为单位，旧：ongoingMoneys")
    private long oppProcessingMoney;

    @ApiModelProperty(value="初步意向的商机数，旧：无")//todo:--
    private int oppPreliminaryNum;
    @ApiModelProperty(value="初步意向的商机金额，分为单位，旧：无")//todo:--
    private long oppPreliminaryMoney;

    @ApiModelProperty(value="已识别的商机数，旧：无")//todo:--
    private int oppDiscernedNum;
    @ApiModelProperty(value="已识别的商机金额，分为单位，旧：无")//todo:--
    private long oppDiscernedMoney;

    @ApiModelProperty(value="已确认的商机数，旧：无")//todo:--
    private int oppConfirmedNum;
    @ApiModelProperty(value="已确认的商机金额，分为单位，旧：无")//todo:--
    private long oppConfirmedMoney;

    @ApiModelProperty(value="已制定方案的商机数，旧：无")//todo:--
    private int oppPlanedNum;
    @ApiModelProperty(value="已制定方案的商机金额，分为单位，旧：无")//todo:--
    private long oppPlanedMoney;
    
    @ApiModelProperty(value="赢单的商机数，旧：finishAmount")
    private int oppCompletedNum;
    @ApiModelProperty(value="赢单的商机金额，分为单位，旧：finishMoneys")
    private long oppCompletedMoney;
    
	@ApiModelProperty(value = "丢单商机数量，旧：failedNums")
	private int oppFailedNum;
	@ApiModelProperty(value = "丢单商机金额，旧：failedMoneys")
	private long oppFailedMoney;

	@ApiModelProperty(value = "取消的商机数量，旧：cancleNums")
	private int oppCanceledNum;
	@ApiModelProperty(value = "取消的商机总额，旧：cancleMoneys")
	private long oppCanceledMoney;

    
    @ApiModelProperty(value="成功率，旧：successRate")
    private Double oppSuccessRate;
    @ApiModelProperty(value="报价次数，旧：offerAmount")
    private int oppOfferTimes;
    
    @ApiModelProperty(value="客户总数，旧：customerAmount")
    private int customerAmount;
    @ApiModelProperty(value="新增客户数，旧：newCustomerAmount")
    private int customerIncreasingNum;
    @ApiModelProperty(value="跟进客户数，旧：trackingCustomers")
    private int customerTrackingNum;
    
    @ApiModelProperty(value="商机跟进次数，旧：trackingAmount")
    private int oppTrackingAmount;
    
    @ApiModelProperty(value="新增客户数统计，旧：newCoustomerCount")
    private List<CustomerCountDTO> customerIncreasingNumList;
    @ApiModelProperty(value="跟进客户数统计，旧：trackingCount")//todo:--
    private List<CustomerCountDTO> customerTrackingNumList;
   
}
