package com.ltu.model.response.dto;

import com.ltu.domain.mp_entity.UserCollectionsEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class UserCollectionDetailDto {

    @ApiModelProperty(value = " collection_id:故事小说id ")
    private Integer collectionId;
    @ApiModelProperty(value = " collection_name:藏品名称 ")
    private String collectionName;
    @ApiModelProperty(value = " collection_type:数字藏品类型 2、故事背景类 3、分身藏品类 4、故事叙事类 5、道具类 6权益类 ")
    private Integer collectionType;
    @ApiModelProperty(value = " collection_cover:藏品封面图片 ")
    private String collectionCover;

    private Integer mainDisplay=0;

    private String collectionTypeName;

    @ApiModelProperty(value = " price:价格 ")
    private Integer price;

    private String blockSmartContractAddress;
    @ApiModelProperty(value = " nft_id:合约对应的NFT-ID ")
    private Integer nftId=0;

    @ApiModelProperty(value = " 用户藏品状态 0待发行 1已持有 2未持有 ")
    private Integer collectionStatus;

    @ApiModelProperty(value = " mint_amount:发行nft数量 ")
    private Integer mintAmount=0;
    @ApiModelProperty(value = " 用户持有数量 ")
    private Integer userMintAmount = 1;

    private Long createTime;
}
