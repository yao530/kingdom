package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.constant.JmsTopicValue;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.domain.mp_entity.UserNotification;
import com.ltu.mapper.UserNotificationMapper;
import com.ltu.model.request.base.BaseIdPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.UserNotificationService;
import com.ltu.service.UserService;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vk@rongding
 * @since 2020-08-15
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserNotificationServiceImpl extends ServiceImpl<UserNotificationMapper, UserNotification> implements UserNotificationService {

    private final UserService userService;


    /**
     * 获取消息列表
     * @param baseIdPageReq
     * @return
     */
    public CodeDataResp getList(BaseIdPageReq baseIdPageReq) {
        List<UserNotification> notificationList;
        Page page = new Page(baseIdPageReq.getPageNumber(), baseIdPageReq.getPageSize());
        if (ShiroUtil.isPrincipalUserAdmin()) {
            notificationList = super.baseMapper.getNotificationList(
                    (baseIdPageReq.getPageNumber()-1)*baseIdPageReq.getPageSize(),
                    baseIdPageReq.getPageSize());
            page.setTotal(super.count(null));
        }
        else {
            Integer userId = ShiroUtil.getPrincipalUserId();
            QueryWrapper<UserNotification> condition = new QueryWrapper<>();
            condition.eq("user_id", userId);
            notificationList = super.baseMapper.selectNotificationByUserId(userId,
                    (baseIdPageReq.getPageNumber()-1)*baseIdPageReq.getPageSize(),
                    baseIdPageReq.getPageSize());
            page.setTotal(super.count(condition));
        }
        page.setRecords(notificationList);
        return CodeDataResp.valueOfSuccess(page);
    }

    /**
     * 获取消息详情
     * @param baseIdReq
     * @return
     */
    public CodeDataResp getDetail(BaseIdReq baseIdReq) {
        UserNotification userNotification = super.baseMapper.selectNotificationById(baseIdReq.getId());
        //消息为空
        if (userNotification == null)
            return CodeDataResp.valueOfFailed("消息已被删除");
        //userId不匹配
        if (!ShiroUtil.isPrincipalUserAuthorized(userNotification.getUserId()))
            return CodeDataResp.valueOfErrorUnAuthorized();
        //设置已读
        if (userNotification.getStatus() != 1) {
            userNotification.setStatus((byte) 1);
            super.updateById(userNotification);
        }
        //母体消息数+1
        //jmsMessagingTemplate.convertAndSend(new ActiveMQTopic(JmsTopicValue.INCREASE_NOTIFICATION_READ_COUNT),
        //                                    userNotification.getNotificationId());

        return CodeDataResp.valueOfSuccess(userNotification);
    }

    /**
     * 删除消息
     * @param baseIdReq
     * @return
     */
    public CodeDataResp remove(BaseIdReq baseIdReq) {
        UserNotification userNotification = super.getById(baseIdReq.getId());
        //记录不存在
        if (userNotification == null)
            return CodeDataResp.valueOfErrorNoRecord();
        super.removeById(baseIdReq);
        return CodeDataResp.valueOfSuccessEmptyData();
    }

    /**
     * 增加消息记录
     * @param userNotification
     */
    public void addUserNotification(UserNotification userNotification) {
//        userNotification.setCreateTime(DateUtils.currentSecs().intValue())
//                .setId(UUIDGenerator.getUUID());
        super.save(userNotification);
    }

    /**
     * 批量发送通知-异步
     */
    @Async
    public void sendNotificationToAllUser(String notificationId) {
        if (StrUtils.isTrimNull(notificationId))
            return;
        //分批发送
        BaseIdPageReq pageReq = new BaseIdPageReq();
        pageReq.setPageSize(100);
        pageReq.setPageNumber(1);
        Page<UserEntity> userPage = userService.page(MpPageUtil.getCommonPage(pageReq));
        //发送page1
        batchsaveUserNotification(userPage.getRecords(), notificationId);
        //循环发送剩余的page
        for (int i = 2; i <= userPage.getPages(); i++) {
            pageReq.setPageNumber(i);
            Page<UserEntity> tempPage = userService.page(MpPageUtil.getCommonPage(pageReq));
            batchsaveUserNotification(tempPage.getRecords(), notificationId);
        }
    }

    /**
     * 批量插入记录
     * @param userList
     * @param notificationId
     */
    private void batchsaveUserNotification(List<UserEntity> userList, String notificationId) {
        if (userList == null || StrUtils.isTrimNull(notificationId)) return;
        List<UserNotification> userNotificationList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            UserEntity user = userList.get(i);
            UserNotification userNotification = new UserNotification();
            userNotification. setStatus((byte) 0)
                    .setCreateTime(DateUtils.currentSecs().intValue())
                    .setUserId(user.getId().toString())
                    .setNotificationId(notificationId);
            userNotificationList.add(userNotification);
        }
        super.saveBatch(userNotificationList);
    }
}
