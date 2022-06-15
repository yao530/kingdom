package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.NFTConstant;
import com.ltu.constant.RedisPubKeyConstant;
import com.ltu.domain.mp_entity.*;
import com.ltu.enums.EnumUtils;
import com.ltu.enums.NftTypeDict;
import com.ltu.enums.OrderDict;
import com.ltu.mapper.UserCollectionsMapper;
import com.ltu.mapper.UserJoinDropMapper;
import com.ltu.model.request.artCollections.UserCollectionPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smartContract.TransferReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.dto.UserCollectionAlumnDto;
import com.ltu.model.response.dto.UserCollectionDetailDto;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.common.UUIDGenerator;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 支付记录 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-03-26
 */
@Service
public class UserCollectionsServiceImpl extends BaseServiceImpl<UserCollectionsMapper, UserCollectionsEntity> implements UserCollectionsService {


    @Autowired
    UserService userService;
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StoryTalesService storyTalesService;
    @Autowired
    StoryService storyService;
    @Autowired
    UserJoinDropService userJoinDropService;
    @Autowired
    ArtCollectionService artCollectionService;
    @Autowired
    CollectionSoldSettingService collectionSoldSettingService;
    @Autowired
    TransferCollectionRecordService transferCollectionRecordService;
    @Autowired
    OrderService orderService;


    /**
     * @desc 购买成功 铸造用户的藏品
     * @param
     * @return
     **/
    public UserCollectionsEntity createUserCollection(UserEntity userEntity,OrderEntity order){
        ArtCollectionEntity artCollection = artCollectionService.getById(order.getCollectionId());
        UserCollectionsEntity userCollectionsEntity = new UserCollectionsEntity();
        buildBaseUserCollection(artCollection,userCollectionsEntity);
        userCollectionsEntity.setOrderId(order.getId());
        userCollectionsEntity.setUserId(order.getUserId());
        userCollectionsEntity.setUserName(order.getUserName());
        userCollectionsEntity.setUserAvatar(order.getUserAvatar());
        userCollectionsEntity.setCollectionOpenType(NFTConstant.OEPN_TYPE_NORMAL_GET);
        super.save(userCollectionsEntity);
        transferCollectionRecordService.mintUserCollection(userEntity,userCollectionsEntity,NFTConstant.TRANSFER_TYPE_P2C);
        return userCollectionsEntity;
    }

    /**
     * @desc 开启盲盒 铸造用户藏品
     * @param
     * @return
     **/
    public UserCollectionsEntity createUserCollectionByOpenBlindBox(UserEntity userEntity,CollectionBlindBoxEntity collectionBlindBox){
        ArtCollectionEntity artCollection = artCollectionService.getById(collectionBlindBox.getCollectionId());
        UserCollectionsEntity userCollectionsEntity = new UserCollectionsEntity();
        buildBaseUserCollection(artCollection,userCollectionsEntity);
        userCollectionsEntity.setOrderId(collectionBlindBox.getOrderId());
        userCollectionsEntity.setUserId(collectionBlindBox.getUserId());
        userCollectionsEntity.setCollectionOpenType(NFTConstant.OEPN_TYPE_BLIND_BOX);
        userCollectionsEntity.setBlindBoxId(collectionBlindBox.getId());
        super.save(userCollectionsEntity);

        transferCollectionRecordService.mintUserCollection(userEntity,userCollectionsEntity,NFTConstant.TRANSFER_TYPE_P2C);
        return userCollectionsEntity;
    }


