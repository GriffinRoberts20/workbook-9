package com.pluralsight.dao;

import com.pluralsight.models.Product;

import java.util.List;

public interface ProductDao {
    Product add(Product product);
    List<Product> getAll();
    List<Product> getAll(String name, int category, double maxPrice);
    void delete(int productID);

    void update(Product product);

    Product get(int id);

    List<Product> getByKeyword(String keyword);
}
