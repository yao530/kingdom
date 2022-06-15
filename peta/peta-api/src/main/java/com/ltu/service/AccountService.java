package com.ltu.service;

import com.ltu.config.shiro.dto.LoginReq;
import com.ltu.domain.mp_entity.AccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.model.request.account.AccountPageReq;
import com.ltu.model.request.account.AccountReq;
import com.ltu.model.request.base.BaseIdReq;

import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.dto.AccountLoginDto;

/**
 * <p>
 * 角色管理 服务类
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */

public interface AccountService extends IService<AccountEntity> {

    /**
     * 获取记录列表
     * @return
     */
    CodeDataResp getList(AccountPageReq pageReq);

    /**
     * 获取记录列表:获取代理商或者代理商获取企业账号
     * @param req
     * @return
     */
    CodeDataResp getSysAccount(AccountPageReq pageReq);

    /**
     * 保存或更新
     * @param req
     * @return
     */
    CodeDataResp saveOrUpdate(AccountReq accountReq);


    /**
     * 创建的用户为管理员角色 AccountType.ACCOUNT_TYPE_CHILDADMIN，
     * 则同步创建管理员登录账号
     * @param
     * @return
     */
     AccountEntity createAccountFromUserInfo(UserEntity userEntity);
    /**
     * 保存或更新
     * @param userLoginReq
     * @return
     */
    CodeDataResp<AccountLoginDto> login(LoginReq userLoginReq);



    /**
     * 删除记录
     * @param req
     * @return
     */
    CodeDataResp deleteAcctount(BaseIdReq req);



    AccountEntity getAccountByPhone(String phone);

    /**
     * 获取代理商列表分页
     * @param req
     * @return
     */
    CodeDataResp getAgentList();

    /**
     * 获取当前账号信息
     * @return
     */
    CodeDataResp getCurrentAccount();

    /**
     * 获取租户
     * @param req
     * @return
     */
    CodeDataResp getTenantList(AccountPageReq pageReq);
}
