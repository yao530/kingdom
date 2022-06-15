package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.constant.AccountType;
import com.ltu.domain.mp_entity.AccountEntity;
import com.ltu.domain.mp_entity.MenuEntity;
import com.ltu.domain.mp_entity.RoleEntity;
import com.ltu.domain.mp_entity.RoleMenuEntity;
import com.ltu.mapper.RoleMenuMapper;
import com.ltu.model.request.account.EditRoleMenuReq;
import com.ltu.model.request.account.FindRoleMenuReq;
import com.ltu.model.request.account.RoleMenuReq;
import com.ltu.model.request.base.BaseIdReq;

import com.ltu.model.response.MenuResp;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.RoleMenuService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.apache.shiro.authc.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单管理 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuEntity> implements RoleMenuService {



    @Autowired
    MenuServiceImpl menuService;

    @Autowired
    AccountServiceImpl accountService;

    @Autowired
    RoleServiceImpl roleService;



    /**
     * 根据角色ID获取菜单
     * @param
     * @return
     */
    public CodeDataResp getList(FindRoleMenuReq roleMenuReq){
        AccountEntity accountEntity = accountService.getById(ShiroUtil.getPrincipalUserId());
        if (accountEntity==null)
            return CodeDataResp.valueOfFailed("账号非法");
//        if (accountEntity.getAccountType()==AccountType.ACCOUNT_TYPE_CHILDADMIN)
//            return CodeDataResp.valueOfFailed("该账号无权查询角色菜单权限");


        List<RoleMenuEntity> roleMenuEntityList = getRoleMenu(roleMenuReq.getRoleId(),0);
        List<RoleMenuEntity> parentRoleMenus = getRoleMenu(accountEntity.getRoleId(),0);

        if (parentRoleMenus.size()>0){
            for (RoleMenuEntity roleMenuEntity:parentRoleMenus){
                if (roleMenuEntityList.size()>0){
                    List<RoleMenuEntity> difRoleMenus =  roleMenuEntityList.stream().filter(t->t.getMenuId()==roleMenuEntity.getMenuId()).collect(Collectors.toList());
                    if (difRoleMenus.size()==0){//同步返回新菜单
                        roleMenuEntity.setAuthStatus(0);//默认不可见
                        roleMenuEntityList.add(roleMenuEntity);
                    }
                }
                else {//同步返回新菜单
                    roleMenuEntity.setAuthStatus(0);//默认不可见
                    roleMenuEntityList.add(roleMenuEntity);
                }

            }
        }
        return CodeDataResp.valueOfSuccess(buildRoleMenus(roleMenuEntityList));
    }

    /**
     * 获取记录列表
     * @param
     * @return
     */
    public CodeDataResp getList() {

        AccountEntity accountEntity = accountService.getById(ShiroUtil.getPrincipalUserId());
        if (accountEntity==null)
            return CodeDataResp.valueOfFailed("账号非法");

        if (!StrUtils.isVaileNum(accountEntity.getRoleId()))
            return CodeDataResp.valueOfFailed("无角色登录权限");
        RoleEntity roleEntity = roleService.getById(accountEntity.getRoleId());
        if (roleEntity==null)
            return CodeDataResp.valueOfFailed("无角色登录权限");
        List<RoleMenuEntity> roleMenuEntityList = getRoleMenu(accountEntity.getRoleId(),1);
//        if (accountEntity.getAccountType()==AccountType.ACCOUNT_TYPE_CHILDADMIN&&roleMenuEntityList.size()==0)
//            return CodeDataResp.valueOfFailed("该账号无权限管理菜单");

//
//        if (roleMenuEntityList.size()==0||)//找上级管理员权限
//        {
//            AccountEntity parentAccount = accountService.getAccountById(accountEntity.getParentId());
//            if (parentAccount!=null){
//                roleMenuEntityList = getRoleMenu(parentAccount.getRoleId(),parentAccount.getId());
//            }
//
//        }



        return CodeDataResp.valueOfSuccess(buildRoleMenus(roleMenuEntityList));
    }

    private List<MenuResp> buildRoleMenus(List<RoleMenuEntity> roleMenuEntityList){
        List<MenuResp> menuRespList = new ArrayList<>();
        if (roleMenuEntityList.size()>0){
            List<Integer> ids = roleMenuEntityList.stream().map(t->t.getMenuId()).collect(Collectors.toList());
            if (ids.size()>0){
                List<MenuEntity> menus = menuService.getList(ids);
                //构建菜单
                if (menus.size()>0){
                    List<MenuEntity> levelFirst =  menus.stream().filter(t->t.getMenuLevel()==1).collect(Collectors.toList());
                    levelFirst = levelFirst.stream().sorted((a, b) -> a.getSort() - b.getSort()).collect(Collectors.toList());
                    List<MenuResp> menuResps = buildRoleMenus(roleMenuEntityList,levelFirst);
                    if (menuResps.size()>0){
                        for (MenuResp menuResp:menuResps){
                            List<MenuEntity> levelSeconds =  menus.stream().filter(t->t.getMenuLevel()==2&&t.getParentMenuId()==menuResp.getMenuId()).collect(Collectors.toList());
                            List<MenuResp> twos = buildRoleMenus(roleMenuEntityList,levelSeconds);
                            menuResp.setChildrenList(twos.size()>0?twos:null);
                            menuRespList.add(menuResp);
                        }
                    }
                }
            }
        }

        return menuRespList;
    }


    private List<MenuResp> buildRoleMenus(List<RoleMenuEntity> roleMenuEntities,List<MenuEntity> entities){
        List<MenuResp> menuResps = new ArrayList<>();
        for (MenuEntity menuEntity:entities){
            List<RoleMenuEntity> list = roleMenuEntities.stream().filter(t->t.getMenuId()==menuEntity.getId()).collect(Collectors.toList());
            if (list.size()>0){
                MenuResp menuResp = new MenuResp();
                BeanMapper.copy(menuEntity,menuResp);
                //id与菜单id转换
                menuResp.setId(list.get(0).getId());
                menuResp.setMenuId(menuEntity.getId());
                menuResp.setAuthStatus(list.get(0).getAuthStatus());
                menuResps.add(menuResp);
            }
        }

        return menuResps;

    }

    /**
     * 根据菜单权限状态获取菜单，登录时获取可读和可读写的菜单，编辑角色权限时获取全部状态的
     * 0表示全部 1 表示可读写的
     * @param req
     * @return
     */
    private List<RoleMenuEntity> getRoleMenu(Integer roleId,Integer authStatus){
        QueryWrapper<RoleMenuEntity> condition = new QueryWrapper<>();
        condition.eq("role_id",roleId);
        //condition.eq("account_id",accountId);
        if (StrUtils.isVaileNum(authStatus))
            condition.ge("auth_status",1);//大于0的 表示
        return this.list(condition);
    }


    private List<RoleMenuEntity> getRoleMenu(List<Integer> ids){
        QueryWrapper<RoleMenuEntity> condition = new QueryWrapper<>();
        condition.in("id",ids);


        return super.list(condition);
    }


    /**
     * 保存或更新
     * @param req
     * @return
     */
    public CodeDataResp saveOrUpdate(RoleMenuReq roleMenuReq) {

        UserDto userDto = ShiroUtil.getPrincipalUser();

        RoleEntity roleEntity = roleService.getById(roleMenuReq.getRoleId());
        if (roleEntity==null)
            return CodeDataResp.valueOfFailed("角色不存在");

        List<EditRoleMenuReq> req = roleMenuReq.getRoleMenuReqs();
        List<RoleMenuEntity> roleMenuEntityList = getRoleMenu(roleMenuReq.getRoleId(),0);//判断该角色是否有菜单
        List<RoleMenuEntity> list = new ArrayList<>();
        if (req!=null&&req.size()>0){
            //获取父级全部菜单
            List<RoleMenuEntity> parentRoleMenu = getRoleMenu( userDto.getRoleId(),0);
            //未给该角色分配的权限
            List<RoleMenuEntity> newMenus =new ArrayList<>();
            if (parentRoleMenu.size()>0){
                for (RoleMenuEntity roleMenuEntity:parentRoleMenu){
                    if (roleMenuEntityList.size()>0){
                        List<RoleMenuEntity> difRoleMenus =  roleMenuEntityList.stream().filter(t->t.getMenuId()==roleMenuEntity.getMenuId()).collect(Collectors.toList());
                        if (difRoleMenus.size()==0){//同步返回新菜单

                            RoleMenuEntity newRoleMenu = new RoleMenuEntity();
                            newRoleMenu.setStatus(roleMenuEntity.getStatus());
                            newRoleMenu.setMenuId(roleMenuEntity.getMenuId());
                            newRoleMenu.setAccountId(roleMenuEntity.getAccountId());
                            newRoleMenu.setAuthStatus(roleMenuEntity.getAuthStatus());
                            newRoleMenu.setRoleId(roleMenuReq.getRoleId());
                            newRoleMenu.setSort(roleMenuEntity.getSort());
                            newRoleMenu.setRoutes(roleMenuEntity.getRoutes());
                            newMenus.add(newRoleMenu);
                        }
                    }
                    else {//同步返回新菜单
                        RoleMenuEntity newRoleMenu = new RoleMenuEntity();
                        newRoleMenu.setStatus(roleMenuEntity.getStatus());
                        newRoleMenu.setMenuId(roleMenuEntity.getMenuId());
                        newRoleMenu.setAccountId(roleMenuEntity.getAccountId());
                        newRoleMenu.setAuthStatus(roleMenuEntity.getAuthStatus());
                        newRoleMenu.setSort(roleMenuEntity.getSort());
                        newRoleMenu.setRoleId(roleMenuReq.getRoleId());

                        newMenus.add(newRoleMenu);
                    }
                }
            }

            //编辑角色新的菜单
            if (newMenus.size()>0)
                list.addAll(buildMenu(newMenus,req,roleMenuReq));

            if (roleMenuEntityList.size()>0)
                list.addAll(buildMenu(roleMenuEntityList,req,roleMenuReq));


            if (list.size()>0)
                this.saveOrUpdateBatch(list);


        }
        return CodeDataResp.valueOfSuccess(list);
    }

    private List<RoleMenuEntity>  buildMenu( List<RoleMenuEntity> roleMenuEntityList,List<EditRoleMenuReq> req,RoleMenuReq roleMenuReq){
        List<RoleMenuEntity> list = new ArrayList<>();

        if (roleMenuEntityList.size()>0){
            for (RoleMenuEntity roleMenuEntity:roleMenuEntityList){
                if (StrUtils.isVaileNum(roleMenuEntity.getId())){//修改菜单
                    List<EditRoleMenuReq> roleMenuReqList = req.stream().filter(t->t.getId().intValue()==roleMenuEntity.getId().intValue()).collect(Collectors.toList());
                    if (roleMenuReqList.size()>0){
                        roleMenuEntity.setAuthStatus(roleMenuReqList.get(0).getAuthStatus());
                        roleMenuEntity.setUpdateTime(DateUtils.currentSecs());
                        list.add(roleMenuEntity);
                    }
                }
                else {//新增
                    List<EditRoleMenuReq> menuReqs = req.stream().filter(t->t.getMenuId().intValue()==roleMenuEntity.getMenuId().intValue()).collect(Collectors.toList());
                    if (menuReqs.size()>0){
                        roleMenuEntity.setAuthStatus(menuReqs.get(0).getAuthStatus());
                        roleMenuEntity.setUpdateTime(DateUtils.currentSecs());
                        list.add(roleMenuEntity);
                    }
                }

            }
        }
        else {
            //从管理者菜单复制并新增
            List<Integer> ids = req.stream().map(t -> t.getId()).collect(Collectors.toList());
            List<RoleMenuEntity> menuEntityList = getRoleMenu(ids);
            if (menuEntityList.size()>0){
                for (int i=0;i<menuEntityList.size();i++){
                    RoleMenuEntity menuEntity = menuEntityList.get(i);
                    List<EditRoleMenuReq> roleMenuReqList = req.stream().filter(t->t.getMenuId().intValue()==menuEntity.getMenuId().intValue()).collect(Collectors.toList());
                    RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
                    roleMenuEntity.setCreateTime(DateUtils.currentSecs());
                    roleMenuEntity.setRoleId(roleMenuReq.getRoleId());
                    roleMenuEntity.setAccountId(roleMenuEntity.getAccountId());
                    roleMenuEntity.setSort(roleMenuEntity.getSort());
                    roleMenuEntity.setStatus(roleMenuEntity.getStatus());
                    roleMenuEntity.setMenuId(menuEntity.getMenuId());
                    roleMenuEntity.setSort(menuEntity.getSort());
                    if (roleMenuReqList.size()>0)
                        roleMenuEntity.setAuthStatus(roleMenuReqList.get(0).getAuthStatus());
                    else
                        roleMenuEntity.setAuthStatus(0);
                    list.add(roleMenuEntity);
                }
            }
        }
        return list;
    }


    /**
     * 创建原始菜单
     * @param accountEntity
     * @return
     */
    public CodeDataResp creatMenu(AccountEntity accountEntity) {

        List<RoleMenuEntity> roleMenuEntityList = getRoleMenu(accountEntity.getRoleId(),0);
        List<RoleMenuEntity> list = new ArrayList<>();
        if (roleMenuEntityList.size()>0){
            for (RoleMenuEntity roleMenuEntity:roleMenuEntityList){
                RoleMenuEntity entity = new RoleMenuEntity();
                entity.setAuthStatus(roleMenuEntity.getAuthStatus());
                entity.setAccountId(accountEntity.getId());
                entity.setCreateTime(DateUtils.currentSecs());
                entity.setRoleId(accountEntity.getRoleId());
                entity.setMenuId(roleMenuEntity.getMenuId());
                entity.setStatus(roleMenuEntity.getStatus());
                entity.setSort(roleMenuEntity.getSort());

                list.add(entity);
            }
        }
        if (list.size()>0)
            this.saveBatch(list);

        return CodeDataResp.valueOfSuccessEmptyData();
    }


    public void createNewMenus(MenuEntity menuEntity, UserDto userDto){
        RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
        roleMenuEntity.setRoleId(userDto.getRoleId());
        roleMenuEntity.setAccountId(userDto.getId());
        roleMenuEntity.setStatus(1);
        roleMenuEntity.setAuthStatus(2);
        roleMenuEntity.setCreateTime(DateUtils.currentSecs());
        roleMenuEntity.setMenuId(menuEntity.getId());

        super.save(roleMenuEntity);

    }

    /**
     * 获取记录详情
     * @param req
     * @return
     */
    public CodeDataResp getDetail(BaseIdReq req) {
        ///todo ...
        return CodeDataResp.valueOfSuccessEmptyData();
    }

    /**
     * 删除记录
     * @param req
     * @return
     */
    public CodeDataResp remove(BaseIdReq req) {
        ///todo ...
        return CodeDataResp.valueOfSuccessEmptyData();
    }
}
