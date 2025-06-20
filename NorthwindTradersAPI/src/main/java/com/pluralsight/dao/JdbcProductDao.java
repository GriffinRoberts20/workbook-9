package com.pluralsight.dao;

import com.pluralsight.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcProductDao implements ProductDao{

    private DataSource dataSource;


    @Autowired
    public JdbcProductDao(DataSource dataSource){
        this.dataSource=dataSource;
    }

    @Override
    public Product add(Product product){
        String sql="INSERT INTO products (ProductName, CategoryID, UnitPrice) VALUES(?,?,?)";
        try(Connection connection=this.dataSource.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,product.getName());
            preparedStatement.setInt(2,product.getCategory());
            preparedStatement.setDouble(3,product.getPrice());
            int rows=preparedStatement.executeUpdate();
            System.out.printf("Rows updated %d\n", rows);
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int productID = keys.getInt(1);
                    product.setProductId(productID);
                }
            }
            System.out.println("Added "+product);
        } catch (SQLException e){
            System.out.println("Error adding product");
        }
        return product;
    }

    @Override
    public List<Product> getAll() {
        List<Product> products=new ArrayList<>();
        String sql = """
                SELECT ProductID, ProductName, CategoryID, UnitPrice
                FROM products
                ORDER BY ProductID
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int productID=resultSet.getInt("ProductID");
                    String name=resultSet.getString("ProductName");
                    int category=resultSet.getInt("CategoryID");
                    double price=resultSet.getDouble("UnitPrice");
                    products.add(new Product(productID,name,category,price));
                }
            } catch (SQLException e){
                System.out.println("Error getting all");
            }
        } catch (SQLException e){
            System.out.println("Error querying all");
        }
        return products;
    }

    @Override
    public List<Product> getAll(String name, int category, double maxPrice) {
        List<Product> products=new ArrayList<>();
        String sql = """
                SELECT ProductID, ProductName, CategoryID, UnitPrice
                FROM products
                """;
        if(!(name.isEmpty())|| category>0 || maxPrice>0){
            sql+="WHERE";
            if(!(name.isEmpty())){
                sql+=" ProductName LIKE ?";
            }
            if(category>0){
                if(!(name.isEmpty())){
                    sql+=" AND";
                }
                sql+=" CategoryID=?";
            }
            if(maxPrice>0){
                if(!(name.isEmpty())||category>0){
                    sql+=" AND";
                }
                sql+=" UnitPrice<?";
            }
            sql+="\n";
        }
        sql+="ORDER BY ProductID";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if(!(name.isEmpty())|| category>0 || maxPrice>0){
                int i=1;
                if(!(name.isEmpty())){
                    preparedStatement.setString(i,"%"+name+"%");
                    i++;
                }
                if(category>0){
                    preparedStatement.setInt(i,category);
                    i++;
                }
                if(maxPrice>0){
                    preparedStatement.setDouble(i,maxPrice);
                }
            }
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int productID=resultSet.getInt("ProductID");
                    String productName =resultSet.getString("ProductName");
                    int categoryID =resultSet.getInt("CategoryID");
                    double price=resultSet.getDouble("UnitPrice");
                    products.add(new Product(productID, productName, categoryID,price));
                }
            } catch (SQLException e){
                System.out.println("Error getting all");
            }
        } catch (SQLException e){
            System.out.println("Error querying all");
        }
        return products;
    }

    @Override
    public void delete(int productID){
        String sql="DELETE FROM products WHERE ProductID=?";
        try(Connection connection=this.dataSource.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            preparedStatement.setInt(1,productID);
            int rows=preparedStatement.executeUpdate();
            System.out.printf("Rows deleted %d\n", rows);
        } catch (SQLException e){
            System.out.println("Delete SQL error");
        }
    }

    @Override
    public void update(Product product){
        String sql= "UPDATE products SET";
        if(product.getName()!=null) sql+=" ProductName=?";
        if(product.getName()!=null&&product.getCategory()!=0) sql+=",";
        if(product.getCategory()!=0) sql+=" CategoryID=?";
        if(product.getCategory()!=0&&product.getPrice()!=0) sql+=",";
        if(product.getPrice()!=0) sql+=" UnitPrice=?";
        sql+=" WHERE ProductID=?";

        try(Connection connection=this.dataSource.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            int i=1;
            if(product.getName()!=null) {
                preparedStatement.setString(i, product.getName());
                i++;
            }
            if(product.getCategory()!=0){
                preparedStatement.setInt(i, product.getCategory());
                i++;
            }
            if(product.getPrice()!=0) {
                preparedStatement.setDouble(i, product.getPrice());
                i++;
            }
            preparedStatement.setInt(i,product.getProductId());
            int rows=preparedStatement.executeUpdate();
            System.out.printf("Rows updated %d\n", rows);
        } catch(SQLException e) {
            System.out.println("Error updating product");
        }
    }

    public int getCategoryId(String categoryName){
        int categoryID=-1;
        String categorySql="SELECT CategoryID FROM categories WHERE CategoryName=?";
        try(Connection connection=this.dataSource.getConnection();
            PreparedStatement categoryStatement= connection.prepareStatement(categorySql)){
            categoryStatement.setString(1,categoryName);
            try(ResultSet categoryResult =categoryStatement.executeQuery()){
                while(categoryResult.next()){
                    categoryID= categoryResult.getInt("CategoryID");
                }
            }
        } catch (SQLException e){
            System.out.println("Error getting category ID");
        }
        return categoryID;
    }

    @Override
    public Product get(int id){
        Product product=null;
        String sql="""
                SELECT ProductID, ProductName, CategoryID, UnitPrice
                FROM products
                WHERE ProductID=?
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1,id);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int productID=resultSet.getInt("ProductID");
                    String name=resultSet.getString("ProductName");
                    int category=resultSet.getInt("CategoryID");
                    double price=resultSet.getDouble("UnitPrice");
                    product=new Product(productID,name,category,price);
                }
            } catch (SQLException e){
                System.out.println("Error getting product");
            }
        } catch (SQLException e){
            System.out.println("Error querying product");
        }
        return product;
    }

    @Override
    public List<Product> getByKeyword(String keyword){
        List<Product> products=new ArrayList<>();
        String sql = """
                SELECT ProductID, ProductName, CategoryID, UnitPrice
                FROM products
                WHERE p.ProductName LIKE ?
                ORDER BY p.ProductID
                """;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1,"%"+keyword+"%");
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int productID=resultSet.getInt("ProductID");
                    String name=resultSet.getString("ProductName");
                    int category=resultSet.getInt("CategoryID");
                    double price=resultSet.getDouble("UnitPrice");
                    products.add(new Product(productID,name,category,price));
                }
            } catch (SQLException e){
                System.out.println("Error getting by keyword");
            }
        } catch (SQLException e){
            System.out.println("Error querying by keyword");
        }
        return products;
    }
}
