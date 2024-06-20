import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
class UpdateProduct{
    static synchronized void updateStock(String id,int newStock){
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
        MongoCollection<Document> collection = Main.getCollection();
        Document Pricefilter = new Document("productId", id);
        Document Priceupdate = new Document("$set", new Document("productStock",newStock ));
        collection.updateOne(Pricefilter, Priceupdate);
        CompletableFuture<Void> loadTask = CompletableFuture.runAsync(Main::saveProductsToFile);
        System.out.println("sucessfully stock updated");
    }

    static synchronized void updatePrize(String id,double newPrize){
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
        MongoCollection<Document> collection = Main.getCollection();
        Document Pricefilter = new Document("productId", id);
        Document Priceupdate = new Document("$set", new Document("productPrice",newPrize ));
        collection.updateOne(Pricefilter, Priceupdate);
        CompletableFuture<Void> loadTask = CompletableFuture.runAsync(Main::saveProductsToFile);
        System.out.println("sucessfully prize updated");

    }
}
