package com.pluralsight.dao;

import com.pluralsight.models.Category;
import com.pluralsight.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCategoryDao implements CategoryDao{
    private DataSource dataSource;


    @Autowired
    public JdbcCategoryDao(DataSource dataSource){
        this.dataSource=dataSource;
    }

    @Override
    public List<Category> getAll(String name){
        List<Category> categories=new ArrayList<>();
        String sql="SELECT CategoryID, CategoryName FROM categories";
        if(!(name.isEmpty())){
            sql+=" WHERE CategoryName LIKE ?";
        }
        sql+=" ORDER BY CategoryID";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            if(!(name.isEmpty())){
                preparedStatement.setString(1,"%"+name+"%");
            }
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int id=resultSet.getInt("CategoryID");
                    String categoryName=resultSet.getString("CategoryName");
                    categories.add(new Category(id,categoryName));
                }
            } catch (SQLException e){
                System.out.println("Error getting all");
            }
        } catch (SQLException e) {
            System.out.println("Error querying all");
        }
        return categories;
    }

    @Override
    public Category getById(int id) {
        Category category=new Category();
        String sql="SELECT CategoryID, CategoryName FROM categories WHERE CategoryID=?";
        try(Connection connection=dataSource.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int categoryID =resultSet.getInt("CategoryID");
                    String categoryName=resultSet.getString("CategoryName");
                    category.setCategoryId(categoryID);
                    category.setCategoryName(categoryName);
                }
            } catch (SQLException e){
                System.out.println("Error getting by ID");
            }
        } catch (SQLException e) {
            System.out.println("Error querying by ID");
        }
        return category;
    }

    @Override
    @ResponseStatus(value = HttpStatus.CREATED)
    public Category add(Category category){
        String sql="INSERT INTO categories (CategoryName) VALUES(?)";
        try(Connection connection=this.dataSource.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,category.getCategoryName());
            int rows=preparedStatement.executeUpdate();
            System.out.printf("Rows updated %d\n", rows);
            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    int categoryID = keys.getInt(1);
                    category.setCategoryId(categoryID);
                }
            }
            System.out.println("Added "+category);
        } catch (SQLException e){
            System.out.println("Error adding category");
        }
        return category;
    }

    @Override
    public void update(Category category){
        String sql= """
                UPDATE categories
                SET CategoryName=?
                WHERE CategoryID=?""";
        try(Connection connection=this.dataSource.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            preparedStatement.setString(1,category.getCategoryName());
            preparedStatement.setInt(2,category.getCategoryId());
            int rows=preparedStatement.executeUpdate();
            System.out.printf("Rows updated %d\n", rows);
        } catch(SQLException e) {
            System.out.println("Error updating category");
        }
    }

    @Override
    public void delete(int id){
        String sql="DELETE FROM categories WHERE CategoryID=?";
        try(Connection connection=this.dataSource.getConnection();
            PreparedStatement preparedStatement=connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);
            int rows=preparedStatement.executeUpdate();
            System.out.printf("Rows deleted %d\n", rows);
        } catch (SQLException e){
            System.out.println("Delete SQL error");
        }
    }
}
