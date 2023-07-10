package com.gm.wj.service;

import com.gm.wj.entity.Book;
import com.gm.wj.redis.RedisService;
import com.gm.wj.service.plus.BookPlusService;
import com.gm.wj.util.CastUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Evan
 * @date 2019/4
 */
@Service
public class BookBizService {
    @Autowired
    private BookPlusService bookPlusService;

    @Autowired
    private RedisService redisService;

    public List<Book> list() {
        List<Book> books;
        String key = "booklist";
        Object bookCache = redisService.get(key);

        if (bookCache == null) {
            books = bookPlusService.lambdaQuery().orderByDesc(Book::getId).list();
            redisService.set(key, books);
        } else {
            books = CastUtils.objectConvertToList(bookCache, Book.class);
        }
        return books;
    }

    public void addOrUpdate(Book book) {
        redisService.delete("booklist");
        bookPlusService.save(book);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete("booklist");
    }

    public void deleteById(int id) {
        redisService.delete("booklist");
        bookPlusService.removeById(id);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redisService.delete("booklist");
    }

    public List<Book> listByCategory(int cid) {
        List<Book> books = bookPlusService.lambdaQuery().eq(Book::getCid, cid).list();
        return books;
    }

    public List<Book> Search(String keywords) {
        return bookPlusService.lambdaQuery()
                .like(Book::getTitle, keywords)
                .or()
                .like(Book::getAuthor, keywords)
                .list();
    }
}
