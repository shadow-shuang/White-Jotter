package com.gm.wj.service.plus.impl;

import com.gm.wj.entity.User;
import com.gm.wj.mapper.plus.UserPlusMapper;
import com.gm.wj.service.plus.UserPlusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shuang.wu
 * @since 2023-07-10
 */
@Service
public class UserPlusServiceImpl extends ServiceImpl<UserPlusMapper, User> implements UserPlusService {

}
