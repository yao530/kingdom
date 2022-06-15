package com.ltu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.NFTConstant;
import com.ltu.constant.RedisPubKeyConstant;
import com.ltu.domain.mp_entity.*;
import com.ltu.mapper.UserJoinDropMapper;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.dto.UserRankingDto;
import com.ltu.service.*;
import com.ltu.util.common.MathUtils;
import com.ltu.util.common.UUIDGenerator;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户藏品抽签记录 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */
@Service
public class UserJoinDropServiceImpl extends BaseServiceImpl<UserJoinDropMapper, UserJoinDropEntity> implements UserJoinDropService {

    @Autowired
    UserService userService;
    @Autowired
    CollectionSoldSettingService collectionSoldSettingService;
    @Autowired
    UserInviteRecordService userInviteRecordService;
    @Autowired
    ArtCollectionService artCollectionService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserCollectionsService userCollectionsService;
    @Autowired
    CollectionBlindBoxService collectionBlindBoxService;

    public CodeDataResp joinCollectionDrop(BaseIdReq idReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        UserEntity userEntity = userService.getById(userDto.getId());
        if (userEntity==null)
            return CodeDataResp.valueOfFailed("用户数据非法");

        CollectionSoldSettingEntity settingEntity = collectionSoldSettingService.getById(idReq.getId());
        if (settingEntity==null)
            return CodeDataResp.valueOfFailed("活动非法");
        if (settingEntity.getStatus()!=1)
            return CodeDataResp.valueOfFailed("活动已失效");

        ArtCollectionEntity artCollection = artCollectionService.getById(settingEntity.getCollectionId());
        if (artCollection==null)
            return CodeDataResp.valueOfFailed("数据非法");

//        if (settingEntity.getDropType()==NFTConstant.SOLD_TYPE_AIR_DROP&&(
//                settingEntity.getJoinUserAuthType()==NFTConstant.AIR_DROP_USER_NEW_USER||
//                        settingEntity.getJoinUserAuthType()==NFTConstant.AIR_DROP_USER_INVITE_RELATION_USER)){
//            if (userEntity.getCreateTime()<artCollection.getBookStartTime()){
//                return CodeDataResp.valueOfFailed("仅限新用户参加");
//            }
//        }
        UserJoinDropEntity joinDropEntity = getUserDropRecord(idReq.getId(),userEntity.getId());
        if (joinDropEntity!=null)
            return CodeDataResp.valueOfFailed("你已参加活动");
//        if(artCollection.getSoldType()!= NFTConstant.SOLD_TYPE_AIR_DROP||artCollection.getSoldType()!= NFTConstant.SOLD_TYPE_APPOINTMENT_DROP)
//            return CodeDataResp.valueOfFailed("非抽签藏品");
//        if (artCollection.getPublishStatus()!=NFTConstant.PUBLISH_ONLINE_STATUS)
//            return CodeDataResp.valueOfFailed("藏品未上架");;
//        if(artCollection.getSoldStatus()== NFTConstant.SOLD_STATUS_WAIT)
//            return CodeDataResp.valueOfFailed("活动未开始");
//        if(artCollection.getSoldStatus()== NFTConstant.SOLD_STATUS_WAIT_BOOKED)
//            return CodeDataResp.valueOfFailed("抽签中请耐心等候");

        joinDropEntity = new UserJoinDropEntity();
        joinDropEntity.setCollectionId(settingEntity.getCollectionId());
        joinDropEntity.setCollectionSettingId(settingEntity.getId());
        joinDropEntity.setSoldType(settingEntity.getSoldType());
        joinDropEntity.setDropType(settingEntity.getDropType());
        joinDropEntity.setUserId(userEntity.getId());
        joinDropEntity.setUserAvatar(userEntity.getUserAvatar());
        joinDropEntity.setUserName(userEntity.getUserNick());
        joinDropEntity.setJoinDropTime(DateUtils.currentSecs());
        joinDropEntity.setCreateTime(DateUtils.currentSecs());
        joinDropEntity.setStatus(0);
        joinDropEntity.setInviteCode(UUIDGenerator.getUUID());
        //按参加空投活动先后顺序

        addUserJoinDropRanking(artCollection,settingEntity,userEntity);
        addInviteRanking(settingEntity.getId(),userEntity.getId(),0);
        addFinshRanking(settingEntity.getId(),userEntity.getId(),DateUtils.currentMillis());
        this.save(joinDropEntity);
        return CodeDataResp.valueOfSuccess(joinDropEntity);
    }

