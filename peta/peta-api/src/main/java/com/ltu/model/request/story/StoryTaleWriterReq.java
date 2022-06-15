package com.ltu.model.request.story;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class StoryTaleWriterReq extends BaseIdReq{


    @ApiModelProperty(value = " writer_name:故事作者名称 ")
    private Integer writerId;
    @ApiModelProperty(value = " character_id:故事主角人物ID ")
    private Integer characterId;
}
