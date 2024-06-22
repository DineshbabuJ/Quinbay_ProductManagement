import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.HashMap;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
//import java.io.*;
//import java.util.ArrayList;
//import java.util.concurrent.CompletableFuture;

public class Customer {
    private Connection conn;

    public Customer() {
        String url = "jdbc:postgresql://localhost:5432/postgres"; // Replace with your database URL
        String user = "postgres"; // Replace with your database username
        String password = "1234";
        try {
            conn = DriverManager.getConnection(url, user, password); // Assign to the class-level variable
            if (conn != null) {
                System.out.println("Connected to the PostgreSQL database!");

            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    static ArrayList<Product> purchaseHistory = new ArrayList<>();
    static HashMap<String,Product> orderItems = new HashMap<>();

    static String insertOrderItems = "INSERT INTO \"OrderItems\" (\"OrderId\", \"ProductId\", \"ProductName\", \"ProductQuantity\", \"ProductPrice\") VALUES (?, ?, ?, ?, ?)";
    static String insertOrders = "INSERT INTO \"Orders\" (\"OrderId\", \"NoOfProducts\", \"totalPrice\") VALUES (?, ?, ?)";
    private int getOrderCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) AS count FROM \"Orders\""; // Replace with your table name if different
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    void purchase() {
        orderItems.clear();
        boolean isContinue = true;
        Scanner sc = new Scanner(System.in);
        int orderCount = getOrderCount();
        String orderId = "Ord" + (orderCount+1);
        while(isContinue) {
            System.out.println("Enter the product id to purchase ");
            String id = sc.next();
            if (!Main.list.containsKey(id) || !Main.list.get(id).existflag) {
                System.out.println("Invalid id");
            } else {
                System.out.println("Enter the number of products to purchase ");
                int stock = sc.nextInt();
                if (stock <= 0) {
                    System.out.println("Invalid amount for purchase");
                } else if (Main.list.get(id).stock == 0) {
                    System.out.println("Out of stock");
                } else if (Main.list.get(id).stock < stock) {
                    System.out.println("There are only " + Main.list.get(id).stock + " available");
                } else {
//                    Main.list.get(id).stock -= stock;
//                    if (Main.list.get(id).stock == 0) Main.list.get(id).existflag = false;
//                    Main.saveProductsToFile();
                    Product item = Main.list.get(id);
                    Product p1 = new Product(item.productId, item.productName, item.productPrize, stock);
                    if(orderItems.containsKey(p1.productId)){
                        p1.stock+=orderItems.get(p1.productId).stock;
                    }
                    if(p1.stock<=Main.list.get(id).stock)
                        orderItems.put(p1.productId,p1);
                    else System.out.println("There are only "+Main.list.get(id).stock+" products available But you try to order more tha that");
//                    CompletableFuture<Void> loadTask = CompletableFuture.runAsync(this::saveProductsToFile);
                }

            }
            System.out.println("Do you need want to purchase more items (y/n)");
            if (!sc.next().equalsIgnoreCase("y")) isContinue = false;
        }

        System.out.println("Confirm to purchase (y/n)");
        if (sc.next().equalsIgnoreCase("y")) {
            boolean itemExceededStock=false;
            try {
                PreparedStatement pstmt = conn.prepareStatement(insertOrderItems);
                int noOfProducts=0;
                int totalPrice=0;
                for (Product prod : orderItems.values()) {
                    noOfProducts+=prod.stock;
                    totalPrice+= (int) (prod.stock*prod.productPrize);
                    pstmt.setString(1, orderId);
                    pstmt.setString(2, prod.productId);
                    pstmt.setString(3, prod.productName);
                    pstmt.setInt(4, prod.stock);
                    pstmt.setDouble(5, prod.productPrize);
                    pstmt.addBatch();

                    if (Main.list.get(prod.productId).stock-prod.stock>=0) {
                        MongoCollection<Document> collection = Main.getCollection();
                        Document stockfilter = new Document("productId", prod.productId);
                        Document stockupdate = new Document("$set", new Document("productStock",Main.list.get(prod.productId).stock-prod.stock ));
                        collection.updateOne(stockfilter, stockupdate);
                    }
                    else{
                        System.out.println("only "+Main.list.get(prod.productId).stock+" are available in "+prod.productId+" but you ordered "+prod.stock);
                        itemExceededStock=true;
                    }


                }
                if(!itemExceededStock) {

                    // Clear the order items after saving
                    PreparedStatement pstmt2 = conn.prepareStatement(insertOrders);
                    pstmt2.setString(1, orderId);
                    pstmt2.setInt(2, noOfProducts);
                    pstmt2.setDouble(3, totalPrice);
                    pstmt2.addBatch();
                    pstmt2.executeBatch();
                    System.out.println("Order saved successfully!");
                    pstmt.executeBatch();
                    Main.readProductsFromDatabase();
//                for(Product prod:orderItems){
//                      purchaseHistory.add(prod);
//                }
                }
                else System.out.println("order cancelled");


            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    void History() {
//        if (purchaseHistory.isEmpty()) {
//            System.out.println("No purchases so far");
//        }
//        for (Product item : purchaseHistory) {
//            displayProduct(item);
//        }
        String sql = "SELECT o.\"OrderId\", o.\"NoOfProducts\", o.\"totalPrice\", " +
                "oi.\"ProductId\", oi.\"ProductName\", oi.\"ProductQuantity\", oi.\"ProductPrice\" " +
                "FROM \"Orders\" o " +
                "JOIN \"OrderItems\" oi ON o.\"OrderId\" = oi.\"OrderId\" " +
                "ORDER BY o.\"OrderId\"";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No purchase history found.");
            } else {
                String currentOrderId = "";
                while (rs.next()) {
                    String orderId = rs.getString("OrderId");
                    if (!orderId.equals(currentOrderId)) {
                        if (!currentOrderId.isEmpty()) {
                            System.out.println();  // Print a blank line between orders
                        }
                        currentOrderId = orderId;
                        int noOfProducts = rs.getInt("NoOfProducts");
                        double totalPrice = rs.getDouble("totalPrice");
                        System.out.println("Order ID: " + orderId +
                                ", No of Products: " + noOfProducts +
                                ", Total Price: " + totalPrice);
                    }
                    // Fetch and display the product details for the current order
                    String productId = rs.getString("ProductId");
                    String productName = rs.getString("ProductName");
                    int productQuantity = rs.getInt("ProductQuantity");
                    double productPrice = rs.getDouble("ProductPrice");
                    System.out.println("    Product ID: " + productId +
                            ", Product Name: " + productName +
                            ", Quantity: " + productQuantity +
                            ", Price: " + productPrice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void displayProduct(Product item) {
        if (item.existflag)
            System.out.println("ProductID: " + item.productId + " ProductName: " + item.productName +" CategoryId:"+item.categoryId+ " prize: " + item.productPrize + " quantity " + item.stock);
    }

    void viewProduct(String id) {
        if (Main.list.containsKey(id)) {
            this.displayProduct(Main.list.get(id));
        } else {
            System.out.println("No such product");
        }
    }

    void viewProduct() {
        if (Main.list.isEmpty()) {
            System.out.println("Empty Inventory");
        }
        for (Product item : Main.list.values()) {
            this.displayProduct(Main.list.get(item.productId));
        }
    }

//    static void loadProductsFromFile() {
//        try (BufferedReader reader = new BufferedReader(new FileReader("historyOfPurchase.txt"))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length == 5) {
//                    Product product = new Product(parts[0], parts[1], Float.parseFloat(parts[2]), Integer.parseInt(parts[3]), Boolean.parseBoolean(parts[4]));
//                    if (Boolean.parseBoolean(parts[4]))
//                        purchaseHistory.add(product);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    void saveProductsToFile() {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter("historyOfPurchase.txt"))) {
//            for (Product product : purchaseHistory) {
//                writer.write(product.toString());
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
