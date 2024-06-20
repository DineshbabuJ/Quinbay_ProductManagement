import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class Customer  {
    static ArrayList<Product> purchaseHistory=new ArrayList<>();
    void purchase(int id,int stock){
        if(stock<=0){
            System.out.println("Invalid amount for purchase");
        }
        else if(Main.list.get(id).stock==0){

            System.out.println("Out of stock");
        }
        else if(Main.list.get(id).stock<stock){
            System.out.println("There are only "+Main.list.get(id).stock+" available");
        }
        else{

            Main.list.get(id).stock=Main.list.get(id).stock-stock;
            if(Main.list.get(id).stock==0) Main.list.get(id).existflag=false;
            Main.saveProductsToFile();
            System.out.println("Purchased successfully");
            Product item=Main.list.get(id);
            Product p1=new Product(item.productId,item.productName,item.productPrize,stock);
            purchaseHistory.add(p1);
//            saveProductsToFile();
            CompletableFuture<Void> loadTask = CompletableFuture.runAsync(this::saveProductsToFile);
        }

    }
    void History(){
        if(purchaseHistory.isEmpty()){
            System.out.println("No purchases so far");
        }
        for(Product item:purchaseHistory){
            displayProduct(item);
        }
    }

    void displayProduct(Product item){
        if(item.existflag)
            System.out.println("ProductID: "+item.productId+" ProductName: "+item.productName+ " prize: "+item.productPrize+" quantity "+item.stock);
    }
    void viewProduct(int id){
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
    static void loadProductsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("historyOfPurchase.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Product product = new Product(Integer.parseInt(parts[0]), parts[1], Float.parseFloat(parts[2]), Integer.parseInt(parts[3]),Boolean.parseBoolean(parts[4]));
                    if(Boolean.parseBoolean(parts[4]))
                        purchaseHistory.add( product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     void saveProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("historyOfPurchase.txt"))) {
            for (Product product : purchaseHistory) {
                writer.write(product.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
