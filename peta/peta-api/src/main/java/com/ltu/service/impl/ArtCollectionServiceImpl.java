package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.NFTConstant;
import com.ltu.constant.RedisPubKeyConstant;
import com.ltu.constant.RoleType;
import com.ltu.domain.mp_entity.*;
import com.ltu.enums.RedisBusinessDict;
import com.ltu.mapper.ArtCollectionMapper;
import com.ltu.model.request.artCollections.ArtCollectionPageReq;
import com.ltu.model.request.artCollections.ArtCollectionReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;
import com.ltu.model.request.smartContract.AdminMintReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import com.ltu.util.redis.RedistUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 艺术藏品 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-21
 */
@Service
public class ArtCollectionServiceImpl extends BaseServiceImpl<ArtCollectionMapper, ArtCollectionEntity> implements ArtCollectionService {


    @Autowired
    StoryTalesService storyTalesService;

    @Autowired
    StoryService storyService;

    @Autowired
    VirtualCharacterService virtualCharacterService;

    @Autowired
    CollectionDetailService collectionDetailService;
    @Autowired
    AccountService accountService;
    @Autowired
    UserService userService;

    @Autowired
    ContractSquService contractSquService;

    @Autowired
    ArtCollectionMapper artCollectionMapper;

    @Autowired
    CollectionSoldSettingService collectionSoldSettingService;

    @Autowired
    RedistUtil redistUtil;


    public CodeDataResp saveOrUpdate(ArtCollectionReq artCollectionReq){

        UserDto userDto = ShiroUtil.getPrincipalUser();
        AccountEntity accountEntity = accountService.getById(userDto.getId());
        if (accountEntity==null)
            return CodeDataResp.valueOfFailed("请求数据非法");
        VirtualCharacterEntity creater = null;
        if (accountEntity.getRoleId()== RoleType.ARTISTOR){
            creater = new VirtualCharacterEntity();
            creater = virtualCharacterService.getCharacterByAccount(accountEntity);
        }

        if (StrUtils.isVaileNum(artCollectionReq.getId())){
            ArtCollectionEntity artCollectionEntity = super.getById(artCollectionReq.getId());
            if (artCollectionEntity==null)
                return CodeDataResp.valueOfFailed("数据非法");
            BeanMapper.copy(artCollectionReq,artCollectionEntity);
            artCollectionEntity.setUpdateTime(DateUtils.currentMillis());
            if (creater!=null){
                artCollectionEntity.setArtCreatorId(creater.getId());
            }
            buildStoryInfos(artCollectionEntity,artCollectionReq);
            super.saveOrUpdate(artCollectionEntity);
            collectionDetailService.saveOrUpdate(artCollectionEntity,artCollectionReq);

        }else {
            ArtCollectionEntity artCollectionEntity = new ArtCollectionEntity();
            BeanMapper.copy(artCollectionReq,artCollectionEntity);
            artCollectionEntity.setCreateTime(DateUtils.currentMillis());
            if (creater!=null){
                artCollectionEntity.setArtCreatorId(creater.getId());
            }
            buildStoryInfos(artCollectionEntity,artCollectionReq);
            super.saveOrUpdate(artCollectionEntity);

            collectionDetailService.saveOrUpdate(artCollectionEntity,artCollectionReq);

        }

        return CodeDataResp.valueOfSuccessEmptyData();
    }


    private void buildStoryInfos(ArtCollectionEntity artCollectionEntity,ArtCollectionReq artCollectionReq){

        if (StrUtils.isVaileNum(artCollectionReq.getStoryId())){
            StoryEntity storyEntity = storyService.getById(artCollectionReq.getStoryId());
            if (storyEntity!=null){
                artCollectionEntity.setWriterId(storyEntity.getWriterId());
                artCollectionEntity.setChapterNumber(storyEntity.getChapterNumber());
                artCollectionEntity.setStoryId(storyEntity.getId());
                artCollectionEntity.setStoryTitle(storyEntity.getStoryTitle());
            }

        }
       if (StrUtils.isVaileNum(artCollectionReq.getStoryTaleId())){
           StoryTalesEntity storyTalesEntity = storyTalesService.getById(artCollectionReq.getStoryTaleId());
           if (storyTalesEntity!=null){
               artCollectionEntity.setStoryTaleId(storyTalesEntity.getId());
               artCollectionEntity.setStoryTaleTitle(storyTalesEntity.getTaleTitle());
               artCollectionEntity.setMetaId(storyTalesEntity.getMetaId());
               artCollectionEntity.setMetaName(storyTalesEntity.getMetaName());
               VirtualCharacterEntity characterEntity = virtualCharacterService.getById(storyTalesEntity.getCharacterId());
               if (characterEntity!=null){
                   artCollectionEntity.setCharacterId(storyTalesEntity.getCharacterId());
                   artCollectionEntity.setCharacterName(characterEntity.getCharacterName());
                   artCollectionEntity.setCharacterAvatar(characterEntity.getCharacterAvatar());
               }
           }
       }

    }


