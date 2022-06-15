package com.ltu.service.impl;

import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.RoleType;
import com.ltu.domain.mp_entity.RoleEntity;
import com.ltu.domain.mp_entity.UserBlockchainInfoEntity;
import com.ltu.domain.mp_entity.UserEntity;
import com.ltu.domain.mp_entity.UserInfoEntity;
import com.ltu.model.response.CodeResp;
import com.ltu.service.*;
import com.ltu.util.security.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Objects;
import com.ltu.config.WxMaConfiguration;
import com.ltu.config.shiro.ShiroUserService;
import com.ltu.config.shiro.dto.LoginReq;
import com.ltu.config.shiro.dto.SmsCodeLoginReq;
import com.ltu.constant.CommonConstant;
import com.ltu.mapper.UserMapper;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.smscode.VerifySmsCodeReq;
import com.ltu.model.request.user.UpdateUserInfoReq;
import com.ltu.model.request.user.UserForgotPassword;
import com.ltu.model.request.user.UserPageReq;
import com.ltu.model.request.user.UserRegisterReq;
import com.ltu.model.request.user.UserResetPasswordReq;

import com.ltu.model.response.base.CodeDataResp;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;


import java.util.List;
import java.util.Random;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vk@rongding
 * @since 2020-08-15
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    private final ShiroUserService shiroUserService;

    private final VerifySmsCodeService verifySmsCodeService;
    @Value("${wx.miniapp.appid}")
    private String miniAppid;

    @Autowired
    RoleService roleService;

    @Autowired
    ContractSquService contractSquService;
    @Autowired
    UserInfoService userInfoService;



    public CodeDataResp testOkex() throws Exception {
//        UserDto userDto = ShiroUtil.getPrincipalUser();
//        UserEntity userEntity = super.getById(userDto.getId());
//        userBlockchainInfoService.saveOrUpdate(userEntity);




//        try {
//            String methodName = "mint";
//            List<Type> inputParameters = new ArrayList<>();
//            List<TypeReference<?>> outputParameters = new ArrayList<>();
//
//            Address tAddress = new Address("0x7ebb2594de6902FdbAEB626fcD63Ff4b2bB0FDC9");
//
//            Uint256 value = new Uint256(10);
//            inputParameters.add(tAddress);
//            inputParameters.add(value);
//
//            TypeReference<Bool> typeReference = new TypeReference<Bool>() {
//            };
//            outputParameters.add(typeReference);
//
//            blockchainPubService.getTokenBalance("0x7ebb2594de6902FdbAEB626fcD63Ff4b2bB0FDC9","0x1C24e329Ff7F56cDc17e237A68f431eb16Fa4275");
//            blockchainPubService.getTokenName("0x1C24e329Ff7F56cDc17e237A68f431eb16Fa4275");
//
//            TransactionReceipt receipt=  blockchainPubService.sendContractMethmod("0x1C24e329Ff7F56cDc17e237A68f431eb16Fa4275",methodName,inputParameters,outputParameters,"8916523ebdf889a744485baf7bccc4dbd897ebb3c45ae8290fbd090c32433467",true);
//
//            return CodeDataResp.valueOfSuccess(receipt);
//
//        }catch (Exception e){
//            throw new Exception(e);
//        }
//
        return CodeDataResp.valueOfSuccessEmptyData();

    }



    /**
     * 用户登陆
     * @param userLoginReq
     * @return
     */
    public CodeDataResp login(LoginReq userLoginReq) {
        UserEntity user = getUser(userLoginReq.getUsername());
        if (user == null) {
            return CodeDataResp.valueOfFailed("用户不存在");
        }
        if (!userLoginReq.getPassword().equals(user.getPassword())) {
            return CodeDataResp.valueOfFailed("用户名或密码错误");
        }
        if (user.getStatus() != 1) {
            return CodeDataResp.valueOfFailed("用户被禁用");
        }

        return userLoginGetToken(userLoginReq,user);
    }

    private CodeDataResp userLoginGetToken(LoginReq userLoginReq,UserEntity user){
        //初步验证成功
        String token = shiroUserService.userLoginGetToken(userLoginReq, CommonConstant.LOGIN_TYPE_FRONT,user.getRoleId());
        user.setLoginCount(user.getLoginCount()+1);
        user.setLastLoginTime(DateUtils.currentSecs());
        user.setToken(token);
        super.updateById(user);
        return CodeDataResp.valueOfSuccess(user);
    }

    /**
     * 短信验证码登陆
     * @param userLoginReq
     * @return
     */
    public CodeDataResp loginBySmsCode(SmsCodeLoginReq userLoginReq) {
        VerifySmsCodeReq verifySmsCodeReq = new VerifySmsCodeReq();
        verifySmsCodeReq.setSmsCode(userLoginReq.getSmsCode());
        verifySmsCodeReq.setUserPhone(userLoginReq.getMobilePhone());
//		if (!verifySmsCodeService.verifySmsCode(verifySmsCodeReq) &&  !Objects.equal(userLoginReq.getSmsCode(), "1234") )
//        	return CodeDataResp.valueOfFailed("短信验证码错误");

        UserEntity user = getUserByPhone(userLoginReq.getMobilePhone());
        if (user == null) {
            //用户不存在，执行注册
            UserRegisterReq userRegisterReq = new UserRegisterReq();
            userRegisterReq.setUserPhone(userLoginReq.getMobilePhone());
            userRegisterReq.setPassword(Md5Util.encodePassword("123456"));
            userRegisterReq.setSmsCode(userLoginReq.getSmsCode());
            CodeDataResp<UserEntity> resp=    register(userRegisterReq);
            //getWxUserOpendId(resp.getData(),userLoginReq);
           return	resp;
        }
        else {
            //用户已存在，执行登陆
            LoginReq loginReq = new LoginReq();
            loginReq.setUsername(user.getMobilePhone());
            loginReq.setPassword(user.getPassword());
            if(StringUtils.isBlank( user.getOpenId()) && StringUtils.isNotBlank( userLoginReq.getWxCode()))
            	getWxUserOpendId(user,userLoginReq);
            return  userLoginGetToken(loginReq,user);
        }
        //检查用户头像昵称openId  存在不用重新获取code 不存在则去获取
    }
    
    /**
     * 获取微信openId
     * */
	private CodeDataResp getWxUserOpendId( UserEntity user ,SmsCodeLoginReq userLoginReq ) {
		if (StringUtils.isBlank(userLoginReq.getWxCode()))
			  return CodeDataResp.valueOfFailed("授权code不能为空");  

		final WxMaService wxService = WxMaConfiguration.getMaService(miniAppid);
		try {
			WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(userLoginReq.getWxCode());
			log.info("微信用户的 openId:{}", session.getOpenid());
			// 涉及到用户需要补全信息，打开注释即可
				user.setUserNick(userLoginReq.getUserNick());
				user.setUserAvatar(userLoginReq.getUserAvatar());
				user.setOpenId(session.getOpenid());
				this.updateById(user);
		} catch (WxErrorException e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return CodeDataResp.valueOfSuccess(user);
	}

    /**
     * 注销登录
     * @return
     */
    public CodeDataResp logout() {
        shiroUserService.userLogout();
        return CodeDataResp.valueOfSuccess("注销成功");
    }

    /**
     * 用户注册
     * @param userRegisterReq
     * @return
     */
    private CodeDataResp register(UserRegisterReq userRegisterReq) {
        UserEntity user = getUserByPhone(userRegisterReq.getUserPhone());
//        if (user != null)
//            return CodeDataResp.valueOfFailed("用户已存在");
////
//        VerifySmsCodeReq verifySmsCodeReq = new VerifySmsCodeReq();
//        verifySmsCodeReq.setSmsCode(userRegisterReq.getSmsCode());
//        verifySmsCodeReq.setUserPhone(userRegisterReq.getUserPhone());
//
//        if (!verifySmsCodeService.verifySmsCode(verifySmsCodeReq))
//            return CodeDataResp.valueOfFailed("短信验证码错误");

//        StringBuilder sb = new StringBuilder("");
//        sb.append(DateUtils.format(currentDate, DateUtils.DATE_TIME_FORMAT_NUMBER));
       String userCode = StrUtils.randomnum(10000, 99999)+"";

        user = new UserEntity();
        BeanMapper.copy(userRegisterReq, user);
        	user.setCreateTime(DateUtils.currentSecs())
                .setLastLoginTime(DateUtils.currentSecs())
                .setStatus(1)
                .setLoginCount(1)
                .setUserType(1)
                    .setRoleId(5)
                    .setRoleName("普通用户")
                    .setUserLoginAccount(userRegisterReq.getUserPhone())
                .setMobilePhone(userRegisterReq.getUserPhone())
                    .setUserCode(userCode)
                    .setUserNick("王国公民_"+userCode);
        super.save(user);
        LoginReq loginReq = new LoginReq();
        loginReq.setUsername(user.getMobilePhone());
        loginReq.setPassword(user.getPassword());
        return userLoginGetToken(loginReq,user);
    }

    /**
     * 重置密码
     * @param req
     * @return
     */
    public CodeDataResp resetPassword(UserResetPasswordReq req) {
        UserEntity user = super.getById(req.getUserId());
        if (user == null)
            return CodeDataResp.valueOfErrorNoRecord();
        if (!user.getPassword().equals(req.getOldPassword()))
            return CodeDataResp.valueOfFailed("原密码输入错误");
        user.setPassword(req.getNewPassword());
        super.updateById(user);
        return CodeDataResp.valueOfSuccessEmptyData();
    }

    /**
     * 忘记密码
     * @param req
     * @return
     */
    public CodeDataResp forgotPassword(UserForgotPassword req) {
        VerifySmsCodeReq verifySmsCodeReq = new VerifySmsCodeReq();
        verifySmsCodeReq.setSmsCode(req.getSmsCode());
        verifySmsCodeReq.setUserPhone(req.getUserPhone());
        if (!verifySmsCodeService.verifySmsCode(verifySmsCodeReq))
            return CodeDataResp.valueOfFailed("短信验证码错误");
        UserEntity user = getUserByPhone(req.getUserPhone());
        if (user == null) {
            return CodeDataResp.valueOfErrorNoRecord();
        }
        user.setPassword(req.getPassword());
        super.updateById(user);

        LoginReq loginReq = new LoginReq();
        loginReq.setUsername(user.getMobilePhone());
        loginReq.setPassword(user.getPassword());
        return login(loginReq);
    }

    /**
     * 获取用户详细信息
     * @param baseIdReq
     * @return
     */
    public CodeDataResp getUserInfo(BaseIdReq baseIdReq) {
        return CodeDataResp.valueOfSuccess(super.getById(baseIdReq.getId()));
    }

    /**
     * 更新用户信息：此处需要先验证手机号短信验证码
     * @param userReq
     * @return
     */
    public CodeDataResp updateUserInfo(UpdateUserInfoReq userReq) {
        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        UserEntity user =super.getById(userDto.getId());
        if (user==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        if (user.getIdentityStatus()==1)
            return CodeDataResp.valueOfFailed("已实名验证");
        if (!StrUtils.isTrimNull(userReq.getIdentityId())){
            UserInfoEntity infoEntity = userInfoService.getUserInfoByIdentityId(userReq.getIdentityId());
            if (infoEntity!=null)
                return CodeDataResp.valueOfFailed("身份ID已注册");
        }
        if (user!=null){

            if (!StrUtils.isTrimNull(userReq.getIdentityId())&&user.getIdentityStatus()==0){
                //此处需要验证身份证的真实性 队列验证
                user.setIdentityStatus(1);
                userInfoService.createrUserInfo(user,userReq);
            }

            super.updateById(user);

            contractSquService.createUserBlockchainInfo(user);

        }

        return CodeDataResp.valueOfSuccess(user);
    }

    /**
     * MP-获取用户列表
     * @param pageReq
     * @return
     */
    public CodeDataResp getList(UserPageReq pageReq) {
        Page<UserEntity> page = MpPageUtil.getCommonPage(pageReq);
        QueryWrapper<UserEntity> condition = new QueryWrapper<>();

        //根据title筛选
        if (!StrUtils.isTrimNull(pageReq.getNickname())) {
            condition.like("nickName",pageReq.getNickname());
        }

        //根据title筛选
        if (!StrUtils.isTrimNull(pageReq.getUserPhone())) {
            condition.like("mobile_phone",pageReq.getUserPhone());
        }


        //根据时间筛选
        //start
        if (StrUtils.isVaileNum(pageReq.getStartTime())) {
            condition.ge("create_time",pageReq.getStartTime());
        }
        //end
        if (StrUtils.isVaileNum(pageReq.getEndTime())) {
            condition.le("create_time",pageReq.getEndTime());
        }

        return CodeDataResp.valueOfSuccess(super.page(page, condition));
    }


    public List<UserEntity> getCreatorUsers(){

        QueryWrapper<UserEntity> condition = new QueryWrapper<>();
         condition.le("role_type", RoleType.PET_MASTER);
         condition.le("status",1);
         condition.le("identity_status",1);

        return super.list(condition);
    }

    /**
     * MP-获取用户完整信息
     * @param baseIdReq
     * @return
     */
    @Override
    public CodeDataResp getDetail(BaseIdReq baseIdReq) {
        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("账号非法");
        UserEntity userEntity = super.getById(userDto.getId());
        if (userEntity==null)
            return CodeDataResp.valueOfFailed("账号非法");
        userEntity.setUserBlockchainAddress(userEntity.getUserBlockchainAddress().substring(2));

        return CodeDataResp.valueOfSuccess(userEntity);
    }

    public CodeDataResp<UserEntity> registerByWxGzh(String openId){
        UserEntity userEntity = getUserByOpenId(openId);

        RoleEntity roleEntity = roleService.getRoleByType(RoleType.USER);
        //对封禁违规的账户处理
        if (userEntity!=null&&userEntity.getStatus()==0)
            return CodeDataResp.valueOfFailed("账号已被封禁");
        if (userEntity==null){
            userEntity = new UserEntity();
            userEntity.setUserLoginAccount(openId);
            userEntity.setOpenId(openId);
            userEntity.setPassword(StrUtils.getRandomNumberStr(6));
            userEntity.setRoleId(roleEntity.getId());
            userEntity.setRoleName(roleEntity.getRoleName());
            userEntity.setUserCode(StrUtils.getRandomNumberStr(10));
            userEntity.setCreateTime(DateUtils.currentSecs());
            userEntity.setStatus(1);

        }
        userEntity.setLoginCount(userEntity.getLoginCount()+1);
        userEntity.setLastLoginTime(DateUtils.currentSecs());
        super.saveOrUpdate(userEntity);
        LoginReq loginReq = new LoginReq();
        //初步验证成功
        String token = shiroUserService.userLoginGetToken(loginReq, CommonConstant.LOGIN_TYPE_FRONT,userEntity.getRoleId());
        userEntity.setToken(token);
        super.updateById(userEntity);
        return CodeDataResp.valueOfSuccess(userEntity);

    }

    public UserEntity getUser(String userLoginName) {
        if (StrUtils.isTrimNull(userLoginName))
            return null;
        QueryWrapper<UserEntity> condition = new QueryWrapper<>();
        condition.eq("user_login_account",userLoginName);
        return super.getOne(condition);
    }


    /**
     * 通过手机号获取用户信息
     * @param userPhone
     * @return
     */
    public UserEntity getUserByPhone(String userPhone) {
        if (StrUtils.isTrimNull(userPhone))
            return null;
        QueryWrapper<UserEntity> condition = new QueryWrapper<>();
        condition.eq("mobile_phone",userPhone);
        return super.getOne(condition);
    }

    public UserEntity getUserByOpenId(String openid){
        QueryWrapper<UserEntity> condition = new QueryWrapper<>();
        condition.eq("openId",openid);
        return super.getOne(condition);
    }




}
