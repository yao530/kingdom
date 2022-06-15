package com.ltu.model.request.story;

import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class StoryTalesPageReq extends PageReqData {

    @ApiModelProperty(value = " tale_title:小说故事名称 ")
    private String taleTitle;
    @ApiModelProperty(value = " story_type:故事类型：1主故事线 2 宠物故事线  3番外篇故事 ")
    private Integer storyType;
    @ApiModelProperty(value = " writer_id:故事作者ID ")
    private Integer writerId;

    @ApiModelProperty(value = " character_id:故事主角人物ID ")
    private Integer characterId;

    @ApiModelProperty(value = " meta_id:宇宙ID ")
    private Integer metaId;



}
