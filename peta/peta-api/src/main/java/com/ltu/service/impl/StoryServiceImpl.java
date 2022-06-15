package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.NFTConstant;
import com.ltu.constant.RoleType;
import com.ltu.domain.mp_entity.*;
import com.ltu.mapper.StoryMapper;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.model.request.story.StoryPageReq;
import com.ltu.model.request.story.StoryReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 故事内容 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@Service
public class StoryServiceImpl extends BaseServiceImpl<StoryMapper, StoryEntity> implements StoryService {


    @Autowired
    StoryTalesService storyTalesService;

    @Autowired
    VirtualCharacterService virtualCharacterService;

    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;
    @Autowired
    StoryDetailService storyDetailService;

    @Autowired
    ContractSquService contractSquService;
    @Autowired
    StoryMapper storyMapper;


    public CodeDataResp saveOrUpdate(StoryReq storyReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        AccountEntity accountEntity = accountService.getById(userDto.getId());
        if (accountEntity==null)
            return CodeDataResp.valueOfFailed("请求数据非法");
        VirtualCharacterEntity creater = null;
        if (accountEntity.getRoleId()== RoleType.WRITER){
            creater = new VirtualCharacterEntity();
            creater = virtualCharacterService.getCharacterByAccount(accountEntity);
        }
        StoryTalesEntity storyTalesEntity = storyTalesService.getById(storyReq.getStoryTaleId());
        Integer oldStoryTaleId = 0;
        if (StrUtils.isVaileNum(storyReq.getId())){
            StoryEntity storyEntity = super.getById(storyReq.getId());
            if (storyEntity==null)
                return CodeDataResp.valueOfFailed("数据非法");
            oldStoryTaleId = storyEntity.getStoryTaleId();
            BeanMapper.copy(storyReq,storyEntity);
            storyEntity.setUpdateTime(DateUtils.currentMillis());
            addStoryToStoryTale(storyReq,oldStoryTaleId,storyEntity,true);
            storyEntity.setStoryType(storyTalesEntity.getStoryType());
            if (creater!=null){
                storyEntity.setWriterId(creater.getId());
                storyEntity.setWriterAvatar(creater.getCharacterAvatar());
                storyEntity.setWriterName(creater.getCharacterName());
            }
            super.saveOrUpdate(storyEntity);
            storyDetailService.saveOrUpdate(storyEntity,storyReq.getContext());
        }
        else {
            StoryEntity storyEntity = new StoryEntity();
            BeanMapper.copy(storyReq,storyEntity);
            storyEntity.setCreateTime(DateUtils.currentMillis());
            storyEntity.setCollectionType(NFTConstant.NFT_TYPE_STORY_COPYRIGHT);
            addStoryToStoryTale(storyReq,oldStoryTaleId,storyEntity,false);
            storyEntity.setStoryType(storyTalesEntity.getStoryType());
            if (creater!=null){
                storyEntity.setWriterId(creater.getId());
                storyEntity.setWriterAvatar(creater.getCharacterAvatar());
                storyEntity.setWriterName(creater.getCharacterName());
            }

            super.saveOrUpdate(storyEntity);
            storyDetailService.saveOrUpdate(storyEntity,storyReq.getContext());
        }
        return CodeDataResp.valueOfSuccessEmptyData();
    }

    public CodeDataResp mint(AdminMintReq req){
        if (StrUtils.isVaileNum(req.getId())) {
            StoryEntity storyEntity = super.getById(req.getId());
            if (storyEntity == null)
                return CodeDataResp.valueOfFailed("数据非法");
            StoryTalesEntity storyTalesEntity = storyTalesService.getById(storyEntity.getStoryTaleId());
//            if (storyTalesEntity!=null){
//                req.setBlockSmartContractAddress(storyTalesEntity.getBlockSmartContractAddress());
//                if (!StrUtils.isTrimNull(req.getBlockSmartContractAddress())&&!StrUtils.isVaileNum(storyEntity.getMintStatus()))
//                {
//                    UserEntity userEntity = userService.getById(storyEntity.getWriterId());
//                    contractSquService.mintStory(storyTalesEntity,userEntity,storyEntity);
//                }
//            }

            BeanMapper.copy(req,storyEntity);
            super.saveOrUpdate(storyEntity);
        }

        return CodeDataResp.valueOfSuccessEmptyData();
    }


    private void addStoryToStoryTale(StoryReq storyReq,Integer oldStoryTaleId,StoryEntity storyEntity,Boolean isEdit){
        StoryTalesEntity storyTalesEntity =null;
        StoryTalesEntity oldStoryTalesEntity =null;
        if (!StrUtils.isVaileNum(oldStoryTaleId)){
            storyTalesEntity = storyTalesService.getById(storyReq.getStoryTaleId());
        }else {
            storyTalesEntity = storyTalesService.getById(storyReq.getStoryTaleId());
            oldStoryTalesEntity = storyTalesService.getById(oldStoryTaleId);
        }
        if (oldStoryTalesEntity!=null){
            oldStoryTalesEntity.setChapterNumber(storyTalesEntity.getChapterNumber()-1);
            storyTalesService.saveOrUpdate(storyTalesEntity);
        }
        if (storyTalesEntity!=null){
            storyEntity.setStoryTaleId(storyTalesEntity.getId());
            storyEntity.setStoryTaleTitle(storyTalesEntity.getTaleTitle());
            storyEntity.setCharacterId(storyTalesEntity.getCharacterId());
            storyEntity.setWriterId(storyTalesEntity.getWriterId());
            storyEntity.setWriterName(storyTalesEntity.getWriterName());
            storyEntity.setWriterAvatar(storyTalesEntity.getWriterAvatar());


            if (!isEdit){
                storyTalesEntity.setChapterNumber(storyTalesEntity.getChapterNumber()+1);
                storyEntity.setChapterNumber(storyTalesEntity.getChapterNumber());
            }

            storyTalesService.saveOrUpdate(storyTalesEntity);

        }


    }


