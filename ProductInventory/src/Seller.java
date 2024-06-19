import java.util.*;
import java.util.concurrent.CompletableFuture;

public class Seller {

    Scanner sc = new Scanner(System.in);

    void addProduct(){
        System.out.println("Enter product name:");
        String name=sc.next();
        System.out.println("Enter product price:");
        float prize=sc.nextFloat();
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
        Product p1=new Product(name,prize,stock);
        Main.list.put(p1.productId,p1);

        CompletableFuture<Void> loadTask = CompletableFuture.runAsync(Main::saveProductsToFile);
        System.out.println("Added successfully");
    }
    void displayProduct(Product item){
        if(item.existflag)
            System.out.println("ProductID: "+item.productId+" ProductName: "+item.productName+ " prize: "+item.productPrize+" stock available: "+item.stock+" exist:"+item.existflag);
    }
    void viewProduct(int id){
        if(Main.list.containsKey(id)){
            this.displayProduct(Main.list.get(id));
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

    void deleteProduct(int id){
        if(Main.list.containsKey(id)){
            Main.list.get(id).existflag=false;
            CompletableFuture<Void> save = CompletableFuture.runAsync(Main::saveProductsToFile);
            System.out.println("Removed successfully");
        }
        else{
            System.out.println("No such Product ");
        }
    }

}
