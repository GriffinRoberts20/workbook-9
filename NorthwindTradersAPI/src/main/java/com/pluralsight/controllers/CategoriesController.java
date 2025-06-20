package com.pluralsight.controllers;

import com.pluralsight.dao.CategoryDao;
import com.pluralsight.models.Category;
import com.pluralsight.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {
    private CategoryDao categoryDao;

    @Autowired
    public CategoriesController(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @GetMapping
    public List<Category> getAll(@RequestParam(defaultValue = "")String name){
        return categoryDao.getAll(name);
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable int id){
        return categoryDao.getById(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Category addCategory(@RequestBody Category category){
        return categoryDao.add(category);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable int id, @RequestBody Category category){
        category.setCategoryId(id);
        categoryDao.update(category);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int id){
        categoryDao.delete(id);
    }
}
