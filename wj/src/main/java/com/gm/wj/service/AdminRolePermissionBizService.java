package com.gm.wj.service;

import com.gm.wj.entity.AdminPermission;
import com.gm.wj.entity.AdminRolePermission;
import com.gm.wj.service.plus.AdminRolePermissionPlusService;
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
public class AdminRolePermissionBizService {

    @Autowired
    private AdminRolePermissionPlusService adminRolePermissionPlusService;

    public List<AdminRolePermission> findAllByRid(int rid) {
        return adminRolePermissionPlusService.lambdaQuery()
                .eq(AdminRolePermission::getRid, rid).list();
    }

    @Transactional
    public void savePermChanges(int rid, List<AdminPermission> perms) {
        boolean remove = adminRolePermissionPlusService.lambdaUpdate()
                .eq(AdminRolePermission::getRid, rid)
                .remove();

        List<AdminRolePermission> rps = new ArrayList<>();
        perms.forEach(p -> {
            AdminRolePermission rp = new AdminRolePermission();
            rp.setRid(rid);
            rp.setPid(p.getId());
            rps.add(rp);
        });
        adminRolePermissionPlusService.saveBatch(rps);
    }
}
