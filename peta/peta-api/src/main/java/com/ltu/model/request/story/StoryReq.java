package com.ltu.model.request.story;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class StoryReq extends BaseIdReq{

    @ApiModelProperty(value = " story_title:故事名称 ")
    private String storyTitle;
    @ApiModelProperty(value = " story_cover:故事封面图片 ")
    private String storyCover;
    @ApiModelProperty(value = " story_sketch:故事简述 ")
    private String storySketch;
    @ApiModelProperty(value = " story_section:故事章节数量 ")
    private Integer storySection;
    @ApiModelProperty(value = " chapter_id:故事篇章id ")
    private Integer storyTaleId;
    @ApiModelProperty(value = " original_file_url:原始文件存储地址 ")
    private String originalFileUrl;

    @ApiModelProperty(value = " writer_id:故事作者ID ")
    private Integer writerId;

    @ApiModelProperty(value = " art_creator_id:艺术创作者ID ")
    private Integer artCreatorId;

    @ApiModelProperty(value = " story_type:故事类型：1主故事线 2 宠物故事线  ")
    private Integer storyType;

    @ApiModelProperty(value = " context:图文内容 ")
    private String context;
}
