package com.ltu.model.request.story;

import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2022/8/20.
 */
@Data
public class StoryPageReq extends PageReqData{

    @ApiModelProperty(value = " story_title:故事名称 ")
    private String storyTitle;

    @ApiModelProperty(value = " story_tale_id:故事IP id ")
    private Integer storyTaleId;

    @ApiModelProperty(value = " writer_id:故事作者ID ")
    private Integer writerId;

    @ApiModelProperty(value = " art_creator_id:艺术创作者ID ")
    private Integer artCreatorId;

    @ApiModelProperty(value = " story_type:故事类型：1主故事线 2 宠物故事线  ")
    private Integer storyType;

    @ApiModelProperty(value = " character_id:故事人物ID ")
    private Integer characterId;

    private Integer status;

}
