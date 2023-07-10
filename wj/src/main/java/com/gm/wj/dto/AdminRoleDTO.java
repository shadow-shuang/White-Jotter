package com.gm.wj.dto;

import com.gm.wj.entity.AdminMenu;
import com.gm.wj.entity.AdminPermission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.wu
 * @since 2023-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AdminRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String nameZh;

    private Boolean enabled;

    private List<AdminPermission> perms;

    private List<AdminMenu> menus;
}