    /**
     * @desc 交易铸造成功记录更新
     * @param
     * @return
     **/
    public void updataTransferRecord(TransferCollectionRecordEntity transferCollectionRecordEntity){

            if (transferCollectionRecordEntity.getTransferType()==NFTConstant.TRANSFER_TYPE_P2C){
                //首次购买 平台---用户的交易,更新订单、用户藏品记录
                UserCollectionsEntity userCollectionsEntity = super.getById(transferCollectionRecordEntity.getToUserCollectionId());
                if (userCollectionsEntity!=null){
                    userCollectionsEntity.setMintStatus(NFTConstant.TRANSFER_FINSH_STATUS);
                    userCollectionsEntity.setStatus(1);
                    userCollectionsEntity.setNftId(transferCollectionRecordEntity.getNftId());
                    userCollectionsEntity.setNftIdAmount(transferCollectionRecordEntity.getNftIdAmount());
                    userCollectionsEntity.setTransferId(transferCollectionRecordEntity.getId());
                    userCollectionsEntity.setUpdateTime(DateUtils.currentMillis());
                    super.saveOrUpdate(userCollectionsEntity);
                    //更新订单
                    orderService.updateOrderByMintSuccess(userCollectionsEntity);


                }
            }
            if (transferCollectionRecordEntity.getTransferType()==NFTConstant.TRANSFER_TYPE_C2C){
                UserCollectionsEntity fromUserCollectionsEntity = super.getById(transferCollectionRecordEntity.getFromUserCollectionId());
                if (fromUserCollectionsEntity!=null){
                    fromUserCollectionsEntity.setStatus(2);//已转赠
                    fromUserCollectionsEntity.setUpdateTime(DateUtils.currentMillis());
                    fromUserCollectionsEntity.setTransferId(transferCollectionRecordEntity.getId());
                    super.saveOrUpdate(fromUserCollectionsEntity);
                }
                //创建被转赠用户的藏品记录
                ArtCollectionEntity artCollection = artCollectionService.getById(fromUserCollectionsEntity.getCollectionId());
                UserEntity toUser = userService.getById(transferCollectionRecordEntity.getToUserId());
                UserCollectionsEntity userCollectionsEntity = new UserCollectionsEntity();
                buildBaseUserCollection(artCollection,userCollectionsEntity);
                userCollectionsEntity.setUserId(toUser.getId());
                userCollectionsEntity.setTransferId(transferCollectionRecordEntity.getId());
                userCollectionsEntity.setNftId(transferCollectionRecordEntity.getNftId());
                userCollectionsEntity.setNftIdAmount(transferCollectionRecordEntity.getNftIdAmount());
                userCollectionsEntity.setUserName(toUser.getUserNick());
                userCollectionsEntity.setUserAvatar(toUser.getUserAvatar());
                userCollectionsEntity.setStatus(1);
                userCollectionsEntity.setMintStatus(NFTConstant.TRANSFER_FINSH_STATUS);
                super.saveOrUpdate(userCollectionsEntity);

            }
    }


    private void buildBaseUserCollection(ArtCollectionEntity artCollection ,UserCollectionsEntity userCollectionsEntity){
        userCollectionsEntity.setCollectionId(artCollection.getId());
        userCollectionsEntity.setCollectionName(artCollection.getCollectionName());
        userCollectionsEntity.setCollectionCover(artCollection.getCollectionCover());
        userCollectionsEntity.setCollectionType(artCollection.getCollectionType());
        userCollectionsEntity.setCharacterMainDisplay(artCollection.getMainDisplay());
        userCollectionsEntity.setWriterId(artCollection.getWriterId());
        userCollectionsEntity.setArtCreatorId(artCollection.getArtCreatorId());
        userCollectionsEntity.setChapterNumber(artCollection.getChapterNumber());
        userCollectionsEntity.setCollectionCode(UUIDGenerator.getUUID());
        userCollectionsEntity.setMintStatus(NFTConstant.MINT_STATUS_WAIT_MINT);
        userCollectionsEntity.setStoryId(artCollection.getStoryId());
        userCollectionsEntity.setStatus(1);
        userCollectionsEntity.setSmartContractAddress(artCollection.getSmartContractAddress());
        userCollectionsEntity.setCharacterId(artCollection.getCharacterId());
        userCollectionsEntity.setCharacterName(artCollection.getCharacterName());
        userCollectionsEntity.setStoryId(artCollection.getStoryId());
        userCollectionsEntity.setStoryTitle(artCollection.getStoryTitle());
        userCollectionsEntity.setStoryTaleId(artCollection.getStoryTaleId());
        userCollectionsEntity.setStoryTaleTitle(artCollection.getStoryTaleTitle());

        userCollectionsEntity.setCreateTime(DateUtils.currentSecs());
    }


