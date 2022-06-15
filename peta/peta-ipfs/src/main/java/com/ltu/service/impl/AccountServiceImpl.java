package com.ltu.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltu.base.State;
import com.ltu.config.shiro.ShiroUserService;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.LoginReq;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.AccountType;
import com.ltu.constant.CommonConstant;
import com.ltu.constant.RoleType;
import com.ltu.domain.mp_entity.AccountEntity;

import com.ltu.domain.mp_entity.RoleEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.mapper.AccountMapper;

import com.ltu.model.request.account.AccountPageReq;
import com.ltu.model.request.account.AccountReq;

import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;

import com.ltu.model.response.LoginResponse;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.model.response.dto.AccountLoginDto;
import com.ltu.service.AccountService;
import com.ltu.service.RoleService;
import com.ltu.service.UserService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 角色管理 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, AccountEntity> implements AccountService {


    @Autowired
    ShiroUserService shiroUserService;

    @Autowired
    RoleServiceImpl roleService;



    @Autowired
    UserServiceImpl userService;



    /**
     * 获取记录列表:获取子管理员
     * @return
     */
    public CodeDataResp getList(AccountPageReq pageReq) {

        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("账号信息非法");
        Page<AccountEntity> page = MpPageUtil.getCommonPage(pageReq);
        QueryWrapper<AccountEntity> condition = new QueryWrapper<>();

        condition.eq("account_type", RoleType.CHILD_ADMIN);

        return CodeDataResp.valueOfSuccess( this.page(page,condition));

    }

    /**
     * 获取当前账号信息
     * @return
     */
    public CodeDataResp getCurrentAccount() {
        AccountEntity accountEntity = super.getById(ShiroUtil.getPrincipalUserId());
        if (accountEntity==null)
            return CodeDataResp.valueOfFailed("账号非法");
        return CodeDataResp.valueOfSuccess(accountEntity);

    }

    /**
     * 获取记录列表:获取子管理员
     * @return
     */
    public CodeDataResp getTenantList(AccountPageReq pageReq) {

        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("账号信息非法");
        Page<AccountEntity> page = MpPageUtil.getCommonPage(pageReq);
        QueryWrapper<AccountEntity> condition = new QueryWrapper<>();
//        condition.eq("account_type",AccountType.ACCOUNT_TYPE_ACCOUNT_ADMIN);
//        if (userDto.getAccountType()==AccountType.ACCOUNT_TYPE_AGENT_ADMIN){
//            condition.eq("parent_id", userDto.getId());
//
//        }
//        else if (userDto.getAccountType()==AccountType.ACCOUNT_TYPE_SYS_SUPERADMIN){
//            if (StrUtils.isVaileNum(pageReq.getParentId())){
//                condition.eq("parent_id", pageReq.getParentId());
//            }
//            else{
//                condition.ne("parent_id",0);
//            }
//        }
//        else
//            return CodeDataResp.valueOfSuccessEmptyData();

        return CodeDataResp.valueOfSuccess( this.page(page,condition));

    }


    /**
     * 获取记录列表:获取代理商或者代理商获取企业账号
     * @param pageReq
     * @return
     */
    public CodeDataResp getSysAccount(AccountPageReq pageReq) {

        UserDto userDto = ShiroUtil.getPrincipalUser();

        Page<AccountEntity> page = MpPageUtil.getCommonPage(pageReq);
        QueryWrapper<AccountEntity> condition = new QueryWrapper<>();


//        if (userDto.getAccountType()==AccountType.ACCOUNT_TYPE_AGENT_ADMIN){
//            condition.eq("account_type",AccountType.ACCOUNT_TYPE_ACCOUNT_ADMIN);
//            condition.eq("parent_id", userDto.getParentAccountId());
//        }
//        if (userDto.getAccountType()==AccountType.ACCOUNT_TYPE_SYS_SUPERADMIN){
//
//            condition.eq("account_type",AccountType.ACCOUNT_TYPE_AGENT_ADMIN);
//            //根据角色筛选
//            if (StrUtils.isVaileNum(pageReq.getRoleId())) {
//                condition.eq("role_id",pageReq.getRoleId());
//            }
//        }
//
//
//        //根据手机号筛选
//        if (!StrUtils.isTrimNull(pageReq.getKeyWord())) {
//            condition.like("account_name", pageReq.getKeyWord());
//
//        }
//        if (StrUtils.isVaileNum(pageReq.getStartTime())) {
//            condition.ge("create_time",pageReq.getStartTime());
//        }
//
//        if (StrUtils.isVaileNum(pageReq.getEndTime())) {
//            condition.le("end_time",pageReq.getEndTime());
//        }

        return CodeDataResp.valueOfSuccess( this.page(page, condition));

    }


    public CodeDataResp getAgentList() {
        UserDto userDto = ShiroUtil.getPrincipalUser();
        AccountEntity accountEntity = super.getById(userDto.getId());

//        if (accountEntity.getAccountType()==AccountType.ACCOUNT_TYPE_SYS_SUPERADMIN||(accountEntity.getAccountType()==AccountType.ACCOUNT_TYPE_CHILDADMIN&&accountEntity.getParentId()==0)){
//            QueryWrapper<AccountEntity> condition = new QueryWrapper<>();
//            condition.eq("account_type",AccountType.ACCOUNT_TYPE_AGENT_ADMIN).or().eq("account_type",AccountType.ACCOUNT_TYPE_SYS_SUPERADMIN);
//
//            return CodeDataResp.valueOfSuccess(super.list(condition));
//        }
//        else
            return CodeDataResp.valueOfSuccessEmptyData();



    }

    /**
     * 管理员登陆
     * @param userLoginReq
     * @return
     */
    public CodeDataResp login(LoginReq userLoginReq) {
        //检查验证码
//        if (!KaptchaUtil.checkVerifyCode(userLoginReq.getKaptcha(), userLoginReq.getKaptchaUUID()))
//            return CodeDataResp.valueOfFailed("验证码错误");

        AccountEntity account = getAccountByUserName(userLoginReq.getUsername());
        if (account == null) {
            return CodeDataResp.valueOfFailed("用户不存在");
        }

        if (!userLoginReq.getPassword().equals(account.getPassword())) {
            return CodeDataResp.valueOfFailed("用户名或密码错误");
        }
        if (account.getStatus() != 1) {
            return CodeDataResp.valueOfFailed("账号被禁用");
        }
        //获取token
        String token = shiroUserService.userLoginGetToken(userLoginReq, CommonConstant.LOGIN_TYPE_BACKSTAGE,account.getRoleId());
        AccountLoginDto accountLoginDto = new AccountLoginDto();
        BeanMapper.copy(account, accountLoginDto);
        accountLoginDto.setToken(token);
        return CodeDataResp.valueOfSuccess(accountLoginDto);
    }

    /**
     * 管理员注销登录
     * @return
     */
    public CodeDataResp logout() {
        shiroUserService.userLogout();
        return CodeDataResp.valueOfSuccess("注销成功");
    }

    /**
     * 保存或更新
     * @param
     * @return
     */
    @Override
    public CodeDataResp saveOrUpdate(AccountReq accountReq) {

        AccountEntity accountEntity = super.getById(ShiroUtil.getPrincipalUserId());
        if (accountEntity==null)
            return CodeDataResp.valueOfFailed("账号非法");

        RoleEntity role=roleService.getById(accountReq.getRoleId());
        if (role==null)
            return CodeDataResp.valueOfFailed("创建的角色不存在");

        //只能创建自己的下级账号类型
//        if (accountEntity.getAccountType()==AccountType.ACCOUNT_TYPE_AGENT_ADMIN){
//            if (role.getRoleType()!=AccountType.ACCOUNT_TYPE_ACCOUNT_ADMIN||(role.getAccountId()!=accountEntity.getId()))
//                return CodeDataResp.valueOfFailed("无权限创建该角色");
//        }
//
//        if (accountEntity.getAccountType()==AccountType.ACCOUNT_TYPE_ACCOUNT_ADMIN){
//            if (role.getRoleType()!=AccountType.ACCOUNT_TYPE_CHILDADMIN||(role.getAccountId()!=accountEntity.getId()))
//                return CodeDataResp.valueOfFailed("无权限创建该角色");
//        }

        AccountEntity account = getAccountByUserName(accountReq.getAccountLoginName());

        if (!StrUtils.isVaileNum(accountReq.getId())){

            if (account!=null)
                return CodeDataResp.valueOfFailed("已存在相同账号");
            //新增
            AccountEntity a = new AccountEntity();
            BeanMapper.copy(accountReq,a);
            a.setCreateTime(DateUtils.currentSecs());
            if (!StrUtils.isTrimNull(accountReq.getPassword()))
                a.setPassword(accountReq.getPassword());
            a.setRoleId(role.getId());
            a.setRoleName(role.getRoleName());
            a.setAccountType(role.getRoleType());

            super.save(a);

        }
        else {
            if (account!=null&&account.getId()!=accountReq.getId())
                return CodeDataResp.valueOfFailed("已存在相同账号");
            AccountEntity a = super.getById(accountReq.getId());
            BeanMapper.copy(accountReq,a);
            a.setRoleName(role.getRoleName());
            a.setAccountType(role.getRoleType());
            a.setUpdateTime(DateUtils.currentSecs());
            super.updateById(a);

//            if (a.getAccountType()==AccountType.ACCOUNT_TYPE_AGENT_ADMIN||a.getAccountType()==AccountType.ACCOUNT_TYPE_CHILDADMIN||a.getAccountType()==AccountType.ACCOUNT_TYPE_ACCOUNT_ADMIN)//给管理员同步创建用户信息
//                userService.createUser(a,role);
        }

        return CodeDataResp.valueOfSuccess("成功");
    }

    /**
     * 创建的用户为管理员角色 AccountType.ACCOUNT_TYPE_CHILDADMIN，
     * 则同步创建管理员登录账号
     * @param
     * @return
     */
    public AccountEntity createAccountFromUserInfo(UserEntity userEntity){

        AccountEntity accountEntity = new AccountEntity();

        accountEntity.setAccountType(userEntity.getUserType());
        accountEntity.setAccountCode(userEntity.getUserCode());
        accountEntity.setMobilePhone(userEntity.getMobilePhone());
        accountEntity.setAccountLoginName(userEntity.getMobilePhone());
        accountEntity.setAccountName(userEntity.getUserNick());
        accountEntity.setAccountLogo(userEntity.getUserAvatar());
        accountEntity.setMobilePhone(userEntity.getMobilePhone());

        accountEntity.setPassword(userEntity.getPassword());
        accountEntity.setStatus(userEntity.getStatus());
        accountEntity.setRoleId(userEntity.getRoleId());
        accountEntity.setRoleName(userEntity.getRoleName());

        accountEntity.setCreateTime(DateUtils.currentSecs());
        super.save(accountEntity);
        return accountEntity;
    }



    @Override
    public CodeDataResp deleteAcctount(BaseIdReq baseIdReq) {
        AccountEntity account = super.getById(baseIdReq.getId());
        if (account==null)
            return new CodeDataResp(State.CODE_FAILED,"删除账号不存在");
        super.removeById(account.getId());
        return CodeDataResp.valueOfSuccess("删除成功");
    }



    @Override
    public AccountEntity getAccountByPhone(String phone){
        QueryWrapper<AccountEntity> condition=new QueryWrapper<>();
        condition.eq("mobile_phone",phone);

        return super.getOne(condition);
    }

    public AccountEntity getAccountByUserName(String userName){
        QueryWrapper<AccountEntity> condition=new QueryWrapper<>();
        condition.eq("account_login_name",userName);

        return super.getOne(condition);
    }



}
