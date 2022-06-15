package com.ltu.model.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class UserCollectionRecordCommonReq {
    @ApiModelProperty(value = "产品id")
    private String productId;

    @ApiModelProperty(value = "方案id")
    private String collectionId;

    @ApiModelProperty(value = "备注信息")
    private String remark;
}
