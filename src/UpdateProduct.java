import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.CompletableFuture;

class UpdateProduct{
    static synchronized void updateStock(int id,int newStock){
        if(!Main.list.get(id).existflag){
            System.out.println("Already deleted");
            return;
        }
        if(newStock<=0){
            System.out.println("Invalid Stock");
            return;
        }
        Main.list.get(id).stock=newStock;
//        Main.saveProductsToFile();
        CompletableFuture<Void> loadTask = CompletableFuture.runAsync(Main::saveProductsToFile);
        System.out.println("sucessfully stock updated");
    }

    static synchronized void updatePrize(int id,float newPrize){
        if(!Main.list.get(id).existflag){
            System.out.println("Already deleted");
            return;
        }
        if(newPrize<=0){
            System.out.println("Invalid Stock");
            return;
        }
        Main.list.get(id).productPrize=newPrize;
//        Main.saveProductsToFile();
        CompletableFuture<Void> loadTask = CompletableFuture.runAsync(Main::saveProductsToFile);
        System.out.println("sucessfully prize updated");

    }
}
