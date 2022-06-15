package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.RoleType;
import com.ltu.domain.mp_entity.*;
import com.ltu.mapper.StoryTalesMapper;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.model.request.story.StoryTaleWriterReq;
import com.ltu.model.request.story.StoryTalesPageReq;
import com.ltu.model.request.story.StoryTalesReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.MetaService;
import com.ltu.service.StoryTalesService;
import com.ltu.service.UserService;
import com.ltu.service.VirtualCharacterService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 故事小说 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@Service
public class StoryTalesServiceImpl extends BaseServiceImpl<StoryTalesMapper, StoryTalesEntity> implements StoryTalesService {


    @Autowired
   UserService userService;

    @Autowired
    VirtualCharacterService virtualCharacterService;

   @Autowired
   ContractSquServiceImpl contractSquService;
   @Autowired
   MetaService metaService;

   public CodeDataResp saveOrUpdate(StoryTalesReq storyTalesReq){

       VirtualCharacterEntity virtualCharacterEntity = null;
       VirtualCharacterEntity writer = null;
       UserEntity userEntity = null;
       MetaEntity metaEntity = metaService.getMeta(storyTalesReq.getMetaId());
       if (StrUtils.isVaileNum(storyTalesReq.getCharacterId()))
           virtualCharacterEntity = virtualCharacterService.getById(storyTalesReq.getCharacterId());
       if (virtualCharacterEntity!=null)
           userEntity = userService.getById(virtualCharacterEntity.getUserId());

       if (StrUtils.isVaileNum(storyTalesReq.getId())){
           StoryTalesEntity storyTalesEntity = super.getById(storyTalesReq.getId());
           if (storyTalesEntity==null)
               return CodeDataResp.valueOfFailed("数据非法");
           BeanMapper.copy(storyTalesReq,storyTalesEntity);
           storyTalesEntity.setUpdateTime(DateUtils.currentMillis());
           storyTalesEntity.setWriterId(virtualCharacterEntity.getId());
           storyTalesEntity.setWriterName(virtualCharacterEntity.getCharacterName());
           storyTalesEntity.setWriterAvatar(virtualCharacterEntity.getCharacterAvatar());
           storyTalesEntity.setMetaId(metaEntity.getId());
           storyTalesEntity.setMetaName(metaEntity.getMetaName());
           super.saveOrUpdate(storyTalesEntity);

       }
       else {
           StoryTalesEntity storyTalesEntity = new StoryTalesEntity();
           BeanMapper.copy(storyTalesReq,storyTalesEntity);
           storyTalesEntity.setCreateTime(DateUtils.currentMillis());
           storyTalesEntity.setWriterId(virtualCharacterEntity.getId());
           storyTalesEntity.setWriterName(virtualCharacterEntity.getCharacterName());
           storyTalesEntity.setWriterAvatar(virtualCharacterEntity.getCharacterAvatar());
           storyTalesEntity.setMetaId(metaEntity.getId());
           storyTalesEntity.setMetaName(metaEntity.getMetaName());
           super.saveOrUpdate(storyTalesEntity);
       }

       return CodeDataResp.valueOfSuccessEmptyData();
   }

    public CodeDataResp<Page<StoryTalesEntity>> getPage(StoryTalesPageReq pageReqData){
        Page<StoryTalesEntity> page = MpPageUtil.getCommonPage(pageReqData);
        QueryWrapper<StoryTalesEntity> condition = new QueryWrapper<>();
        condition.ge("status",1);//上线的
        if (StrUtils.isVaileNum(pageReqData.getCharacterId())){
            condition.eq("character_id",pageReqData.getCharacterId());
        }
        if (!StrUtils.isTrimNull(pageReqData.getTaleTitle())){
            condition.like("tale_title",pageReqData.getTaleTitle());
        }
        return CodeDataResp.valueOfSuccess(this.page(page,condition));
    }

    public List<StoryTalesEntity> getList(){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        //按用户权限查看
        QueryWrapper<StoryTalesEntity> condition = new QueryWrapper<>();
            //condition.le("writer_id",userDto.getId);

        return super.list(condition);
    }

}
