package com.ltu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ltu.config.shiro.ShiroUtil;
import com.ltu.config.shiro.dto.UserDto;
import com.ltu.domain.mp_entity.MenuEntity;
import com.ltu.mapper.MenuMapper;
import com.ltu.mapper.RoleMenuMapper;
import com.ltu.model.request.account.MenuReq;
import com.ltu.model.request.base.BaseIdReq;

import com.ltu.model.response.MenuResp;
import com.ltu.model.response.base.CodeDataResp;
import com.ltu.service.MenuService;
import com.ltu.service.RoleMenuService;
import com.ltu.util.BeanMapper;
import com.ltu.util.common.StrUtils;
import com.ltu.util.datetime.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author 若尘
 * @since 2021-11-26
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuEntity> implements MenuService {


    @Autowired
    RoleMenuService roleMenuService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 获取记录列表
     * @param
     * @return
     */
    public CodeDataResp getMenuList() {

        QueryWrapper<MenuEntity> condition = new QueryWrapper<>();
        condition.eq("status",1);

        List<MenuEntity> list = this.list(condition);
        List<MenuResp> menuReqs = new ArrayList<>();
        if (list.size()>0){
            List<MenuEntity> levelOne = list.stream().filter(t->t.getMenuLevel()==1).collect(Collectors.toList());
            levelOne =levelOne.stream().sorted((a, b) -> a.getSort() - b.getSort()).collect(Collectors.toList());
            if (levelOne.size()>0){
                for (MenuEntity menuEntity:levelOne){
                    MenuResp menuReq = new MenuResp();
                    BeanMapper.copy(menuEntity,menuReq);
                    List<MenuResp> menuResps = getNextLevel(list,menuEntity.getId());
                    menuReq.setChildrenList(menuResps.size()>0?menuResps:null);

                    menuReqs.add(menuReq);

                }
            }
        }

        return CodeDataResp.valueOfSuccess(menuReqs);
    }

    private List<MenuResp> getNextLevel(List<MenuEntity> list,Integer parentId){
        List<MenuResp> menuResps = new ArrayList<>();
        List<MenuEntity> menuEntityList = list.stream().filter(t->t.getParentMenuId()==parentId).collect(Collectors.toList());
        if (menuEntityList.size()>0){
            for (MenuEntity menuEntity:menuEntityList){
                MenuResp menuReq = new MenuResp();
                BeanMapper.copy(menuEntity,menuReq);
                menuReq.setParentMenuId(parentId);
                menuResps.add(menuReq);
            }
        }

        return menuResps;
    }



    public List<MenuEntity> getList(){
        QueryWrapper<MenuEntity> condition = new QueryWrapper<>();
        condition.eq("status",1);

        List<MenuEntity> list = this.list(condition);

        return list;
    }

    public List<MenuEntity> getList(List<Integer> ids){
        QueryWrapper<MenuEntity> condition = new QueryWrapper<>();
        condition.in("id",ids);
        List<MenuEntity> list = this.list(condition);

        return list;
    }




    /**
     * 保存或更新
     * @param req
     * @return
     */
    public CodeDataResp saveOrUpdate(MenuReq req) {
        UserDto userDto = ShiroUtil.getPrincipalUser();
//        if (userDto.getAccountType()!= AccountType.ACCOUNT_TYPE_SYS_SUPERADMIN)
//            return CodeDataResp.valueOfFailed("无权限编辑");
        if (StrUtils.isVaileNum(req.getId())){
            MenuEntity menuEntity = this.getById(req.getId());
            if (menuEntity==null)
                return CodeDataResp.valueOfFailed("菜单数据非法");

            BeanMapper.copy(req,menuEntity);
            menuEntity.setUpdateTime(DateUtils.currentSecs());

            menuEntity= getMenuLevel(req.getParentMenuId(),menuEntity);

            this.saveOrUpdate(menuEntity);

        }
        else {
            MenuEntity menuEntity = new MenuEntity();
            BeanMapper.copy(req,menuEntity);
            menuEntity.setCreateTime(DateUtils.currentSecs());
            menuEntity= getMenuLevel(req.getParentMenuId(),menuEntity);

            this.saveOrUpdate(menuEntity);

            roleMenuService.createNewMenus(menuEntity,userDto);
        }

        return CodeDataResp.valueOfSuccessEmptyData();
    }


    private MenuEntity getMenuLevel(Integer parentId,MenuEntity menuEntity){
        if (StrUtils.isVaileNum(parentId)){
            MenuEntity parentMenu = this.getById(parentId);
            if (parentMenu!=null){
                menuEntity.setParentMenuId(parentMenu.getId());
                menuEntity.setMenuLevel(parentMenu.getMenuLevel()+1);
            }
        }
        else {
            menuEntity.setParentMenuId(0);
            menuEntity.setMenuLevel(1);
        }
        return menuEntity;
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
        if (StrUtils.isVaileNum(req.getId())){
            MenuEntity menuEntity = super.getById(req.getId());
            if (menuEntity!=null){
                super.removeById(req.getId());
//                menuMapper.upLevelTwoMenu(menuEntity.getId());
//                //删除角色对应的菜单
//                roleMenuMapper.deleteByMenuId(menuEntity.getId());
            }
        }
        return CodeDataResp.valueOfSuccessEmptyData();
    }
}
