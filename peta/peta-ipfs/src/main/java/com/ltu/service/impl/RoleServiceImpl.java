package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.AccountType;
import com.ltu.constant.RoleType;
import com.ltu.domain.mp_entity.AccountEntity;
import com.ltu.domain.mp_entity.RoleEntity;

import com.ltu.mapper.AccountMapper;
import com.ltu.mapper.RoleMapper;
import com.ltu.mapper.RoleMenuMapper;
import com.ltu.mapper.UserMapper;
import com.ltu.model.request.account.RoleCommonReq;
import com.ltu.model.request.base.BaseIdReq;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.AccountService;
import com.ltu.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltu.util.BeanMapper;

import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 角色管理 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleEntity> implements RoleService {


    @Autowired
    AccountService accountService;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取记录列表:获取代理或者租户角色类型列表,
     * @param
     * @return
     */
    public CodeDataResp getList() {

        UserDto userDto = ShiroUtil.getPrincipalUser();
        AccountEntity accountEntity = accountService.getById(userDto.getId());
        QueryWrapper<RoleEntity> condition = new QueryWrapper<>();


        return CodeDataResp.valueOfSuccess(this.list(condition));
    }

    public CodeDataResp getChildRole(){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        QueryWrapper<RoleEntity> condition = new QueryWrapper<>();
            condition.eq("role_type", RoleType.CHILD_ADMIN);
            condition.eq("role_source_platform", RoleType.PLATFORM_BUILD);

        return CodeDataResp.valueOfSuccess(this.list(condition));

    }

    public CodeDataResp getSystemRoles(){
        UserDto userDto = ShiroUtil.getPrincipalUser();
        QueryWrapper<RoleEntity> condition = new QueryWrapper<>();
            condition.le("role_type", RoleType.USER);
            condition.ge("role_type", RoleType.WRITER);
            condition.eq("role_source_platform", RoleType.ADMIN_AUTH);

        return CodeDataResp.valueOfSuccess(this.list(condition));

    }


    /**
     * 保存或更新
     * @param req
     * @return
     */
    public CodeDataResp saveOrUpdate(RoleCommonReq req) {

        UserDto userDto = ShiroUtil.getPrincipalUser();
        if (userDto==null)
            return CodeDataResp.valueOfFailed("账号信息非法");
        if (StrUtils.isVaileNum(req.getId())){
            RoleEntity roleEntity = super.getById(req.getId());
            if (roleEntity==null)
                return CodeDataResp.valueOfFailed("数据非法");
            if (roleEntity.getRoleSourcePlatform()==RoleType.PLATFORM_BUILD){
                BeanMapper.copy(req,roleEntity);
                roleEntity.setRoleCode(req.getRoleCode());
                roleEntity.setRoleName(req.getRoleName());
                roleEntity.setStatus(req.getStatus());
                roleEntity.setRemark(req.getRemark());
                roleEntity.setUpdateTime(DateUtils.currentSecs());

                super.saveOrUpdate(roleEntity);
            }
        }
        else {
            RoleEntity roleEntity = new RoleEntity();
            BeanMapper.copy(req,roleEntity);
            roleEntity.setAccountId(userDto.getId());
            roleEntity.setCreateTime(DateUtils.currentSecs());
            roleEntity.setRoleType(RoleType.CHILD_ADMIN);
            roleEntity.setRoleSourcePlatform(2);

            super.save(roleEntity);

        }

        return CodeDataResp.valueOfSuccessEmptyData();
    }


    public RoleEntity getRoleByType(Integer roleType){
        QueryWrapper<RoleEntity> condition = new QueryWrapper<>();
        condition.eq("role_type", roleType);

        return super.getOne(condition);
    }

    /**
     * 获取记录详情
     * @param req
     * @return
     */
    public CodeDataResp getDetail(BaseIdReq req){


        return CodeDataResp.valueOfSuccessEmptyData();
    }


    /**
     * 删除记录
     * @param req
     * @return
     */
    @Transactional
    public CodeDataResp remove(BaseIdReq req) {
        RoleEntity roleEntity = this.getById(req.getId());
        if (roleEntity==null)
            return CodeDataResp.valueOfFailed("角色不存在");
        if (roleEntity!=null&&roleEntity.getRoleSourcePlatform()==RoleType.ADMIN_AUTH)
            return CodeDataResp.valueOfFailed("系统角色不可删除");



            //删除角色权限、同步已有用户的角色信息
//            roleMenuMapper.deleteByRoleId(roleEntity.getId());
//
//            accountMapper.upAccountByRoleInfos(roleEntity.getId());
//
//            userMapper.upUserByRoleInfos(roleEntity.getId(),roleEntity.getAccountId());




        this.removeById(req.getId());
        return CodeDataResp.valueOfSuccessEmptyData();
    }
}
