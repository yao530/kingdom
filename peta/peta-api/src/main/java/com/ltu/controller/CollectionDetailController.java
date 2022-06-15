package com.ltu.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.impl.BaseServiceImpl;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.ltu.domain.mp_entity.CollectionDetailEntity;
import com.ltu.mapper.CollectionDetailMapper;


/**
 * <p>
 * 故事篇章 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/collectionDetail")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "内容")
public class CollectionDetailController extends BaseServiceImpl<CollectionDetailMapper, CollectionDetailEntity>{


    
 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.GET)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }


}

