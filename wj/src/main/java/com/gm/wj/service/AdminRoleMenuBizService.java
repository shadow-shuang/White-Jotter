package com.gm.wj.service;

import com.gm.wj.entity.AdminRoleMenu;
import com.gm.wj.service.plus.AdminRoleMenuPlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Evan
 * @date 2019/11
 */
@Service
public class AdminRoleMenuBizService {

    @Autowired
    private AdminRoleMenuPlusService adminRoleMenuPlusService;

    public List<AdminRoleMenu> findAllByRid(int rid) {
        return adminRoleMenuPlusService.lambdaQuery()
                .eq(AdminRoleMenu::getRid, rid)
                .list();
    }

    public List<AdminRoleMenu> findAllByRid(List<Integer> rids) {
        return adminRoleMenuPlusService.lambdaQuery()
                .in(AdminRoleMenu::getRid, rids)
                .list();
    }

    public void save(AdminRoleMenu adminRoleMenu) {
        adminRoleMenuPlusService.save(adminRoleMenu);
    }

    @Modifying
    @Transactional
    public void updateRoleMenu(int rid, Map<String, List<Integer>> menusIds) {

        boolean remove = adminRoleMenuPlusService.lambdaUpdate().eq(AdminRoleMenu::getRid, rid).remove();

        List<AdminRoleMenu> adminRoleMenus = new ArrayList<>();
        for (Integer mid : menusIds.get("menusIds")) {
            AdminRoleMenu rm = new AdminRoleMenu();
            rm.setMid(mid);
            rm.setRid(rid);
            adminRoleMenus.add(rm);
        }

        adminRoleMenuPlusService.saveBatch(adminRoleMenus);
    }
}
