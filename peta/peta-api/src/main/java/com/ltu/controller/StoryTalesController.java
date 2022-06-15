package com.ltu.controller;

import java.util.List;

import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.model.request.story.StoryTaleWriterReq;
import com.ltu.model.request.story.StoryTalesPageReq;
import com.ltu.model.request.story.StoryTalesReq;
import com.ltu.service.StoryTalesService;
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

import com.ltu.domain.mp_entity.StoryTalesEntity;
import com.ltu.mapper.StoryTalesMapper;


/**
 * <p>
 * 故事小说 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/storyTales")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "故事小说")
public class StoryTalesController extends BaseServiceImpl<StoryTalesMapper, StoryTalesEntity>{


    @Autowired
    StoryTalesService storyTalesService;

    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<StoryTalesEntity>> getMePage(@RequestBody  BaseFilterPageReq<StoryTalesEntity> req ) {
    	return super.getMePage(req);
    }

    @ApiOperation(value="获取列表")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<StoryTalesEntity>> getList( ) {
        return CodeDataResp.valueOfSuccess(storyTalesService.getList());
    }

    @ApiOperation(value="前端获取列表")
    @RequestMapping(value="/getPage", method= RequestMethod.POST)
    public CodeDataResp<Page<StoryTalesEntity>> getPage(@RequestBody StoryTalesPageReq req ) {
        return storyTalesService.getPage(req);
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp saveOrUpdate(@RequestBody StoryTalesReq req) {
        return storyTalesService.saveOrUpdate(req);
    }


 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.POST)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return super.getDetail(baseIdReq);
    }

    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.GET)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }
    


}

