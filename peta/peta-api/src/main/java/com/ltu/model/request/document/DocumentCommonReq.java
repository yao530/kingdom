package com.ltu.model.request.document;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class DocumentCommonReq {

    /**
     * 主键
     */
    @ApiModelProperty(value="主键，修改和删除时必传")
    private String id;
    /**
     * 标题
     */
    @ApiModelProperty(value="标题，选传")
    private String title;
    /**
     * 内容存储在MongoDB，用key读取
     */
    @ApiModelProperty(value="内容，选传")
    private String content;
    /**
     * 备注
     */
    @ApiModelProperty(value="备注，选传")
    private String remark;
}
