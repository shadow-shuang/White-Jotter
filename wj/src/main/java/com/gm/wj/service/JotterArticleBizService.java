package com.gm.wj.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gm.wj.entity.JotterArticle;
import com.gm.wj.redis.RedisService;
import com.gm.wj.service.plus.JotterArticlePlusService;
import com.gm.wj.util.MyPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Evan
 * @date 2020/1/14 21:00
 */
@Service
public class JotterArticleBizService {
    @Autowired
    private JotterArticlePlusService jotterArticlePlusService;
    @Autowired
    private RedisService redisService;

    public MyPage<JotterArticle> list(int page, int size) {
        MyPage<JotterArticle> articles;
        String key = "articlepage:" + page;
        Object articlePageCache = redisService.get(key);

        if (articlePageCache == null) {

            // TODO
            LambdaQueryWrapper<JotterArticle> wrapper = Wrappers.lambdaQuery(JotterArticle.class).orderByDesc(JotterArticle::getId);
            Page<JotterArticle> articlePage = jotterArticlePlusService.page(new Page<>(page, size), wrapper);
            articles = new MyPage<>(articlePage);
            redisService.set(key, articles);
        } else {
            articles = (MyPage<JotterArticle>) articlePageCache;
        }
        return articles;
    }

    public JotterArticle findById(int id) {
        JotterArticle article;
        String key = "article:" + id;
        Object articleCache = redisService.get(key);

        if (articleCache == null) {
            article = jotterArticlePlusService.lambdaQuery().eq(JotterArticle::getId, id).one();
            redisService.set(key, article);
        } else {
            article = (JotterArticle) articleCache;
        }
        return article;
    }

    public void addOrUpdate(JotterArticle article) {
        jotterArticlePlusService.save(article);
        redisService.delete("article" + article.getId());
        Set<String> keys = redisService.getKeysByPattern("articlepage*");
        redisService.delete(keys);
    }

    public void delete(int id) {
        jotterArticlePlusService.removeById(id);
        redisService.delete("article:" + id);
        Set<String> keys = redisService.getKeysByPattern("articlepage*");
        redisService.delete(keys);
    }
}
