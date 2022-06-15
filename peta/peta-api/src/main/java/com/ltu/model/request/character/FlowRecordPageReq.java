package com.ltu.model.request.character;

import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FlowRecordPageReq extends PageReqData {

    @ApiModelProperty(value = " 用户id ")
    private Integer userId;

    @ApiModelProperty(value = " 故事人物ID ")
    private Integer characterId;

}