    public CodeDataResp mint(AdminMintReq adminMintReq){
        if (StrUtils.isVaileNum(adminMintReq.getId())) {
            ArtCollectionEntity artCollectionEntity = super.getById(adminMintReq.getId());
            if (artCollectionEntity == null)
                return CodeDataResp.valueOfFailed("数据非法");
            BeanMapper.copy(adminMintReq,artCollectionEntity);
            artCollectionEntity.setUpdateTime(DateUtils.currentSecs());
            super.saveOrUpdate(artCollectionEntity);

            collectionSoldSettingService.updateCollectionStock(artCollectionEntity);
            //此处mint队列 需要用平台的账号
            if (adminMintReq.getMintStatus()==NFTConstant.MINT_STATUS_ING_MINT)
                contractSquService.mintArtCollection(artCollectionEntity,adminMintReq);
        }

        return CodeDataResp.valueOfSuccessEmptyData();
    }

    public void updateArtCollectionSoldStatus(Integer id,String updateStr){
        ArtCollectionEntity artCollection = super.getById(id);
        if (artCollection!=null){
            if (RedisBusinessDict.BusinessName.StartTimeTag.toString().equals(updateStr)){
                artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_SELLING);
            }
            if (RedisBusinessDict.BusinessName.EndTimeTag.toString().equals(updateStr)){
                artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_SOLD_OUT);//暂时先显示售馨
            }

