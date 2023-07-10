package com.gm.wj.service;

import com.gm.wj.entity.AdminPermission;
import com.gm.wj.entity.AdminRole;
import com.gm.wj.entity.AdminRolePermission;
import com.gm.wj.service.plus.AdminPermissionPlusService;
import com.gm.wj.service.plus.AdminRolePermissionPlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Evan
 * @date 2019/11
 */
@Service
public class AdminPermissionBizService {

    @Autowired
    private AdminPermissionPlusService adminPermissionPlusService;
    @Autowired
    private AdminRoleBizService adminRoleBizService;
    @Autowired
    private AdminRolePermissionBizService adminRolePermissionBizService;

    @Autowired
    private AdminRolePermissionPlusService adminRolePermissionPlusService;

    public List<AdminPermission> list() {
        return adminPermissionPlusService.list();
    }

    /**
     * Determine whether client requires permission when requests
     * a certain API.
     * @param requestAPI API requested by client
     * @return true when requestAPI is found in the DB
     */
    public boolean needFilter(String requestAPI) {
        List<AdminPermission> adminPermissions = adminPermissionPlusService.list();
        for (AdminPermission adminPermission: adminPermissions) {
            // match prefix
            if (requestAPI.startsWith(adminPermission.getUrl())) {
                return true;
            }
        }
        return false;
    }

    public List<AdminPermission> listPermsByRoleId(int rid) {
        List<Integer> pids = adminRolePermissionBizService.findAllByRid(rid)
                .stream().map(AdminRolePermission::getPid).collect(Collectors.toList());
        return adminPermissionPlusService.listByIds(pids);
    }

    public Set<String> listPermissionURLsByUser(String username) {
        List<Integer> rids = adminRoleBizService.listRolesByUser(username)
                .stream().map(AdminRole::getId).collect(Collectors.toList());

        List<Integer> pids = adminRolePermissionPlusService.lambdaQuery()
                .in(AdminRolePermission::getRid, rids)
                .list()
                .stream().map(AdminRolePermission::getPid)
                .collect(Collectors.toList());

        List<AdminPermission> adminPermissions = adminPermissionPlusService.listByIds(pids);
        Set<String> URLs = adminPermissions.stream().map(AdminPermission::getUrl).collect(Collectors.toSet());

        return URLs;
    }
}
