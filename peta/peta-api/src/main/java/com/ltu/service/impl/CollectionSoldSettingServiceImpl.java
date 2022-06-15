package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.NFTConstant;
import com.ltu.domain.mp_entity.*;
import com.ltu.enums.RedisBusinessDict;
import com.ltu.mapper.CollectionSoldSettingMapper;
import com.ltu.model.request.RollBackKey;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.dto.CollectionSettingDetailDto;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.UUIDGenerator;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 藏品销售设置 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-05-19
 */
@Service
public class CollectionSoldSettingServiceImpl extends BaseServiceImpl<CollectionSoldSettingMapper, CollectionSoldSettingEntity> implements CollectionSoldSettingService {

    @Autowired
    ArtCollectionService artCollectionService;

    @Autowired
    UserInviteRecordService userInviteRecordService;

    @Autowired
    UserJoinDropService userJoinDropService;
    @Autowired
    UserService userService;

    @Autowired
    RollBackKey rollBackKey;

    public CodeDataResp creatSetting(CollectionSettingDetailDto collectionSettingDetailDto){
        Long nowDate = DateUtils.currentSecs();
       if (collectionSettingDetailDto.getSoldType()== NFTConstant.SOLD_TYPE_AIR_DROP
               ||collectionSettingDetailDto.getSoldType()==NFTConstant.SOLD_TYPE_BOOK_DROP)
       {
           if (collectionSettingDetailDto.getBookEndTime()>collectionSettingDetailDto.getStartTime())
               return CodeDataResp.valueOfFailed("活动时间需在售卖时间前");
           if (collectionSettingDetailDto.getBookStartTime()>collectionSettingDetailDto.getBookEndTime())
               return CodeDataResp.valueOfFailed("活动开始时间需小于结束时间");
           if (nowDate>collectionSettingDetailDto.getBookStartTime())
               return CodeDataResp.valueOfFailed("活动结束时间需要大于当前时间");

       }
        if (collectionSettingDetailDto.getStartTime()>collectionSettingDetailDto.getEndTime())
            return CodeDataResp.valueOfFailed("开售时间需小于结束时间");
        if (nowDate>collectionSettingDetailDto.getEndTime())
            return CodeDataResp.valueOfFailed("结束时间需要大于当前时间");
        ArtCollectionEntity artCollection = artCollectionService.getById(collectionSettingDetailDto.getCollectionId());
        if (artCollection!=null){
            artCollection.setPrice(collectionSettingDetailDto.getPrice());
            artCollection.setStartTime(collectionSettingDetailDto.getStartTime());
            artCollection.setEndTime(collectionSettingDetailDto.getEndTime());
            artCollection.setBookStartTime(collectionSettingDetailDto.getBookStartTime());
            artCollection.setBookEndTime(collectionSettingDetailDto.getBookEndTime());
            artCollection.setSoldType(collectionSettingDetailDto.getSoldType());
            artCollection.setPublishStatus(collectionSettingDetailDto.getPublishStatus());
            artCollection.setUpdateTime(DateUtils.currentSecs());
            artCollection.setCollectionOpenType(collectionSettingDetailDto.getCollectionOpenType());
            if (collectionSettingDetailDto.getSoldType()==NFTConstant.SOLD_TYPE_AIR_DROP
                    ||collectionSettingDetailDto.getSoldType()==NFTConstant.SOLD_TYPE_BOOK_DROP)
            {
                if (artCollection.getBookStartTime()<nowDate&&nowDate<artCollection.getBookEndTime())
                    artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_BOOKING);

            }
            if (collectionSettingDetailDto.getSoldType()==NFTConstant.SOLD_TYPE_LIMIT_SELL){
                if (artCollection.getStartTime()>nowDate)
                    artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_WAIT);
                if (artCollection.getStartTime()<nowDate&&nowDate<artCollection.getEndTime())
                    artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_SELLING);
            }


            CollectionSoldSettingEntity soldSetting = super.getById(collectionSettingDetailDto.getId());
            if (soldSetting==null){
                soldSetting = new CollectionSoldSettingEntity();
                soldSetting.setCollectionId(artCollection.getId());

                soldSetting.setCreateTime(DateUtils.currentSecs());
            }
            BeanMapper.copy(collectionSettingDetailDto,soldSetting);
            soldSetting.setUpdateTime(DateUtils.currentSecs());
            soldSetting.setNeedInvited(collectionSettingDetailDto.getNeedInvited());
            artCollectionService.saveOrUpdate(artCollection);
            //添加定时器 计算商品的开售情况
            addCollectionTimeCounter(artCollection);
            this.saveOrUpdate(soldSetting);

        }

        return CodeDataResp.valueOfSuccessEmptyData();
    }

    private void addCollectionTimeCounter(ArtCollectionEntity artCollection){
        Long dateNow = DateUtils.currentMillis();
        if (artCollection.getSoldType().equals(NFTConstant.SOLD_TYPE_AIR_DROP)||artCollection.getSoldType().equals(NFTConstant.SOLD_TYPE_BOOK_DROP)){
            rollBackKey.setThingKey(RedisBusinessDict.BusinessName.BookTimeStart,artCollection.getId(),100l);//bookStartTime-now
            rollBackKey.setThingKey(RedisBusinessDict.BusinessName.BookTimeEnd,artCollection.getId(),200l);//bookEndTime-now
        }
        if (!artCollection.getSoldType().equals(NFTConstant.SOLD_TYPE_AIR_DROP)){//空投不需要购买
            rollBackKey.setThingKey(RedisBusinessDict.BusinessName.StartTimeTag,artCollection.getId(),300l);//startTime-now
            rollBackKey.setThingKey(RedisBusinessDict.BusinessName.EndTimeTag,artCollection.getId(),400l);//endTime-now
        }

    }

    public void updateCollectionSetting(Integer collectionId,String updateStr){
        ArtCollectionEntity artCollection = artCollectionService.getById(collectionId);
        CollectionSoldSettingEntity collectionSoldSetting = getCollectionSetting(artCollection.getId());

        if (RedisBusinessDict.BusinessName.BookTimeStart.toString().equals(updateStr)){
            artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_BOOKING);
            artCollectionService.saveOrUpdate(artCollection);
        }
        if (RedisBusinessDict.BusinessName.BookTimeEnd.toString().equals(updateStr)){
            artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_WAIT_BOOKED);
            artCollection.setUpdateTime(DateUtils.currentMillis());
            //队列计算空投资格人数
            if (artCollection.getSoldType()==NFTConstant.SOLD_TYPE_AIR_DROP){
                userJoinDropService.countAriDropAuthUsers(artCollection,collectionSoldSetting);
            }
            else {//预约
                userJoinDropService.countBookedDraw(artCollection,collectionSoldSetting);
            }
        }
        if (RedisBusinessDict.BusinessName.StartTimeTag.toString().equals(updateStr)){
            artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_SELLING);
        }
        if (RedisBusinessDict.BusinessName.EndTimeTag.toString().equals(updateStr)){
            artCollection.setSoldStatus(NFTConstant.SOLD_STATUS_SOLD_OUT);
        }
    }

    /**
     * @desc 更新nft 库存 需要把库存保存在redis 统一管理
     **/
    public void updateCollectionStock(ArtCollectionEntity collectionEntity){
        CollectionSoldSettingEntity collectionSoldSettingEntity = getCollectionSetting(collectionEntity.getId());
        if (collectionSoldSettingEntity!=null){
            collectionSoldSettingEntity.setMintAmount(collectionEntity.getMintAmount());
            collectionSoldSettingEntity.setSoldNumber(collectionEntity.getSoldNumber());
            collectionSoldSettingEntity.setPublishAmount(collectionEntity.getPublishAmount());
            collectionSoldSettingEntity.setUpdateTime(DateUtils.currentMillis());
            super.saveOrUpdate(collectionSoldSettingEntity);
        }
    }

    public CollectionSettingDetailDto getCollectionSettingDetail(BaseIdReq baseIdReq){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        UserEntity userEntity = userService.getById(userDto.getId());
        CollectionSettingDetailDto collectionSettingDetailDto = new CollectionSettingDetailDto();
        ArtCollectionEntity artCollection = artCollectionService.getById(baseIdReq.getId());
        if (artCollection!=null){
            BeanMapper.copy(artCollection,collectionSettingDetailDto);
            collectionSettingDetailDto.setCollectionId(artCollection.getId());
            CollectionSoldSettingEntity settingEntity = getCollectionSetting(artCollection.getId());
            if (settingEntity!=null){
                BeanMapper.copy(settingEntity,collectionSettingDetailDto);
                collectionSettingDetailDto.setNeedInvited(settingEntity.getNeedInvited());
                if (artCollection.getSoldType()== NFTConstant.SOLD_TYPE_AIR_DROP||artCollection.getSoldType()== NFTConstant.SOLD_TYPE_BOOK_DROP){
                   collectionSettingDetailDto = buildCollectionSettingDetailDto(collectionSettingDetailDto,settingEntity,userEntity);
                }
            }
        }
        return collectionSettingDetailDto;
    }

    private CollectionSettingDetailDto buildCollectionSettingDetailDto(CollectionSettingDetailDto collectionSettingDetailDto ,CollectionSoldSettingEntity settingEntity,UserEntity userEntity){
        List<UserInviteRecordEntity> inviteRecordEntityList=userInviteRecordService.getInvitRecordsByUser(userEntity.getId(),settingEntity.getCollectionId());
        UserJoinDropEntity userJoinDropEntity = userJoinDropService.getUserDropRecord(settingEntity.getId(),userEntity.getId());
        List<UserInviteRecordEntity> list = new ArrayList<>();
        //添加自己的参加记录
        UserInviteRecordEntity userInviteRecord = new UserInviteRecordEntity();
        userInviteRecord.setUserId(userEntity.getId());
        userInviteRecord.setInvitedUserName(userEntity.getUserNick());
        userInviteRecord.setInvitedUserAvatar(userEntity.getUserAvatar());
        userInviteRecord.setStatus(userJoinDropEntity==null?0:1);
        list.add(userInviteRecord);

        //邀请的好友记录
        if (inviteRecordEntityList.size()>0)
            list.addAll(inviteRecordEntityList);
        else {
            for (int i=0;i<3;i++){
                UserInviteRecordEntity recordEntity = new UserInviteRecordEntity();
                recordEntity.setStatus(0);
                list.add(recordEntity);
            }
        }

        if (userJoinDropEntity==null){
            userJoinDropEntity = new UserJoinDropEntity();
            userJoinDropEntity.setInvitedUserNumber(0);
            userJoinDropEntity.setDropStatus(-1);
        }
        collectionSettingDetailDto.setJoinDropEntity(userJoinDropEntity);
        collectionSettingDetailDto.setInviteList(list);

        return collectionSettingDetailDto;

    }

    public CollectionSoldSettingEntity getCollectionSetting(Integer collectionId){
        QueryWrapper<CollectionSoldSettingEntity> condition = new QueryWrapper<>();
        condition.eq("collection_id",collectionId);

        return this.getOne(condition);
    }

}
