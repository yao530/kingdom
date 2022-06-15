package com.ltu.model.request.artCollections;

import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created
 */
@Data
public class TranserCollectionReq {

    @ApiModelProperty(value = " 转赠对应的ID ")
    private Integer transferThingId;

    @ApiModelProperty(value = " 被转赠的用户手机号 ")
    private String userPhone;

}
