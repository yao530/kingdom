package com.ltu.model.request.artCollections;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class FileResourceReq extends BaseIdReq{

    @ApiModelProperty(value = " file_thing_id:文件类型对应的主键 ")
    private Integer fileThingId;
    @ApiModelProperty(value = " file_type:文件类型 1、文章文件 2、藏品文件 3、其他 ")
    private Integer fileType;
    @ApiModelProperty(value = " content:内容备注 ")
    private String content;
    @ApiModelProperty(value = " file_url:文件存储地址 ")
    private String fileUrl;

}
