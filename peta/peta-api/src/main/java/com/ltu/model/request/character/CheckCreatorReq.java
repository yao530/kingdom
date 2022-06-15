package com.ltu.model.request.character;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CheckCreatorReq extends BaseIdReq{


    @ApiModelProperty(value = " role_id:申请创作者角色ID ")
    private Integer characterRoleId;

    @ApiModelProperty(value = " meta_id:宇宙ID ")
    private Integer metaId;

    @ApiModelProperty(value = " check_remark:审核备注 ")
    private String checkRemark;

    @ApiModelProperty(value = " status:状态 0无效 1待审核 2审核通过 3审核不通过 ")
    private Integer status=1;



}
