package com.ltu.model.request.versioncentre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VersionCentreSaveReq {
    @ApiModelProperty(value = "主键，新建不传，修改必传")
    private String id;

    @ApiModelProperty(value = "名称")
    private String title;

    @ApiModelProperty(value = "默认1，0禁用，1启用")
    private Integer status;

    @ApiModelProperty(value = "类型：1-热更新、2-全量建议更新、3-全量强制更新")
    private Integer type;

    @ApiModelProperty(value = "平台：1-安卓、2-苹果")
    private Integer platform;

    @ApiModelProperty(value = "版本号，示例：1.1.1")
    private String versionNum;

    @ApiModelProperty(value = "更新介绍,600字符")
    private String updateIntroduce;

    @ApiModelProperty(value = "资源链接")
    private String sourceLink;

    @ApiModelProperty(value = "发布日期，默认当日，格式：yyyy-MM-dd")
    private String publishDate;

    @ApiModelProperty(value = "备注")
    private String remark;
}
