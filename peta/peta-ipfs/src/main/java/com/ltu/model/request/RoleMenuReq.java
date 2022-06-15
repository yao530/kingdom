package com.ltu.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class RoleMenuReq {

    @ApiModelProperty(value = "角色id",required = true)
    @NotBlank(message = "角色id不能为空")
    @NotNull(message = "角色id不能为空")
    @Min(value = 1,message = "角色id不能为0")
    private Integer roleId;

 

}
