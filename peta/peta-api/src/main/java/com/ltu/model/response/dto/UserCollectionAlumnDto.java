package com.ltu.model.response.dto;

import com.ltu.domain.mp_entity.UserCollectionsEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserCollectionAlumnDto {


    @ApiModelProperty(value="类型")
    private Integer collectionType;

    private String collectionTypeName;

    private Integer mainDisplay=0;

    @ApiModelProperty(value = " story_id:故事id ")
    private Integer storyId;
    @ApiModelProperty(value = " story_title:故事标题 ")
    private String storyTitle;
    @ApiModelProperty(value = " story_tale_title:故事IP名称 ")
    private String storyTaleTitle;
    @ApiModelProperty(value = " chapter_number:故事话数 ")
    private Integer chapterNumber;

    @ApiModelProperty(value="用户的藏品")
    List<UserCollectionDetailDto> detailDtoList;

}
