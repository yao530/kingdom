package com.ltu.model.request.artCollections;

import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class ArtCollectionPageReq extends PageReqData{

    @ApiModelProperty(value = " collection_name:藏品名称 ")
    private String collectionName;

    @ApiModelProperty(value = " collection_type:数字藏品类型 1avatar类 2权益藏品类 3收藏品 4非卖品 ")
    private Integer collectionType;

    @ApiModelProperty(value = " sold_type ")
    private Integer soldType;

    @ApiModelProperty(value = " petaverse_type:所属元宇宙阵营类型 1王国阵营 2宠物阵营 3中立阵营 ")
    private Integer petaverseType=0;

    @ApiModelProperty(value = " story_id:故事id ")
    private Integer storyId=0;

    @ApiModelProperty(value = " story_id:故事id ")
    private Integer storyTaleId=0;


    @ApiModelProperty(value = " character_id:故事人物ID ")
    private Integer characterId=0;

    @ApiModelProperty(value = " art_creator_id:艺术创作者ID ")
    private Integer artCreatorId=0;

}
