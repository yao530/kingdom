package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.RoleType;
import com.ltu.domain.mp_entity.*;
import com.ltu.mapper.ApplyCreatorMapper;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.request.base.PageReqData;
import com.ltu.model.request.character.ApplyCreatorReq;
import com.ltu.model.request.character.CheckCreatorReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.*;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.MpPageUtil;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 创作者申请 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2022-04-16
 */
@Service
public class ApplyCreatorServiceImpl extends BaseServiceImpl<ApplyCreatorMapper, ApplyCreatorEntity> implements ApplyCreatorService {


    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @Autowired
    AccountService accountService;

    @Autowired
    CharacterRoleService characterRoleService;

    @Autowired
    CreatorApplySettingService applySettingService;

    @Autowired
    VirtualCharacterService virtualCharacterService;


    public CodeDataResp saveOrUpdate(ApplyCreatorReq applyCreatorReq){

        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        UserEntity userEntity = userService.getById(userDto.getId());
        if (userEntity==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        if(userEntity.getIdentityStatus()==0)
            return CodeDataResp.valueOfFailed("需完成实名认证");
        if (userEntity.getRoleId()!= RoleType.USER)
            return CodeDataResp.valueOfFailed("你已是创作者");
        CreatorApplySettingEntity settingEntity = applySettingService.getById(applyCreatorReq.getApplySettingId());
        if (settingEntity==null||(settingEntity!=null&&settingEntity.getStatus()==0))
            return CodeDataResp.valueOfFailed("招募活动无效");
        CharacterRoleEntity roleEntity = characterRoleService.getById(applyCreatorReq.getCharacterRoleId());
        if (roleEntity==null)
            return CodeDataResp.valueOfFailed("角色数据非法");

        ApplyCreatorEntity applyCreatorEntity = getApplyCreator(userEntity.getId(),settingEntity.getId());
        if (applyCreatorEntity!=null)
            return CodeDataResp.valueOfFailed("你已申请招募");

        applyCreatorEntity = new ApplyCreatorEntity();
        BeanMapper.copy(applyCreatorReq,applyCreatorEntity);
        applyCreatorEntity.setUserId(userEntity.getId());
        applyCreatorEntity.setUserAvatar(userEntity.getUserAvatar());
        applyCreatorEntity.setUserNick(userEntity.getUserNick());
        applyCreatorEntity.setMobilePhone(userEntity.getMobilePhone());
        applyCreatorEntity.setCharacterRoleId(roleEntity.getId());
        applyCreatorEntity.setCharacterRoleName(roleEntity.getCharacterRoleName());
        applyCreatorEntity.setCreateTime(DateUtils.currentSecs());
        applyCreatorEntity.setApproveTime(DateUtils.currentSecs());
        applyCreatorEntity.setApplySettingId(settingEntity.getId());
        applyCreatorEntity.setApplySettingName(settingEntity.getSettingName());


        super.saveOrUpdate(applyCreatorEntity);


        return CodeDataResp.valueOfSuccessEmptyData();
    }



    public CodeDataResp checkApplyCreator(CheckCreatorReq req){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        ApplyCreatorEntity applyCreatorEntity = super.getById(req.getId());
        if (applyCreatorEntity==null)
            return CodeDataResp.valueOfFailed("数据非法");
        if (applyCreatorEntity.getStatus()!=1)
            return CodeDataResp.valueOfFailed("已审核过");

        UserEntity userEntity = userService.getById(applyCreatorEntity.getUserId());
        if (userEntity==null)
            return CodeDataResp.valueOfFailed("用户数据非法");
        CharacterRoleEntity characterRoleEntity = characterRoleService.getById(req.getCharacterRoleId());
        if (characterRoleEntity==null)
            return CodeDataResp.valueOfFailed("角色数据非法");

        BeanMapper.copy(req,applyCreatorEntity);
        applyCreatorEntity.setCharacterRoleName(characterRoleEntity.getCharacterRoleName());
        applyCreatorEntity.setCharacterRoleId(characterRoleEntity.getId());
        applyCreatorEntity.setLastCheckTime(DateUtils.currentSecs());

        super.saveOrUpdate(applyCreatorEntity);
        //添加管理员账号
        if (req.getStatus().intValue()==2){
            if (StrUtils.isVaileNum(characterRoleEntity.getRoleId())){
                RoleEntity roleEntity = roleService.getById(characterRoleEntity.getRoleId());
                userEntity.setRoleName(roleEntity.getRoleName());
                userEntity.setRoleId(roleEntity.getId());
                userEntity.setUserType(roleEntity.getRoleType());
                userService.saveOrUpdate(userEntity);
                accountService.createAccountFromUserInfo(userEntity);
            }
            virtualCharacterService.createVirtualCharacter(userEntity,applyCreatorEntity,req.getMetaId());

        }


        return CodeDataResp.valueOfSuccessEmptyData();



    }


    public CodeDataResp getPage(PageReqData pageReqData){
        Page<ApplyCreatorEntity> page = MpPageUtil.getCommonPage(pageReqData);
        QueryWrapper<ApplyCreatorEntity> condition = new QueryWrapper<>();

        return CodeDataResp.valueOfSuccess(super.page(page,condition));

    }


    private ApplyCreatorEntity getApplyCreator(Integer userId,Integer settingId){
        QueryWrapper<ApplyCreatorEntity> condition = new QueryWrapper<>();

        condition.eq("user_id",userId);
        condition.eq("apply_setting_id",settingId);
        condition.eq("status","1,2");


        return super.getOne(condition);

    }

    public CodeDataResp getUserApply(BaseIdReq req){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("用户信息非法");
        UserEntity userEntity = userService.getById(userDto.getId());
        if (userEntity==null)
            return CodeDataResp.valueOfFailed("用户信息非法");
        return CodeDataResp.valueOfSuccess(getApplyCreator(userEntity.getId(), req.getId()));
    }


}
