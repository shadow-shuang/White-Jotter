package com.gm.wj.service.plus.impl;

import com.gm.wj.entity.Book;
import com.gm.wj.mapper.plus.BookPlusMapper;
import com.gm.wj.service.plus.BookPlusService;
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
public class BookPlusServiceImpl extends ServiceImpl<BookPlusMapper, Book> implements BookPlusService {

}
