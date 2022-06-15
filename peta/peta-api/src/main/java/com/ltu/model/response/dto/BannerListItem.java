package com.ltu.model.response.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BannerListItem {
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="位置：1首页、2品牌中心、3新闻资讯")
    private Integer location;

    @ApiModelProperty(value="类型：1图文内容、2产品专场、3资讯、4外链")
    private Integer type;

    @ApiModelProperty(value="图片链接")
    private String image;

    @ApiModelProperty(value="内容", notes = "与type对应，图文内容是富文本，产品和资讯是id，外链是链接")
    private String content;

    @ApiModelProperty(value="备注信息")
    private String remark;
}
