package com.ltu.model.request.collectionOrder;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/21.
 */
@Data

public class CreateOrderReq extends BaseIdReq {

    private Integer payNumber=1;
}
