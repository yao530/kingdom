package com.ltu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.domain.mp_entity.StoryTalesEntity;
import com.ltu.model.request.base.BaseFilterPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.model.request.story.StoryChapterReq;
import com.ltu.model.request.story.StoryTaleWriterReq;
import com.ltu.model.request.story.StoryTalesPageReq;
import com.ltu.model.request.story.StoryTalesReq;
import com.ltu.model.response.base.CodeDataResp;

import java.util.List;


/**
 * <p>
 * 故事小说 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */

public interface StoryTalesService extends BaseTService<StoryTalesEntity> {

    CodeDataResp saveOrUpdate(StoryTalesReq storyChapterReq);

    CodeDataResp<Page<StoryTalesEntity>> getPage(StoryTalesPageReq req);

    List<StoryTalesEntity> getList();
}
