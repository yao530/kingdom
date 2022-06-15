package com.ltu.service.impl;

import com.aliyun.oss.common.utils.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.domain.mp_entity.StoryDetailEntity;
import com.ltu.domain.mp_entity.StoryEntity;
import com.ltu.mapper.StoryDetailMapper;
import com.ltu.service.StoryDetailService;
import com.ltu.util.datetime.DateUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 故事详情 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-01
 */
@Service
public class StoryDetailServiceImpl extends BaseServiceImpl<StoryDetailMapper, StoryDetailEntity> implements StoryDetailService {

   public void saveOrUpdate(StoryEntity storyEntity, String context){
       StoryDetailEntity storyDetail = getStoryDetail(storyEntity.getId());
       if (storyDetail==null)
           storyDetail = new StoryDetailEntity();
           storyDetail.setCreateTime(DateUtils.currentSecs());
           storyDetail.setContext(context);
           storyDetail.setStoryId(storyEntity.getId());

           this.saveOrUpdate(storyDetail);
   }

   public StoryDetailEntity getStoryDetail(Integer storyId){
       QueryWrapper<StoryDetailEntity> condition = new QueryWrapper<>();
       condition.eq("story_id",storyId);
       return this.getOne(condition);
   }


}
