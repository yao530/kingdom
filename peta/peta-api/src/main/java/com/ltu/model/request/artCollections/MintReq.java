package com.ltu.model.request.artCollections;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class MintReq extends BaseIdReq{



    @ApiModelProperty(value = " collection_type:数字藏品类型 1avatar类 2权益藏品类 3收藏品 4非卖品 ")
    private Integer collectionType;


    @ApiModelProperty(value = " contract_code_storage_url:数字藏品风格类型 ")
    private String contractCodeStorageUrl;

    @ApiModelProperty(value = " smart_contract_address:合约地址 ")
    private String smartContractAddress;



    @ApiModelProperty(value = " remark:备注 ")
    private String remark;

    private Integer sort;
}
