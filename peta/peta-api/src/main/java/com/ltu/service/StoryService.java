package com.ltu.service;

import com.ltu.domain.mp_entity.StoryEntity;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.model.request.story.StoryPageReq;
import com.ltu.model.request.story.StoryReq;
import com.ltu.model.response.base.CodeDataResp;

import java.util.List;


/**
 * <p>
 * 故事内容 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */

public interface StoryService extends BaseTService<StoryEntity> {

    CodeDataResp saveOrUpdate(StoryReq storyReq);
    CodeDataResp mint(AdminMintReq req);
    CodeDataResp getStoryList(StoryPageReq pageReq);

    CodeDataResp getStorysByCreator(StoryPageReq pageReq);

    List<StoryEntity> getStoryListByIp(BaseIdReq req);

    StoryEntity getStoryDetail(Integer storyId);

}
