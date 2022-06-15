package com.ltu.model.request.collectionOrder;

import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/21.
 */
@Data

public class OrderPageReq extends PageReqData {


    @ApiModelProperty(value = " status:支付状态：0待支付1支付成功2拉起收银台成功,正在支付-1其他 ")
    private Integer status;

    @ApiModelProperty(value = " is_pay:是否支付0否1是默认0 ")
    private Integer isPay;
    @ApiModelProperty(value = " pay_way:支付方式1微信支付2支付宝 ")
    private Integer payWay;

    @ApiModelProperty(value = " order_no:订单编号 ")
    private String orderNo;

    @ApiModelProperty(value = " mobile_phone:账号手机号 ")
    private String mobilePhone;

    @ApiModelProperty(value = " character_id:故事人物ID ")
    private Integer characterId;

    @ApiModelProperty(value = " art_creator_id:艺术创作者ID ")
    private Integer artCreatorId;

    @ApiModelProperty(value = " collection_id:藏品ID ")
    private Integer collectionId;


    private Long startTime;
    private Long endTime;

}
