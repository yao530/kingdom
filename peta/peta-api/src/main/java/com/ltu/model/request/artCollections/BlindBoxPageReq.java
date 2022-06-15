package com.ltu.model.request.artCollections;

import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class BlindBoxPageReq extends PageReqData{


    @ApiModelProperty(value = " status:状态 0未开启 1已开启 2已转增 ")
    private Integer status=0;


}
