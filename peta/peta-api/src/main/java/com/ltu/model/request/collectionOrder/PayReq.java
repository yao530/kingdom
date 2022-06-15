package com.ltu.model.request.collectionOrder;

import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/21.
 */
@Data

public class PayReq extends BaseIdReq {



    @ApiModelProperty(value = " pay_way:支付方式1微信支付2支付宝 3其他 ")
    private Integer payWay;

    private Integer artCollectionId;


}