    public List<StoryEntity> getStoryListByIp(BaseIdReq req){
        List<StoryEntity> storyEntityList = new ArrayList<>();
//        StoryEntity storyEntity = super.getById(req.getId());
//        if (storyEntity==null)
//            return storyEntityList;
        QueryWrapper<StoryEntity> condition = new QueryWrapper<>();
        condition.eq("status",1);
//        condition.eq("publish_status",1);
//        condition.eq("mint_status",1);
//        if (StrUtils.isVaileNum(req.getId()))
//            condition.eq("story_tale_id",storyEntity.getStoryTaleId());

        return super.list(condition);
    }

    public CodeDataResp getStoryList(StoryPageReq pageReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        Page<StoryEntity> page = MpPageUtil.getCommonPage(pageReq);
        pageReq.setSortOrder("desc");
        pageReq.setSortType("create_time");
        QueryWrapper<StoryEntity> condition = new QueryWrapper<>();

        if (StrUtils.isVaileNum(pageReq.getStoryType()))
            condition.eq("story_type",pageReq.getStoryType());
        if (StrUtils.isVaileNum(pageReq.getStoryTaleId())){
            condition.eq("story_tale_id",pageReq.getStoryTaleId());
        }
        if (StrUtils.isVaileNum(pageReq.getArtCreatorId())){
            condition.eq("art_creator_id",pageReq.getArtCreatorId());
        }
        if (StrUtils.isVaileNum(pageReq.getCharacterId())){
            condition.eq("character_id",pageReq.getCharacterId());
        }
        if (!StrUtils.isTrimNull(pageReq.getStoryTitle()))
            condition.like("story_title",pageReq.getStoryTitle());

        Integer count = super.count(condition);
        Integer pageNumber = (pageReq.getPageNumber()-1)*pageReq.getPageSize();
        Integer pageSize = pageReq.getPageNumber()*pageReq.getPageSize()-1;
        List<StoryEntity> list = storyMapper.pageSelect(pageReq.getArtCreatorId(),
                pageReq.getStoryTaleId(),pageReq.getCharacterId(),pageReq.getStoryType(),0l,0l,pageNumber,pageSize);
        Page<StoryEntity> entityPage = new Page<>();
        entityPage.setRecords(list);
        entityPage.setTotal(count);
        entityPage.setSize(pageSize+1);
        entityPage.setCurrent(pageReq.getPageNumber());
        return CodeDataResp.valueOfSuccess(entityPage);
    }

    public CodeDataResp getStorysByCreator(StoryPageReq pageReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        AccountEntity accountEntity = accountService.getById(userDto.getId());

        Page<StoryEntity> page = MpPageUtil.getCommonPage(pageReq);
            pageReq.setSortOrder("desc");
            pageReq.setSortType("create_time");
        QueryWrapper<StoryEntity> condition = new QueryWrapper<>();

         condition.eq("status",StrUtils.isVaileNum(pageReq.getStatus())?pageReq.getStatus():1);

         if (accountEntity.getAccountType()== RoleType.WRITER)
             condition.eq("writer_id",accountEntity.getId());
         if (accountEntity.getAccountType()==RoleType.ARTISTOR)
             condition.eq("art_creator_id",accountEntity.getId());
        if (accountEntity.getAccountType()== RoleType.PET_MASTER){

            UserEntity userEntity = userService.getUserByPhone(accountEntity.getMobilePhone());
            if (userEntity!=null)
                condition.eq("character_id",accountEntity.getId());
        }


        if (StrUtils.isVaileNum(pageReq.getStoryType()))
            condition.eq("story_type",pageReq.getStoryType());


        if (StrUtils.isVaileNum(pageReq.getArtCreatorId())){
            condition.eq("art_creator_id",pageReq.getArtCreatorId());
        }
        if (StrUtils.isVaileNum(pageReq.getCharacterId())){
            condition.eq("character_id",pageReq.getCharacterId());
        }
        if (!StrUtils.isTrimNull(pageReq.getStoryTitle()))
            condition.like("story_title",pageReq.getStoryTitle());
        return CodeDataResp.valueOfSuccess(super.page(page,condition));
    }


    public StoryEntity getStoryDetail(Integer storyId){
        StoryEntity storyEntity = super.getById(storyId);
        if (storyEntity!=null){
            StoryDetailEntity detail = storyDetailService.getStoryDetail(storyId);
            if (detail!=null)
                storyEntity.setContext(detail.getContext());
        }

        return storyEntity;
    }
}