    public CodeDataResp getUserMainAvatarCollections(UserCollectionPageReq pageReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        Page<UserCollectionsEntity> page = MpPageUtil.getCommonPage(pageReq);
        QueryWrapper<UserCollectionsEntity> condition = new QueryWrapper<>();

            condition.eq("user_id", userDto.getId());
            condition.eq("status",1);
            condition.eq("collection_type", NFTConstant.NFT_TYPE_AVATAR);
            //condition.eq("character_main_display",1);

        if (StrUtils.isVaileNum(pageReq.getArtCreatorId())){
            condition.eq("art_creator_id",pageReq.getArtCreatorId());
        }
        if (StrUtils.isVaileNum(pageReq.getCharacterId())){
            condition.eq("character_id",pageReq.getCharacterId());
        }

       return CodeDataResp.valueOfSuccess(super.page(page,condition));
   }

    public CodeDataResp getAlumnCollections(BaseIdReq baseIdReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        StoryTalesEntity storyTalesEntity = storyTalesService.getById(baseIdReq.getId());
        if (storyTalesEntity==null)
            return CodeDataResp.valueOfFailed("Ip不存在");
        List<UserCollectionAlumnDto> userCollectionAlumnDtoList = new ArrayList<>();
        List<ArtCollectionEntity> artCollectionEntityList = artCollectionService.getArtsByTaleId(baseIdReq.getId());
        List<UserCollectionsEntity> userCollectionsEntityList = getUserCollectionByIp(userDto.getId(),baseIdReq.getId());

        if (artCollectionEntityList.size()>0){

            List<ArtCollectionEntity> mainList = artCollectionEntityList.stream().filter(t ->t.getMainDisplay()==1).collect(Collectors.toList());
            List<ArtCollectionEntity> normalList = artCollectionEntityList.stream().filter(t ->t.getMainDisplay()==0).collect(Collectors.toList());
            if (mainList.size()>0)
                userCollectionAlumnDtoList.addAll(buildMainst(mainList,userCollectionsEntityList));
            if (normalList.size()>0)
                userCollectionAlumnDtoList.addAll(buildNormalList(normalList,userCollectionsEntityList));
        }
        return CodeDataResp.valueOfSuccess(userCollectionAlumnDtoList);
    }

    private  List<UserCollectionAlumnDto> buildMainst(List<ArtCollectionEntity> list,List<UserCollectionsEntity> userCollectionsEntityList){
        List<UserCollectionAlumnDto> userCollectionAlumnDtoList = new ArrayList<>();
        Map<Integer,List<ArtCollectionEntity>> listMap = list.stream().collect(Collectors.groupingBy(t -> t.getCollectionType()));
        for (Map.Entry<Integer,List<ArtCollectionEntity>> entry:listMap.entrySet()){
            Integer collectionType = entry.getKey();
            List<ArtCollectionEntity> collectionEntityList = entry.getValue();
            UserCollectionAlumnDto alumnDto = new UserCollectionAlumnDto();
            alumnDto.setCollectionType(collectionType);
            alumnDto.setMainDisplay(1);
            if (collectionEntityList.size()>0)
                userCollectionAlumnDtoList.add(createUserAlumn(alumnDto,collectionEntityList,userCollectionsEntityList));
        }

        return userCollectionAlumnDtoList;
    }

