package com.ltu.controller;

import java.util.List;

import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.model.request.story.StoryPageReq;
import com.ltu.model.request.story.StoryReq;
import com.ltu.service.StoryService;
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

import com.ltu.domain.mp_entity.StoryEntity;
import com.ltu.mapper.StoryMapper;


/**
 * <p>
 * 故事内容 前端控制器
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@RestController
@RequestMapping("/story")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags= "故事内容")
public class StoryController extends BaseServiceImpl<StoryMapper, StoryEntity>{


    @Autowired
    StoryService storyService;

    @ApiOperation(value="获取分页列表")
    @RequestMapping(value="/getMePage", method= RequestMethod.POST)
    public CodeDataResp<Page<StoryEntity>> getMePage(@RequestBody   StoryPageReq req  ) {
    	return storyService.getStoryList(req);
    }

    @ApiOperation(value="创作者获取数据")
    @RequestMapping(value="/getPage", method= RequestMethod.POST)
    public CodeDataResp<Page<StoryEntity>> getPage(@RequestBody  StoryPageReq req ) {
        return storyService.getStorysByCreator(req);
    }
    
    @ApiOperation(value="获取故事线")
    @RequestMapping(value="/getList", method= RequestMethod.POST)
    public CodeDataResp<List<StoryEntity>> getList(@RequestBody BaseIdReq pageReq ) {
        return CodeDataResp.valueOfSuccess(storyService.getStoryListByIp(pageReq));
    }

    @ApiOperation(value="新增/保存")
    @RequestMapping(value="/saveOrUpdate", method= RequestMethod.POST)
    public CodeDataResp saveOrUpdate(@RequestBody StoryReq req) {
        return storyService.saveOrUpdate(req);
    }

    @ApiOperation(value="铸造")
    @RequestMapping(value="/mint", method= RequestMethod.POST)
    public CodeDataResp mint(@RequestBody AdminMintReq req) {
        return storyService.mint(req);
    }
    
 	@ApiOperation(value="获取详情")
    @RequestMapping(value="/getDetail", method= RequestMethod.POST)
    public CodeDataResp getDetail(@RequestBody BaseIdReq baseIdReq) {
        return CodeDataResp.valueOfSuccess(storyService.getStoryDetail(baseIdReq.getId()));
    }

    @ApiOperation(value="删除")
    @RequestMapping(value="/remove", method= RequestMethod.GET)
    public CodeDataResp remove(@RequestBody BaseIdReq baseIdReq) {
        return super.remove(baseIdReq);
    }
    

}

