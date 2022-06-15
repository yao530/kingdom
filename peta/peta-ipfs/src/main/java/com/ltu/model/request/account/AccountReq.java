package com.ltu.model.request.account;

import com.ltu.model.request.CommonRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=true)
public class AccountReq extends CommonRequest {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;

    @ApiModelProperty(value = " account_login_name:登录账号 ")
    private String accountLoginName;
    @ApiModelProperty(value = " mobile_phone:公司联系电话 ")
    private String mobilePhone;
    @ApiModelProperty(value = " tel_phone:联系人手机号 ")
    private String telPhone;
    @ApiModelProperty(value = " account_name:企业名称 ")
    private String accountName;
    @ApiModelProperty(value = " contact_person:联系人 ")
    private String contactPerson;
    @ApiModelProperty(value = " account_logo:logo ")
    private String accountLogo;
    @ApiModelProperty(value = " account_code:账号编码 ")
    private String accountCode;
    @ApiModelProperty(value = " password:登录密码 ")
    private String password;
    @ApiModelProperty(value = " role_id:角色id ")
    private Integer roleId;
    @ApiModelProperty(value = " status:状态 0禁用 1启用 ")
    private Integer status;
    @ApiModelProperty(value = " remark:备注 ")
    private String remark;

}
