//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.sql.SQLException;
import java.util.*;
import java.io.*;
import java.util.concurrent.CompletableFuture;

import com.mongodb.client.*;
import org.bson.Document;
import java.sql.Connection;

public class Main {
    public static HashMap<String,Product> list=new HashMap<>();

    private static final String FILE_NAME = "inventory.txt";
    private static MongoCollection<Document> collection;
    public static void main(String[] args) {

        MongoClient mongoClient = null;
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("ProductManagement");
        collection = database.getCollection("products");

//        System.out.println(Thread.currentThread().getName());
        System.out.println("Starting to load products asynchronously...");
        CompletableFuture<Void> loadTask = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                readProductsFromDatabase();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            loadProductsFromFile();
        });

//        CompletableFuture<Void> loadCustomerTask = CompletableFuture.runAsync(() -> {
//            System.out.println(Thread.currentThread().getName());
//            Customer.loadProductsFromFile();
//        });


        Scanner sc = new Scanner(System.in);
        Seller seller1=new Seller();
        Customer customer1= new Customer();
        System.out.println("-----------------------------------------");
        System.out.println("WELCOME TO PRODUCT STORE");
        boolean flagtoContinue=true;
        while(flagtoContinue){
            System.out.println("-----------------------------------------");
            System.out.println("CHOOSE WHO ARE YOU \n 1.Seller \n 2.Buyer");
            int option = sc.nextInt();

            if (option == 1) {
                System.out.println("List of Choices for you:\n 1.Enter product to the inventory \n 2.View single product \n 3.List All products in inventory\n 4.Update product stock\n 5.Update product prize\n 6.Remove product from inventory \n 7.Exit");
                int choice=sc.nextInt();
                switch(choice){
                    case 1:
//                        seller1.addProduct();
                        CompletableFuture.runAsync(()->seller1.addProduct()).join();
                        break;
                    case 2:
                        System.out.println("enter the product id");
                        String id=sc.next();
                        seller1.viewProduct(id);
                        break;

                    case 3:
                        seller1.viewProduct();
                        break;

                    case 4:
                        System.out.println("enter the product id");
                        String id1=sc.next();
                        if(!Main.list.containsKey(id1)){
                            System.out.println("No such product exists");
                            return;
                        }
                        System.out.println("Enter new Stock");
                        int newStock=sc.nextInt();
                        UpdateProduct.updateStock(id1,newStock);
                        break;

                    case 5:
                        System.out.println("enter the product id");
                        String id2=sc.next();
                        if(!Main.list.containsKey(id2)){
                            System.out.println("No such product exists");
                            return;
                        }
                        System.out.println("Enter new Prize");
                        double newPrize=sc.nextDouble();
                        UpdateProduct.updatePrize(id2,newPrize);
                        break;

                    case 6:
                        System.out.println("enter the product id");
                        String id3=sc.next();
                        seller1.deleteProduct(id3);
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid choice");




                }
            } else if (option == 2) {
                System.out.println("List of Choices for you:\n 1.View single product \n 2.List All products in inventory\n 3. Purchase Product \n 4.History of purchase \n 5.Exit");
                int choice=sc.nextInt();
                switch(choice){

                    case 1:
                        System.out.println("enter the product id");
                        String id=sc.next();
                        customer1.viewProduct(id);
                        break;

                    case 2:
                        customer1.viewProduct();
                        break;

                    case 3:
                        customer1.purchase();

                        break;
                    case 4:
                        customer1.History();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } else {
                System.out.println("Invalid input");
            }
            System.out.println("do you want to continue:(Y/N)");
            String shouldContinue=sc.next();
            if(!shouldContinue.equals("Y") && !shouldContinue.equals("y")) flagtoContinue=false;
        }

    }
     static void loadProductsFromFile() {
         System.out.println(Thread.currentThread().getName());
//        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length == 5) {
//                    Product product = new Product(parts[0], parts[1], Double.parseDouble(parts[2]), Integer.parseInt(parts[3]),Boolean.parseBoolean(parts[4]));
////                    if(Boolean.parseBoolean(parts[4]))
//                        list.put(product.getId(), product);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    static void saveProductsToFile() {
        System.out.println(Thread.currentThread().getName());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Product product : list.values()) {
                writer.write(product.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static MongoCollection<Document> getCollection() {
        return collection;
    }
    public static void readProductsFromDatabase() {
        FindIterable<Document> documents = collection.find();
        for (Document doc : documents) {
            Product product = null;
            try {
                product = Product.fromDocument(doc);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            list.put(product.getId(), product);
        }
    }

}