import java.io.*;
import java.util.HashMap;
import java.util.Map;
//
//
//public class InventoryManager {
//    private static final String FILE_NAME = "inventory.txt";
//    private Map<Integer, Product> inventory;
//
//    public InventoryManager() {
//        inventory = new HashMap<>();
//        loadProductsFromFile();
//    }
//
//    // Method to add or update a product
//    public void addOrUpdateProduct(Product product) {
//        inventory.put(product.getId(), product);
//        saveProductsToFile();
//    }
//
//    // Method to remove a product
//    public void removeProduct(String productId) {
//        inventory.remove(productId);
//        saveProductsToFile();
//    }
//
//    // Method to get a product by ID
//    public Product getProduct(String productId) {
//        return inventory.get(productId);
//    }
//
//    // Method to get all products
//    public Map<Integer, Product> getAllProducts() {
//        return inventory;
//    }
//
//    // Method to save products to file
//    private void saveProductsToFile() {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
//            for (Product product : inventory.values()) {
//                writer.write(product.toString());
//                writer.newLine();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Method to load products from file
//    private void loadProductsFromFile() {
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] parts = line.split(",");
//                if (parts.length == 4) {
//                    Product product = new Product(Integer.parseInt(parts[0]), parts[1], Float.parseFloat(parts[2]), Integer.parseInt(parts[3]));
//                    inventory.put(product.getId(), product);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