            super.saveOrUpdate(artCollection);
        }

    }

    public ArtCollectionEntity getArtDetail(BaseIdReq baseIdReq){
        ArtCollectionEntity artCollectionEntity = redistUtil.getObject(RedisPubKeyConstant.getArtCollectionIdRedisKey(baseIdReq.getId()),ArtCollectionEntity.class);
        if (artCollectionEntity==null){
            artCollectionEntity = super.getById(baseIdReq.getId());
            if (artCollectionEntity.getSoldStatus()== NFTConstant.SOLD_STATUS_WAIT){
                artCollectionEntity.setDistanceTime(artCollectionEntity.getStartTime()-DateUtils.currentMillis());
            }
            CollectionDetailEntity collectionDetailEntity = collectionDetailService.getCollectionDetail(artCollectionEntity.getId());
            if (collectionDetailEntity!=null){
                artCollectionEntity.setContext(collectionDetailEntity.getContext());
                artCollectionEntity.setCollectionRights(collectionDetailEntity.getCollectionRights());
            }
//            CollectionSoldSettingEntity collectionSoldSetting = collectionSoldSettingService.getCollectionSetting(artCollectionEntity.getId());
//            if (collectionSoldSetting!=null)
//             artCollectionEntity.setLimitSoldNumber(collectionSoldSetting.getLimitSoldNumber());
            redistUtil.setValue(RedisPubKeyConstant.getArtCollectionIdRedisKey(artCollectionEntity.getId()),artCollectionEntity);

        }
        return artCollectionEntity;
    }


    public CodeDataResp getPage(ArtCollectionPageReq pageReqData){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        Page<ArtCollectionEntity> page = MpPageUtil.getCommonPage(pageReqData);
        QueryWrapper<ArtCollectionEntity> condition = new QueryWrapper<>();
        if (StrUtils.isVaileNum(pageReqData.getPetaverseType())){
            condition.eq("petaverse_type",pageReqData.getPetaverseType());
        }

        if (StrUtils.isVaileNum(pageReqData.getArtCreatorId())){
            condition.eq("art_creator_id",pageReqData.getArtCreatorId());
        }
        if (StrUtils.isVaileNum(pageReqData.getCharacterId())){
            condition.eq("character_id",pageReqData.getCharacterId());
        }
        if (StrUtils.isVaileNum(pageReqData.getCollectionType())){
            condition.eq("collection_type",pageReqData.getCollectionType());
        }
        if (!StrUtils.isTrimNull(pageReqData.getCollectionName())){
            condition.like("collection_name",pageReqData.getCollectionName());
        }
        Integer count = super.count(condition);
        Integer pageNumber = (pageReqData.getPageNumber()-1)*pageReqData.getPageSize();
        Integer pageSize = pageReqData.getPageNumber()*pageReqData.getPageSize()-1;
        List<ArtCollectionEntity> list = artCollectionMapper.pageSelect(pageReqData.getArtCreatorId(),
                pageReqData.getPetaverseType(),
                pageReqData.getStoryTaleId(),
                pageReqData.getStoryId(),
                pageReqData.getCharacterId(),
                pageReqData.getCollectionType(),
                0l,0l,pageNumber,pageSize);
        Page<ArtCollectionEntity> entityPage = new Page<>();
        entityPage.setRecords(list);
        entityPage.setTotal(count);
        entityPage.setSize(pageSize+1);
        entityPage.setCurrent(pageReqData.getPageNumber());

        return CodeDataResp.valueOfSuccess(entityPage);
    }


    public CodeDataResp getArtCollections(ArtCollectionPageReq pageReqData){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        pageReqData.setSortOrder("desc");
        pageReqData.setSortType("sort");
        Page<ArtCollectionEntity> page = MpPageUtil.getCommonPage(pageReqData);
        QueryWrapper<ArtCollectionEntity> condition = new QueryWrapper<>();
        condition.eq("publish_status",NFTConstant.PUBLISH_ONLINE_STATUS);
        condition.eq("status",NFTConstant.CHECK_STATUS_SUSSCESS);
        if (pageReqData.getCollectionType()==NFTConstant.NFT_TYPE_STORY_COPYRIGHT){
            condition.eq("collection_type",NFTConstant.NFT_TYPE_STORY_COPYRIGHT);
        }
        else {
            condition.ne("collection_type",NFTConstant.NFT_TYPE_STORY_COPYRIGHT);
        }


        if (StrUtils.isVaileNum(pageReqData.getSoldType())&&!pageReqData.getSoldType().equals(NFTConstant.SOLD_TYPE_FREE)){
            condition.eq("sold_type",pageReqData.getSoldType());
        }
        else {
            condition.ne("sold_type",NFTConstant.SOLD_TYPE_FREE);
        }
//        if (StrUtils.isVaileNum(pageReqData.getPetaverseType())){
//            condition.eq("petaverse_type",pageReqData.getPetaverseType());
//        }
//        else {
//            condition.eq("petaverse_type",2);
//        }
        if (StrUtils.isVaileNum(pageReqData.getArtCreatorId())){
            condition.eq("art_creator_id",pageReqData.getArtCreatorId());
        }
        if (StrUtils.isVaileNum(pageReqData.getCharacterId())){
            condition.eq("character_id",pageReqData.getCharacterId());
        }
        if (StrUtils.isVaileNum(pageReqData.getCollectionType())){
            condition.eq("collection_type",pageReqData.getCollectionType());
        }

        if (!StrUtils.isTrimNull(pageReqData.getCollectionName())){
            condition.like("collection_name",pageReqData.getCollectionName());
        }

        return CodeDataResp.valueOfSuccess(super.page(page,condition));
    }

    public CodeDataResp creatorGetPage(PageReqData pageReqData){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        AccountEntity accountEntity = accountService.getById(userDto.getId());

        pageReqData.setSortOrder("desc");
        pageReqData.setSortType("sort");
        Page<ArtCollectionEntity> page = MpPageUtil.getCommonPage(pageReqData);
        QueryWrapper<ArtCollectionEntity> condition = new QueryWrapper<>();
        if (accountEntity.getAccountType()== RoleType.WRITER)
            condition.eq("writer_id",accountEntity.getId());
        if (accountEntity.getAccountType()==RoleType.ARTISTOR)
            condition.eq("art_creator_id",accountEntity.getId());
        if (accountEntity.getAccountType()== RoleType.PET_MASTER){

            UserEntity userEntity = userService.getUserByPhone(accountEntity.getMobilePhone());
            if (userEntity!=null)
                condition.eq("character_id",accountEntity.getId());
        }

        return CodeDataResp.valueOfSuccess(super.page(page,condition));
    }

    public List<ArtCollectionEntity> getStoryArts(BaseIdReq baseIdReq){
        StoryEntity storyEntity = storyService.getById(baseIdReq.getId());
        if (storyEntity!=null){
            QueryWrapper<ArtCollectionEntity> condition = new QueryWrapper<>();
            condition.eq("collection_type",NFTConstant.NFT_TYPE_STORY_COPYRIGHT);
            condition.eq("story_id",storyEntity.getId());
            return super.list(condition);
        }
        else
            return new ArrayList<>();

    }

    public List<ArtCollectionEntity> getArtsByTaleId(Integer taleId){

        QueryWrapper<ArtCollectionEntity> condition = new QueryWrapper<>();
        condition.eq("story_tale_id",taleId);
        return super.list(condition);
    }
}
