package com.ltu.model.request.user;

import com.ltu.model.request.base.BaseFilterPageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserCollectionCatePageReq extends BaseFilterPageReq {
    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="关键字")
    private String keyword;

    @ApiModelProperty(value="用户id，宣传")
    private String userId;

    @ApiModelProperty(value="产品id，用来判断产品是否被收藏")
    private String productId;

    @ApiModelProperty(value="排序字段，默认为update_time", notes = "多字段用英文逗号隔开，多字段排序必须对应多个升降序字段")
    private String sortType = "update_time";
}