    /**
     * @desc 统计并给用户发放空投藏品
     */
    public void countAriDropAuthUsers(ArtCollectionEntity artCollection,CollectionSoldSettingEntity collectionSoldSetting){
        List<Integer> successJoin = new ArrayList<>();
        List<UserJoinDropEntity> joinDropEntityList = new ArrayList<>();
        if (collectionSoldSetting.getDropType()==NFTConstant.DROP_TYPE_FREE){//参加即可的空投 按参加顺序与空投数量
            List<Integer> joinRankings = getJoinDropRanking(collectionSoldSetting.getId());
            if (joinRankings.size()>0){
                successJoin = redisTemplate.opsForList().range(RedisPubKeyConstant.getJoinDropRankingKey(collectionSoldSetting.getId()),0,collectionSoldSetting.getPublishAmount());

            }
        }
        else {
            List<UserRankingDto> userRankingDtos = getRankingByInviteType(collectionSoldSetting);
            if (userRankingDtos.size()>0){
               successJoin = userRankingDtos.stream().map(t -> t.getValue()).collect(Collectors.toList());
            }
        }

        if (successJoin.size()>0){
            joinDropEntityList = getUserJoinDrops(collectionSoldSetting.getId(),successJoin);
        }
        if (artCollection.getCollectionOpenType()==NFTConstant.OEPN_TYPE_BLIND_BOX){
            collectionBlindBoxService.createBoxByAirDrop(successJoin,joinDropEntityList,artCollection,collectionSoldSetting);
        }
        else{
            userCollectionsService.createAirDropCollection(successJoin,joinDropEntityList,artCollection,collectionSoldSetting);
        }
    }
    /**
     * @desc 统计参加预约的用户 完成抽签
     */
    public void countBookedDraw(ArtCollectionEntity artCollection,CollectionSoldSettingEntity collectionSoldSetting){
        List<UserRankingDto> userRankingDtos = getRankingByInviteType(collectionSoldSetting);
        List<UserJoinDropEntity> dropEntityList = new ArrayList<>();
        if (userRankingDtos.size()>0){
            List<Integer> userIds = userRankingDtos.stream().map(t -> t.getValue()).collect(Collectors.toList());
            if (userIds.size()>0){
                List<UserJoinDropEntity> joinDropEntityList = getUserJoinDrops(collectionSoldSetting.getId(),userIds);
                if (joinDropEntityList.size()>0){
                    for (UserJoinDropEntity userJoinDropEntity:joinDropEntityList){
                        userJoinDropEntity.setDropStatus(1);
                        userJoinDropEntity.setUpdateTime(DateUtils.currentMillis());
                        dropEntityList.add(userJoinDropEntity);
                    }
                }
            }
        }
        if (dropEntityList.size()>0){
            super.saveOrUpdateBatch(dropEntityList);
        }
        artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_WAIT_BOOKED);
        artCollection.setUpdateTime(DateUtils.currentMillis());
        artCollectionService.saveOrUpdate(artCollection);

    }



    /**
     * @desc 获取参加空投/预约活动的顺序
     */
    private List<Integer> getJoinDropRanking(Integer settingId){
       return redisTemplate.opsForList().range(RedisPubKeyConstant.getJoinDropRankingKey(settingId),0,-1);
    }

    /**
     * @desc 获取参加空投/预约活动的邀请数量排名，或者完全邀请任务先后排名
     */
    private List<UserRankingDto> getRankingByInviteType(CollectionSoldSettingEntity collectionSoldSetting){
        List<UserRankingDto> userRankingDtos = new ArrayList<>();
        if (collectionSoldSetting.getDropType()==NFTConstant.DROP_TYPE_FINSH_INVITE_RANKD){
            Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisTemplate.opsForZSet().rangeWithScores(RedisPubKeyConstant.getFinshInviteRanking(collectionSoldSetting.getId()),0,-1);
            if (rangeWithScores!=null)
             userRankingDtos = JSON.parseArray(JSON.toJSONString(rangeWithScores),UserRankingDto.class);
        }
        if (collectionSoldSetting.getDropType()==NFTConstant.DROP_TYPE_DRAW_REWARD||collectionSoldSetting.getDropType()==NFTConstant.DROP_TYPE_INVITE_NUMBER_RANKING){
            Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisTemplate.opsForZSet().rangeWithScores(RedisPubKeyConstant.getInviteRankingKey(collectionSoldSetting.getId()),0,-1);
            if (rangeWithScores!=null)
                userRankingDtos = JSON.parseArray(JSON.toJSONString(rangeWithScores),UserRankingDto.class);
        }
        if(userRankingDtos.size()>0){
            userRankingDtos = userRankingDtos.stream().sorted((a, b) -> a.getScore().intValue() - b.getScore().intValue()).collect(Collectors.toList());
            if (collectionSoldSetting.getDropType()!=NFTConstant.DROP_TYPE_DRAW_REWARD){//按邀请人数随机抽签
                if (userRankingDtos.size()>collectionSoldSetting.getPublishAmount()){
                    //邀请人数多的中签
                    List<UserRankingDto> userRankingDtoList = userRankingDtos.stream().filter(t -> t.getScore() >0).collect(Collectors.toList());
                    List<UserRankingDto> unAuthUsers = userRankingDtos.stream().filter(t -> t.getScore()==0).collect(Collectors.toList());
                    Integer remainNumber = collectionSoldSetting.getPublishAmount()-1;
                    if (remainNumber>0&&unAuthUsers.size()>0){
                        for (int i=0;i<remainNumber;i++){


                        }
                    }
                }
            }
            else {
                //取完成邀请的前x名
                userRankingDtos = userRankingDtos.subList(0,userRankingDtos.size()>collectionSoldSetting.getPublishAmount()?collectionSoldSetting.getPublishAmount():userRankingDtos.size());

            }
        }
        return userRankingDtos;
    }
    /**
     * @desc 计算用户参加抽签活动的排名 按参加时间先后
     **/
    private void addUserJoinDropRanking(ArtCollectionEntity artCollection,CollectionSoldSettingEntity collectionSoldSettingEntity,UserEntity userEntity){
        if (artCollection.getSoldType()!= NFTConstant.SOLD_TYPE_AIR_DROP&&
                collectionSoldSettingEntity.getJoinUserAuthType()==NFTConstant.AIR_DROP_USER_INVITE_RELATION_USER){
           //给邀请用户空投、邀请用户限量获取
            UserInviteRecordEntity inviteRecordEntity = userInviteRecordService.getInviteRecordByInvitedUserId(userEntity.getId());
            UserJoinDropEntity userJoinDropEntity = getUserDropRecord(collectionSoldSettingEntity.getId(),inviteRecordEntity.getUserId());
            if (userJoinDropEntity!=null&&userJoinDropEntity.getInvitedUserNumber()<collectionSoldSettingEntity.getLimitSoldNumber()){
                redisTemplate.opsForList().leftPush(RedisPubKeyConstant.getJoinDropRankingKey(collectionSoldSettingEntity.getId()),userJoinDropEntity.getUserId());
            }
        }

        redisTemplate.opsForList().leftPush(RedisPubKeyConstant.getJoinDropRankingKey(collectionSoldSettingEntity.getId()),userEntity.getId());
    }
    /**
     * @desc 计算用户邀请人数排名
     **/
    public void addInviteRanking(Integer collectionSeetingId,Integer userId,Integer inviteNumber){
        redisTemplate.opsForZSet().incrementScore(RedisPubKeyConstant.getInviteRankingKey(collectionSeetingId), userId, inviteNumber);
    }
    /**
     * @desc 计算用户完成邀请任务排名
     **/
    public void addFinshRanking(Integer collectionSeetingId,Integer userId,Long finshTime){
        redisTemplate.opsForZSet().incrementScore(RedisPubKeyConstant.getFinshInviteRanking(collectionSeetingId), userId, finshTime);
    }

    public UserJoinDropEntity getUserDropRecord(Integer collectionDropSettingId,Integer userId){
        QueryWrapper<UserJoinDropEntity> condition = new QueryWrapper<>();
        condition.eq("collection_setting_id",collectionDropSettingId);
        condition.eq("user_id",userId);
        return this.getOne(condition);
    }

    public UserJoinDropEntity getUserDropRecordByCollection(Integer collectionId,Integer userId){
        QueryWrapper<UserJoinDropEntity> condition = new QueryWrapper<>();
        condition.eq("collection_id",collectionId);
        condition.eq("user_id",userId);
        return this.getOne(condition);
    }

    public List<UserJoinDropEntity> getUserJoinDrops(Integer collectionSettingId,List<Integer> userIds){
        QueryWrapper<UserJoinDropEntity> condition = new QueryWrapper<>();
        condition.eq("collection_setting_id",collectionSettingId);
        condition.in("user_id",userIds);
        condition.eq("status",0);
        condition.eq("drop_status",0);
        return super.list(condition);
    }



}
