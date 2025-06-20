package com.pluralsight.dao;

import com.pluralsight.models.Category;

import java.util.List;

public interface CategoryDao {
    public List<Category> getAll(String name);
    public Category getById(int id);
    public Category add(Category category);
    public void update(Category category);
    public void delete(int id);
}
