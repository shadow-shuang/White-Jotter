package com.gm.wj.service;

import com.gm.wj.dto.UserDTO;
import com.gm.wj.entity.AdminRole;
import com.gm.wj.entity.User;
import com.gm.wj.service.plus.UserPlusService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Evan
 * @date 2019/4
 */
@Service
public class UserBizService {
    @Autowired
    private UserPlusService userPlusService;
    @Autowired
    private AdminRoleBizService adminRoleBizService;
    @Autowired
    private AdminUserRoleBizService adminUserRoleBizService;

    public List<UserDTO> list() {
        List<User> users = userPlusService.query().list();

        List<UserDTO> userDTOS = users
                .stream().map(user -> (UserDTO) new UserDTO().convertFrom(user)).collect(Collectors.toList());

        userDTOS.forEach(u -> {
            List<AdminRole> roles = adminRoleBizService.listRolesByUser(u.getUsername());
            u.setRoles(roles);
        });

        return userDTOS;
    }

    public boolean isExist(String username) {
        User user = userPlusService.lambdaQuery().eq(User::getUsername, username).one();
        return null != user;
    }

    public User findByUsername(String username) {
        return userPlusService.lambdaQuery().eq(User::getUsername, username).one();
    }

    public User get(String username, String password) {
        return userPlusService.lambdaQuery().eq(User::getUsername, username)
                .eq(User::getPassword, password)
                .one();
    }

    public int register(User user) {
        String username = user.getUsername();
        String name = user.getName();
        String phone = user.getPhone();
        String email = user.getEmail();
        String password = user.getPassword();

        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        phone = HtmlUtils.htmlEscape(phone);
        user.setPhone(phone);
        email = HtmlUtils.htmlEscape(email);
        user.setEmail(email);
        user.setEnabled(true);

        if (username.equals("") || password.equals("")) {
            return 0;
        }

        boolean exist = isExist(username);

        if (exist) {
            return 2;
        }

        // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();

        user.setSalt(salt);
        user.setPassword(encodedPassword);

        userPlusService.save(user);
        return 1;
    }

    public void updateUserStatus(User user) {
        User userInDB = userPlusService.lambdaQuery().eq(User::getUsername, user.getUsername()).one();
        userInDB.setEnabled(user.getEnabled());
        userPlusService.save(userInDB);
    }

    public boolean resetPassword(User user) {
        User userInDB = userPlusService.lambdaQuery().eq(User::getUsername, user.getUsername()).one();
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        userInDB.setSalt(salt);
        String encodedPassword = new SimpleHash("md5", "123", salt, times).toString();
        userInDB.setPassword(encodedPassword);
        return userPlusService.save(userInDB);
    }

    public void editUser(UserDTO user) {
        User userInDB = userPlusService.lambdaQuery().eq(User::getUsername, user.getUsername()).one();
        userInDB.setName(user.getName());
        userInDB.setPhone(user.getPhone());
        userInDB.setEmail(user.getEmail());
        userPlusService.save(userInDB);
        adminUserRoleBizService.saveRoleChanges(userInDB.getId(), user.getRoles());
    }

    public void deleteById(int id) {
        userPlusService.removeById(id);
    }
}
