package com.gm.wj.service;

import com.gm.wj.dto.AdminRoleDTO;
import com.gm.wj.entity.AdminMenu;
import com.gm.wj.entity.AdminPermission;
import com.gm.wj.entity.AdminRole;
import com.gm.wj.entity.AdminUserRole;
import com.gm.wj.service.plus.AdminRolePlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Evan
 * @date 2019/11
 */
@Service
public class AdminRoleBizService {

    @Autowired
    private AdminRolePlusService adminRolePlusService;
    @Autowired
    private UserBizService userBizService;
    @Autowired
    private AdminUserRoleBizService adminUserRoleBizService;
    @Autowired
    private AdminPermissionBizService adminPermissionBizService;
    @Autowired
    private AdminRolePermissionBizService adminRolePermissionBizService;
    @Autowired
    private AdminMenuBizService adminMenuBizService;

    public List<AdminRoleDTO> listWithPermsAndMenus() {
        List<AdminRole> roles = adminRolePlusService.lambdaQuery().list();

        List<AdminRoleDTO> roleDTOS = roles.stream().map(adminRole -> {
            AdminRoleDTO adminRoleDTO = new AdminRoleDTO();
            adminRoleDTO.setId(adminRole.getId());
            adminRoleDTO.setName(adminRoleDTO.getName());
            adminRoleDTO.setNameZh(adminRoleDTO.getNameZh());
            adminRoleDTO.setEnabled(adminRole.getEnabled());
            List<AdminPermission> adminPermissions = adminPermissionBizService.listPermsByRoleId(adminRole.getId());
            List<AdminMenu> adminMenus = adminMenuBizService.getMenusByRoleId(adminRole.getId());
            adminRoleDTO.setPerms(adminPermissions);
            adminRoleDTO.setMenus(adminMenus);
            return adminRoleDTO;
        }).collect(Collectors.toList());

        return roleDTOS;
    }

    public List<AdminRole> findAll() {
        return adminRolePlusService.lambdaQuery().list();
    }


    public void addOrUpdate(AdminRoleDTO adminRoleDTO) {
        AdminRole adminRole = new AdminRole();
        adminRole.setName(adminRoleDTO.getName());
        adminRole.setNameZh(adminRole.getNameZh());
        adminRole.setEnabled(adminRole.getEnabled());

        adminRolePlusService.save(adminRole);
    }

    public List<AdminRole> listRolesByUser(String username) {
        int uid = userBizService.findByUsername(username).getId();
        List<Integer> rids = adminUserRoleBizService.listAllByUid(uid)
                .stream().map(AdminUserRole::getRid).collect(Collectors.toList());

        return adminRolePlusService.listByIds(rids);
    }

    public AdminRole updateRoleStatus(AdminRole role) {
        AdminRole roleInDB = adminRolePlusService.getById(role.getId());
        roleInDB.setEnabled(role.getEnabled());
        adminRolePlusService.updateById(roleInDB);
        return roleInDB;
    }

    public void editRole(@RequestBody AdminRoleDTO role) {
        AdminRole adminRole = new AdminRole();
        adminRole.setId(role.getId());
        adminRole.setName(role.getName());
        adminRole.setNameZh(role.getNameZh());
        adminRole.setEnabled(role.getEnabled());
        adminRolePlusService.save(adminRole);

        adminRolePermissionBizService.savePermChanges(role.getId(), role.getPerms());
    }
}
