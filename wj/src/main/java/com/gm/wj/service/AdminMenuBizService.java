package com.gm.wj.service;

import com.gm.wj.entity.AdminMenu;
import com.gm.wj.entity.AdminRoleMenu;
import com.gm.wj.entity.AdminUserRole;
import com.gm.wj.entity.User;
import com.gm.wj.service.plus.AdminMenuPlusService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Evan
 * @date 2019/10
 */
@Service
public class AdminMenuBizService {
    @Autowired
    private AdminMenuPlusService adminMenuPlusService;
    @Autowired
    private UserBizService userBizService;
    @Autowired
    private AdminUserRoleBizService adminUserRoleBizService;
    @Autowired
    private AdminRoleMenuBizService adminRoleMenuBizService;

    public List<AdminMenu> getAllByParentId(int parentId) {
        return adminMenuPlusService.lambdaQuery()
                .eq(AdminMenu::getParentId, parentId)
                .list();
    }

    public List<AdminMenu> getMenusByCurrentUser() {
        // Get current user in DB.
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userBizService.findByUsername(username);

        // Get roles' ids of current user.
        List<Integer> rids = adminUserRoleBizService.listAllByUid(user.getId())
                .stream().map(AdminUserRole::getRid).collect(Collectors.toList());

        // Get menu items of these roles.
        List<Integer> menuIds = adminRoleMenuBizService.findAllByRid(rids)
                .stream().map(AdminRoleMenu::getMid).collect(Collectors.toList());

        List<AdminMenu> menus = adminMenuPlusService.listByIds(menuIds).stream().distinct().collect(Collectors.toList());

        // Adjust the structure of the menu.
        handleMenus(menus);
        return menus;
    }

    public List<AdminMenu> getMenusByRoleId(int rid) {
        List<Integer> menuIds = adminRoleMenuBizService.findAllByRid(rid)
                .stream().map(AdminRoleMenu::getMid).collect(Collectors.toList());

        List<AdminMenu> menus = adminMenuPlusService.listByIds(menuIds);
        handleMenus(menus);

        return menus;
    }

    /**
     * Adjust the Structure of the menu.
     *
     * @param menus Menu items list without structure
     */
    public void handleMenus(List<AdminMenu> menus) {
        menus.forEach(m -> {
            List<AdminMenu> children = getAllByParentId(m.getId());
            m.setChildren(children);
        });

        menus.removeIf(m -> m.getParentId() != 0);
    }
}
