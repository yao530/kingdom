package com.ltu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.domain.mp_entity.UserNotification;
import com.ltu.model.request.base.BaseIdPageReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vk@rongding
 * @since 2020-08-15
 */
public interface UserNotificationService extends IService<UserNotification> {

    /**
     * 获取消息列表
     * @param baseIdPageReq
     * @return
     */
    CodeDataResp getList(BaseIdPageReq baseIdPageReq);

    /**
     * 获取消息详情
     * @param baseIdReq
     * @return
     */
    CodeDataResp getDetail(BaseIdReq baseIdReq);

    /**
     * 删除消息
     * @param baseIdReq
     * @return
     */
    CodeDataResp remove(BaseIdReq baseIdReq);

    /**
     * 增加消息记录
     * @param userNotification
     */
    void addUserNotification(UserNotification userNotification);

    /**
     * 批量发送通知-异步
     */
    void sendNotificationToAllUser(String notificationId);
}
