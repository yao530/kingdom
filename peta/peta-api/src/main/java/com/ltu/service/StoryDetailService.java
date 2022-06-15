package com.ltu.service;

import com.ltu.domain.mp_entity.StoryDetailEntity;
import com.ltu.domain.mp_entity.StoryEntity;


/**
 * <p>
 * 故事详情 服务类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-01
 */

public interface StoryDetailService extends BaseTService<StoryDetailEntity> {

    void saveOrUpdate(StoryEntity storyEntity,String context);
    StoryDetailEntity getStoryDetail(Integer storyId);
}
