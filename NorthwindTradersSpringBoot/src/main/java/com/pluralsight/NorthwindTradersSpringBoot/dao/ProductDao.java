package com.pluralsight.NorthwindTradersSpringBoot.dao;

import com.pluralsight.NorthwindTradersSpringBoot.models.Product;

import java.util.List;

public interface ProductDao {
    void add(Product product);
    List<Product> getAll();

    void delete(int productID);

    void update(Product product);

    Product get(int id);

    List<Product> getByKeyword(String keyword);
}
