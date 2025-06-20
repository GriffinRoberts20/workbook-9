package com.pluralsight.NorthwindTradersSpringBoot;

import com.pluralsight.NorthwindTradersSpringBoot.dao.ProductDao;
import com.pluralsight.NorthwindTradersSpringBoot.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class NorthwindApplication implements CommandLineRunner {
    @Autowired
    @Qualifier("jdbcProductDao")
    private ProductDao productDao;

    static Scanner scanner=new Scanner(System.in);
    @Override
    public void run(String... args) throws Exception{
        while(true){
            System.out.println("\n=== Product Admin Menu ===");
            System.out.println("1. List Products");
            System.out.println("2. Add Product");
            System.out.println("3. Delete Product");
            System.out.println("4. Update Product");
            System.out.println("5. Search Products");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice){
                case "1":
                    processListAll();
                    break;
                case "2":
                    processAddProduct();
                    break;
                case "3":
                    processDelete();
                    break;
                case "4":
                    processUpdate();
                    break;
                case "5":
                    processKeywordSearch();
                    break;
                case "0":
                    System.out.println("Exiting");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    public void processListAll(){
        List<Product> products=productDao.getAll();
        for(Product p:products){
            System.out.println(p);
        }
    }

    public void processAddProduct(){
        Product product=new Product();
        product=pickName(product);
        product=pickCategory(product);
        product=pickPrice(product);
        productDao.add(product);
    }

    public void processDelete(){
        int productId;
        while(true){
            System.out.print("Enter productId of product to delete, or 0 to cancel: ");
            try {
                productId = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e){
                System.out.println("Invalid input");
                continue;
            }
            if(productId<0){
                System.out.println("Invalid input");
                continue;
            }
            if(productId==0) return;
            productDao.delete(productId);
            break;
        }
    }

    public void processUpdate(){
        int id;
        while(true){
            System.out.print("Enter product ID to update or 0 to cancel: ");
            try{
                id=Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e){
                System.out.println("Invalid input");
                continue;
            }
            if(id<0){
                System.out.println("Invalid input");
                continue;
            }
            if(id==0) return;
            Product toUpdate=productDao.get(id);
            if(toUpdate==null){
                System.out.println("Invalid id, canceling update");
                return;
            }
            boolean updating=true;
            while(updating){
                System.out.println(toUpdate);
                System.out.println("What would you like to update?");
                System.out.println("\t1. Name");
                System.out.println("\t2. Category");
                System.out.println("\t3. Price");
                System.out.println("\t4. Update product");
                System.out.println("\t0. Cancel update");
                System.out.print("Enter choice: ");
                String choice=scanner.nextLine().trim();
                switch (choice){
                    case "1":
                        toUpdate=pickName(toUpdate);
                        break;
                    case "2":
                        toUpdate=pickCategory(toUpdate);
                        break;
                    case "3":
                        toUpdate=pickPrice(toUpdate);
                        break;
                    case "4":
                        productDao.update(toUpdate);
                        updating=false;
                        continue;
                    case "0":
                        updating=false;
                        continue;
                    default:
                        System.out.println("Invalid input");
                }
            }
            break;
        }
    }

    public void processKeywordSearch(){
        System.out.print("Search by product name: ");
        String keyword=scanner.nextLine().trim();
        List<Product> products=productDao.getByKeyword(keyword);
        for(Product p:products){
            System.out.println(p);
        }
    }

    public Product pickName(Product product){
        while(true){
            System.out.print("Enter product name: ");
            String name=scanner.nextLine().trim();
            if(!(name.isEmpty())){
                product.setName(name);
                break;
            }
            System.out.println("Name cannot be empty");
        }
        return product;
    }

    public Product pickCategory(Product product){
        while(true){
            System.out.println("Categories");
            System.out.println("\t1. Beverages");
            System.out.println("\t2. Condiments");
            System.out.println("\t3. Confections");
            System.out.println("\t4. Dairy Products");
            System.out.println("\t5. Grains/Cereals");
            System.out.println("\t6. Meat/Poultry");
            System.out.println("\t7. Produce");
            System.out.println("\t8. Seafood");
            System.out.print("Select category: ");
            String category=scanner.nextLine().trim();
            switch (category){
                case "1":
                    product.setCategory("Beverages");
                    break;
                case "2":
                    product.setCategory("Condiments");
                    break;
                case "3":
                    product.setCategory("Confections");
                    break;
                case "4":
                    product.setCategory("Dairy Products");
                    break;
                case "5":
                    product.setCategory("Grains/Cereals");
                    break;
                case "6":
                    product.setCategory("Meat/Poultry");
                    break;
                case "7":
                    product.setCategory("Produce");
                    break;
                case "8":
                    product.setCategory("Seafood");
                    break;
                default:
                    System.out.println("Invalid category");
                    continue;
            }
            break;
        }
        return product;
    }

    public Product pickPrice(Product product){
        while(true){
            System.out.print("Enter price: ");
            String price=scanner.nextLine().trim();
            if(!(price.isEmpty())){
                try{
                    double p=Double.parseDouble(price);
                    if(p<=0){
                        System.out.println("Price cannot be 0 or less");
                        continue;
                    }
                    product.setPrice(p);
                    break;
                } catch (Exception e){
                    System.out.println("Price must be a number");
                }
            }
            System.out.println("Price cannot be empty");
        }
        return product;
    }
}
