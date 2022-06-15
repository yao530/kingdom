package com.ltu.model.request.story;

import com.ltu.model.request.base.BaseIdReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/20.
 */
@Data
public class StoryTalesReq extends BaseIdReq{

    @ApiModelProperty(value = " tale_title:小说故事名称 ")
    private String taleTitle;
    @ApiModelProperty(value = " tale_cover:小说故事篇章封面图片 ")
    private String taleCover;
    @ApiModelProperty(value = " tale_sketch:小说故事简述 ")
    private String taleSketch;
    @ApiModelProperty(value = " tale_styles:小说故事风格标签 ")
    private String taleStyles;
    @ApiModelProperty(value = " story_type:故事类型：1主故事线 2 宠物故事线  3番外篇故事 ")
    private Integer storyType;
    @ApiModelProperty(value = " writer_id:故事作者ID ")
    private Integer writerId;
    @ApiModelProperty(value = " block_smart_contract_address:合约地址 ")
    private String blockSmartContractAddress;
    @ApiModelProperty(value = " character_id:故事主角人物ID ")
    private Integer characterId;
    @ApiModelProperty(value = " status:状态 0未上线 1上线中 2已完结 ")
    private Integer status;
    @ApiModelProperty(value = " remark:备注 ")
    private String remark;
    @ApiModelProperty(value = " meta_id:宇宙ID ")
    private Integer metaId;



}
