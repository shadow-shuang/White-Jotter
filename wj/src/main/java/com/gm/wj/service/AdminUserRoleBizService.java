package com.gm.wj.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gm.wj.entity.AdminRole;
import com.gm.wj.entity.AdminUserRole;
import com.gm.wj.mapper.plus.AdminUserRolePlusMapper;
import com.gm.wj.service.plus.AdminUserRolePlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Evan
 * @date 2019/11
 */
@Service
public class AdminUserRoleBizService {

    @Autowired
    private AdminUserRolePlusService adminUserRolePlusService;

    public List<AdminUserRole> listAllByUid(int uid) {
        return adminUserRolePlusService.lambdaQuery()
                .eq(AdminUserRole::getUid, uid)
                .list();
    }

    @Transactional
    public void saveRoleChanges(int uid, List<AdminRole> roles) {
        boolean delete = adminUserRolePlusService.lambdaUpdate()
                .eq(AdminUserRole::getUid, uid)
                .remove();

        adminUserRolePlusService.lambdaUpdate()
                .eq(AdminUserRole::getUid, uid)
                .remove();

        List<AdminUserRole> adminUserRoles = new ArrayList<>();
        roles.forEach(r -> {
            AdminUserRole ur = new AdminUserRole();
            ur.setUid(uid);
            ur.setRid(r.getId());
            adminUserRoles.add(ur);
        });
        adminUserRolePlusService.saveBatch(adminUserRoles);
    }
}
