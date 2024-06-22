import java.util.*;
import java.util.concurrent.CompletableFuture;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
//import java.sql.SQLException;
//import java.util.*;
//import java.io.*;
//import java.util.concurrent.CompletableFuture;

import com.mongodb.client.*;
//import org.bson.Document;
//import java.sql.Connection;


public class Seller extends Main{

    Scanner sc = new Scanner(System.in);

    void addProduct(){
        System.out.println("Enter Product Category: ");
        String categoryId = sc.next().toLowerCase();
        addCategory(categoryId);
        System.out.println("Enter product name:");
        String name=sc.next();
        System.out.println("Enter product price:");
        double prize=sc.nextDouble();
        if(prize<=0){
            System.out.println("Invalid prize");
            return;
        }
        System.out.println("Enter stock of product:");
        int stock=sc.nextInt();
        if(stock<=0){
            System.out.println("Invalid Stock");
            return;
        }
        Product p1=new Product(name,prize,stock,categoryId);
        Main.list.put(p1.productId,p1);
        MongoCollection<Document> collection = Main.getCollection();
        Document productDocument = new Document();
        productDocument.append("productId", p1.productId)
                .append("productName", p1.productName)
                .append("productPrice", p1.productPrize)
                .append("productStock", p1.stock)
                .append("exist", true)
                .append("categoryId",p1.categoryId);
        collection.insertOne(productDocument);


        CompletableFuture<Void> loadTask = CompletableFuture.runAsync(Main::saveProductsToFile);
        System.out.println("Added successfully");
    }
    void displayProduct(Product item){
        System.out.println("ProductID: "+item.productId+" categoryid: "+item.categoryId+" ProductName: "+item.productName+ " prize: "+item.productPrize+" stock available: "+item.stock+" exist:"+item.existflag);
    }
    void viewProduct(String id){
        if(Main.list.containsKey(id)){
            this.displayProduct(Main.list.get(id));
        }
        else{
            System.out.println("no such product");
        }
    }
    void viewProduct(){
        if(Main.list.isEmpty()){
            System.out.println("Empty Inventory");
        }
        for( Product item:Main.list.values()){
            this.displayProduct(Main.list.get(item.productId));
        }
    }

    void deleteProduct(String id){
        if(Main.list.containsKey(id)){
            if(!Main.list.get(id).existflag){
                System.out.println("Already removed");
                return;
            }
            Main.list.get(id).existflag=false;
            MongoCollection<Document> collection = Main.getCollection();
            Document deleteFilter = new Document("productId",id);
            Document Update = new Document("$set", new Document("exist",false));
            collection.updateOne(deleteFilter,Update);
            CompletableFuture<Void> save = CompletableFuture.runAsync(Main::saveProductsToFile);
            System.out.println("Removed successfully");
        }
        else{
            System.out.println("No such Product ");
        }
    }
    private static void addCategory(String categoryName) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("ProductManagement");
        MongoCollection<Document> categories = database.getCollection("Category");

        try {

            Document existingCategory = categories.find(Filters.eq("categoryName", categoryName)).first();

            if (existingCategory == null) {
                // Category does not exist, so add it
                Document categoryDocument = new Document();
                long categoryCount = categories.countDocuments();
                int categoryId = generateCategoryId((int) categoryCount);
                categoryDocument.append("categoryName", categoryName);
                categoryDocument.append("categoryId","Cat"+categoryId );
                categories.insertOne(categoryDocument);

                System.out.println("Category '" + categoryName + "' added.");
            }
        } catch (Exception e) {
            System.err.println("Error adding category: " + e.getMessage());
            e.printStackTrace();
        } finally {
            mongoClient.close();
        }

    }
    private static int generateCategoryId(int existingCount) {
        return existingCount + 1;
    }


}
