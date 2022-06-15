package com.ltu.model.request.story;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class StoryChapterReq extends BaseIdReq{

    @ApiModelProperty(value = " chapter_title:故事篇章名称 ")
    private String chapterTitle;
    @ApiModelProperty(value = " chapter_cover:故事篇章封面图片 ")
    private String chapterCover;

    @ApiModelProperty(value = " story_tale_id:故事小说id ")
    private Integer storyTaleId;

    @ApiModelProperty(value = " remark:备注 ")
    private String remark;
}
