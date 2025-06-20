package com.pluralsight.controllers;

import com.pluralsight.dao.ProductDao;
import com.pluralsight.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private ProductDao productDao;

    @Autowired
    public ProductsController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping
    public List<Product> getAll(@RequestParam(defaultValue = "")String name, @RequestParam(defaultValue = "-1")int category, @RequestParam(defaultValue = "-1")double maxPrice){
        return productDao.getAll(name,category,maxPrice);
    }

    @GetMapping("/{id}")
    public Product getByID(@PathVariable int id){
        return productDao.get(id);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public Product addProduct(@RequestBody Product product){
        return productDao.add(product);
    }

    @PutMapping("/{id}")
    public void updateProduct(@PathVariable int id, @RequestBody Product product){
        product.setProductId(id);
        productDao.update(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value=HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable int id){
        productDao.delete(id);
    }
}
