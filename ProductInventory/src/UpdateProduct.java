import java.util.*;
class UpdateProduct{
    static void updateStock(int id,int newStock){
        if(newStock<=0){
            System.out.println("Invalid Stock");
            return;
        }
        Main.list.get(id).stock=newStock;
        Main.saveProductsToFile();
        System.out.println("sucessfully stock updated");
    }

    static void updatePrize(int id,float newPrize){
        if(newPrize<=0){
            System.out.println("Invalid Stock");
            return;
        }
        Main.list.get(id).productPrize=newPrize;
        Main.saveProductsToFile();
        System.out.println("sucessfully prize updated");

    }
}
