package com.ltu.model.request.artCollections;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class ArtCollectionReq extends BaseIdReq{

    @ApiModelProperty(value = " collection_name:藏品名称 ")
    private String collectionName;

    @ApiModelProperty(value = " collection_type:数字藏品类型 1avatar类 2权益藏品类 3收藏品 4非卖品 ")
    private Integer collectionType;

    @ApiModelProperty(value = " mintAmount:发行nft数量 ")
    private Integer mintAmount;

    @ApiModelProperty(value = " collection_styles:数字藏品风格类型 ")
    private String collectionStyles;

    @ApiModelProperty(value = " collection_cover:藏品封面图片 ")
    private String collectionCover;

    @ApiModelProperty(value = " original_file_url:原始文件存储地址 ")
    private String originalFileUrl;

    @ApiModelProperty(value = " story_tale_id:故事小说id ")
    private Integer storyTaleId;

    @ApiModelProperty(value = " story_id:故事id ")
    private Integer storyId;

    @ApiModelProperty(value = " main_display:是否主作品 0否 1是 ")
    private Integer mainDisplay;

    @ApiModelProperty(value = " art_creator_id:艺术创作者ID ")
    private Integer artCreatorId;

    @ApiModelProperty(value = " character_id:故事人物ID ")
    private Integer characterId;

    @ApiModelProperty(value = " sold_type:销售状态 1空投 2抢购 3预约购买 4定向投放 ")
    private Integer soldType;

    @ApiModelProperty(value = " price:价格 ")
    private Integer price;

    @ApiModelProperty(value = " start_time:创建时间 ")
    private Long startTime;
    @ApiModelProperty(value = " end_time:创建时间 ")
    private Long endTime;

    @ApiModelProperty(value = " collection_rights:藏品权益描述 ")
    private String collectionRights;
    @ApiModelProperty(value = " context:图文内容 ")
    private String context;

    @ApiModelProperty(value = " remark:备注 ")
    private String remark;

    private Integer sort;

    @ApiModelProperty(value = " sold_tips:销售提示 ")
    private String soldTips;

    @ApiModelProperty(value = " limit_sold_number:限制购买数量 默认1 ")
    private Integer limitSoldNumber=1;

    @ApiModelProperty(value = " sold_status:销售状态 0待售 1售卖中 2售馨")
    private Integer soldStatus;
    @ApiModelProperty(value = " publish_status:上架状态 0待上架 1上架中 2已下架")
    private Integer publishStatus=0;
    @ApiModelProperty(value = " status:状态 0待审核 1审核通过 2审核不通过 ")
    private Integer status;
}
