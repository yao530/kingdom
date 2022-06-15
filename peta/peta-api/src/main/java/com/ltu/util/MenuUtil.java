package com.ltu.util;

import com.ltu.model.response.MenuResp;

import java.util.ArrayList;
import java.util.List;

public class MenuUtil {
//    /**
//     * 将一二级菜单传进去，可获得分组好的菜单
//     * @param listL1
//     * @param listL2
//     * @return
//     */
//    public static List<MenuResp> getGroupedMenuList(List<Menu> listL1, List<Menu> listL2, List<Menu> listL3) {
//        List<MenuResp> resultList = new ArrayList<>();
//        for (Menu itemL1 : listL1) {
//            MenuResp m = new MenuResp();
//            BeanMapper.copy(itemL1, m);
//            resultList.add(m);
//        }
//
//        for (MenuResp menuResp : resultList) {
//            List<MenuResp> childrenListL2 = new ArrayList<>();
//            for (Menu itemL2 : listL2) {
//                if(itemL2.getParentId().equals(menuResp.getId())){
//                    MenuResp mL2 = new MenuResp();
//                    BeanMapper.copy(itemL2, mL2);
//                    List<MenuResp> childrenListL3 = new ArrayList<>();
//                    for (Menu itemL3 : listL3) {
//                        if (itemL3.getParentId().equals(mL2.getId())) {
//                            MenuResp mL3 = new MenuResp();
//                            BeanMapper.copy(itemL3, mL3);
//                            childrenListL3.add(mL3);
//                        }
//                    }
//                    mL2.setChildrenList(childrenListL3);
//                    childrenListL2.add(mL2);
//                }
//            }
//            menuResp.setChildrenList(childrenListL2);
//        }
//        return resultList;
//    }
//
//    /**
//     * 将整个menu list传进去可获得按一二级分组好的菜单
//     * @param originList
//     * @return
//     */
//    public static List<MenuResp> getGroupedMenuList(List<Menu> originList) {
//        List<Menu> listL1 = new ArrayList<>();
//        List<Menu> listL2 = new ArrayList<>();
//        List<Menu> listL3 = new ArrayList<>();
//        for (Menu menu : originList) {
//            if (menu.getLevel() == 1) {
//                listL1.add(menu);
//            }
//            else if (menu.getLevel() == 2) {
//                listL2.add(menu);
//            }
//            else if (menu.getLevel() == 3) {
//                listL3.add(menu);
//            }
//        }
//        return MenuUtil.getGroupedMenuList(listL1, listL2, listL3);
//    }
}