    private  List<UserCollectionAlumnDto> buildNormalList(List<ArtCollectionEntity> list,List<UserCollectionsEntity> userCollectionsEntityList){
        List<UserCollectionAlumnDto> userCollectionAlumnDtoList = new ArrayList<>();
        Map<Integer,List<ArtCollectionEntity>> listMap = list.stream().collect(Collectors.groupingBy(t -> t.getStoryId()));
        for (Map.Entry<Integer,List<ArtCollectionEntity>> entry:listMap.entrySet()){
            Integer storyId = entry.getKey();
            List<ArtCollectionEntity> collectionEntityList = entry.getValue();
            UserCollectionAlumnDto alumnDto = new UserCollectionAlumnDto();
            alumnDto.setStoryId(storyId);
            alumnDto.setMainDisplay(0);
            if (collectionEntityList.size()>0)
                userCollectionAlumnDtoList.add(createUserAlumn(alumnDto,collectionEntityList,userCollectionsEntityList));
        }

        return userCollectionAlumnDtoList;
    }



    private UserCollectionAlumnDto createUserAlumn(UserCollectionAlumnDto alumnDto, List<ArtCollectionEntity> collectionEntityList, List<UserCollectionsEntity> userCollectionsEntityList){


        List<UserCollectionDetailDto> detailDtoList = new ArrayList<>();

        for (Integer i=0;i<collectionEntityList.size();i++){
            ArtCollectionEntity artCollection = collectionEntityList.get(i);
            NftTypeDict.NftSource nftSource = EnumUtils.indexToEnum(NftTypeDict.NftSource.class,
                    artCollection.getCollectionType());
            if (i==0) {
                alumnDto.setCollectionType(artCollection.getCollectionType());
                alumnDto.setStoryTaleTitle(artCollection.getStoryTaleTitle());
                alumnDto.setChapterNumber(artCollection.getChapterNumber());
                alumnDto.setStoryTitle(artCollection.getStoryTitle());
                alumnDto.setCollectionTypeName(nftSource.getValue());
            }

            UserCollectionDetailDto detailDto = new UserCollectionDetailDto();
            BeanMapper.copy(artCollection,detailDto);
            detailDto.setCollectionId(artCollection.getId());
            detailDto.setCreateTime(artCollection.getCreateTime());
            detailDto.setCollectionTypeName(nftSource.getValue());
            if (artCollection.getMainDisplay()==1)
                detailDto.setMainDisplay(artCollection.getMainDisplay());
            detailDto.setCollectionStatus(artCollection.getSoldStatus()==NFTConstant.SOLD_STATUS_WAIT?0:2);
            if (userCollectionsEntityList.size()>0) {
                List<UserCollectionsEntity> collectionsEntityList =
                        userCollectionsEntityList.stream().
                                filter(t -> t.getCollectionId().equals(artCollection.getId())).collect(Collectors.toList());

                detailDto.setBlockSmartContractAddress(artCollection.getSmartContractAddress());
                if (collectionsEntityList.size() > 0){
                    detailDto.setNftId(collectionsEntityList.get(0).getNftId());
                    detailDto.setCreateTime(collectionsEntityList.get(0).getCreateTime());
                    detailDto.setCollectionStatus(1);
                }

            }
            detailDtoList.add(detailDto);
        }
        if (detailDtoList.size()>0){
            detailDtoList = detailDtoList.stream().sorted((a,b) -> a.getCollectionType() - b.getCollectionType()).collect(Collectors.toList());
            alumnDto.setDetailDtoList(detailDtoList);
        }



        return alumnDto;
    }

