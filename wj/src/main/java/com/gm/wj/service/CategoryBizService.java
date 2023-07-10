package com.gm.wj.service;

import com.gm.wj.entity.Category;
import com.gm.wj.service.plus.CategoryPlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Evan
 * @date 2019/4
 */
@Service
public class CategoryBizService {
    @Autowired
    private CategoryPlusService categoryPlusService;

    public List<Category> list() {
        return categoryPlusService.lambdaQuery()
                .orderByDesc(Category::getId)
                .list();
    }

    public Category get(int id) {
        return categoryPlusService.getById(id);
    }
}
