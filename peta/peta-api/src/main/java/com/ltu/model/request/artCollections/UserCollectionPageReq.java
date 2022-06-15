package com.ltu.model.request.artCollections;

import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class UserCollectionPageReq extends PageReqData{


    @ApiModelProperty(value = " collection_type:数字藏品类型 1avatar类 2权益藏品类 3收藏品 4非卖品 ")
    private Integer collectionType=1;

    @ApiModelProperty(value = " petaverse_type:所属元宇宙阵营类型 1王国阵营 2宠物阵营 3中立阵营 ")
    private Integer petaverseType=2;

    @ApiModelProperty(value = " story_id:故事id ")
    private Integer storyId;

    @ApiModelProperty(value = " character_id:故事人物ID ")
    private Integer characterId;

    @ApiModelProperty(value = " art_creator_id:艺术创作者ID ")
    private Integer artCreatorId;

}