    public CodeDataResp setMainDisPlayByAvatar(BaseIdReq baseIdReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        UserEntity userEntity = userService.getById(userDto.getId());
        UserCollectionsEntity collectionsEntity = super.getById(baseIdReq.getId());
        if (collectionsEntity==null)
            return CodeDataResp.valueOfFailed("数据非法");
        collectionsEntity.setCharacterMainDisplay(1);
        collectionsEntity.setUpdateTime(DateUtils.currentSecs());
        userEntity.setUserAvatar(collectionsEntity.getCollectionCover());
        userEntity.setUserNick(collectionsEntity.getCollectionName()+" #"+collectionsEntity.getNftId());
//        UserCollectionsEntity oldCollection = findMainDisplayAvatar(userDto.getId(),collectionsEntity.getCharacterId());
//        if (oldCollection!=null){
//            oldCollection.setCharacterMainDisplay(0);
//            oldCollection.setUpdateTime(DateUtils.currentSecs());
//            super.saveOrUpdate(oldCollection);
//        }
        userService.saveOrUpdate(userEntity);
        super.saveOrUpdate(collectionsEntity);

        return CodeDataResp.valueOfSuccessEmptyData();

    }

    /**
     * 根据空投记录给用户空投
     **/
    public Boolean createAirDropCollection(List<Integer> userIds,List<UserJoinDropEntity> joinDropEntityList, ArtCollectionEntity artCollection,CollectionSoldSettingEntity collectionSoldSetting){
        Integer nftAmount = collectionSoldSetting.getPublishAmount();
        Integer soldAmout = 0;
        List<UserCollectionsEntity> userCollectionsEntityList = new ArrayList<>();
        List<UserJoinDropEntity> dropEntityList = new ArrayList<>();

        if (joinDropEntityList.size()>0){
            for (Integer userId:userIds){
                List<UserJoinDropEntity> userJoinDropEntityList = joinDropEntityList.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toList());
                if (userJoinDropEntityList.size()>0){
                    UserJoinDropEntity userJoinDropEntity = userJoinDropEntityList.get(0);
                    userJoinDropEntity.setStatus(1);
                    userJoinDropEntity.setDropStatus(1);
                    UserCollectionsEntity userCollectionsEntity = new UserCollectionsEntity();
                    buildBaseUserCollection(artCollection,userCollectionsEntity);
                    userCollectionsEntity.setUserId(userJoinDropEntity.getUserId());
                    userCollectionsEntity.setUserName(userJoinDropEntity.getUserName());
                    userCollectionsEntity.setUserAvatar(userJoinDropEntity.getUserAvatar());
                    userCollectionsEntity.setNftId(nftAmount);
                    dropEntityList.add(userJoinDropEntity);
                    userCollectionsEntityList.add(userCollectionsEntity);
                    nftAmount--;
                    soldAmout++;
                }

                redisTemplate.opsForList().remove(RedisPubKeyConstant.getJoinDropRankingKey(collectionSoldSetting.getId()),0,userId);

            }
        }

        if (dropEntityList.size()>0){
            userJoinDropService.saveOrUpdateBatch(dropEntityList);
        }
        //去mint
        if (userCollectionsEntityList.size()>0){
            super.saveOrUpdateBatch(userCollectionsEntityList);
        }

        artCollection.setSoldNumber(soldAmout);
        artCollection.setPublishAmount(nftAmount);
        artCollectionService.saveOrUpdate(artCollection);
        collectionSoldSettingService.updateCollectionStock(artCollection);

        return true;

    }

    private List<UserCollectionsEntity> getUserCollectionByIp(Integer userId,Integer storyTaleId){
        QueryWrapper<UserCollectionsEntity> condition = new QueryWrapper<>();

        condition.eq("user_id",userId);
        condition.eq("status",1);
        condition.eq("story_tale_id",storyTaleId);

        return super.list(condition);
    }


    private UserCollectionsEntity findMainDisplayAvatar(Integer userId,Integer characterId){
        QueryWrapper<UserCollectionsEntity> condition = new QueryWrapper<>();

        condition.eq("user_id",userId);
        condition.eq("status",1);
        condition.eq("character_id",characterId);


        return super.getOne(condition);
    }


}
