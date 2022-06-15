package com.ltu.model.request.banner;

import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.PageReqData;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by vescky on 2020/8/21.
 */
@Data

public class BannerPageReq extends PageReqData {
    /**
     * 标题
     */
    @ApiModelProperty(value="按标题筛选时可选传")
    private String title;

    /**
     * 位置：1首页、2品牌中心、3新闻资讯
     */
    @ApiModelProperty(value="按位置筛选时可选传：1首页、2品牌中心、3新闻资讯")
    private Integer location;

    /**
     * 类型：1图文内容、2产品专场、3资讯、4外链
     */
    @ApiModelProperty(value="按类型筛选时可选传：1图文内容、2产品专场、3资讯、4外链")
    private Integer type;
}
