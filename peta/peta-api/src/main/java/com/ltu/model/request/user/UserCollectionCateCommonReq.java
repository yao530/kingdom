package com.ltu.model.request.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class UserCollectionCateCommonReq {
    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id，必传", required = true)
    @NotNull(message = "userId不能为空")
    private String userId;
    /**
     * 主键id
     */
    @ApiModelProperty(value="主键id，修改和删除必传")
    private String id;
    /**
     * 标题名称
     */
    @ApiModelProperty(value="标题，新建时必传")
    private String title;
    /**
     * 备注信息
     */
    @ApiModelProperty(value="备注信息，宣传")
    private String remark;
}
