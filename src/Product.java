//import java.io.Serializable;
import org.bson.Document;
public class Product  {

    String productId;
    String productName;
    double productPrize;
    int stock;
    boolean existflag=true;
    String categoryId;

    Product(String name,double prize,int stock,String categoryId){
        System.out.println(Main.list.size());
        this.productId="Prod"+(Main.list.size()+1);
        this.productName=name;
        this.productPrize=prize;
        this.stock=stock;
        this.categoryId=categoryId;
    }
    Product(String id,String name,double prize,int stock,boolean flag){

        this.productId=id;
        this.productName=name;
        this.productPrize=prize;
        this.stock=stock;
        this.existflag=flag;
    }
    Product(String id,String name,double prize,int stock){

        this.productId=id;
        this.productName=name;
        this.productPrize=prize;
        this.stock=stock;
    }
    Product(String id,String name,double prize,int stock,String cat){

        this.productId=id;
        this.productName=name;
        this.productPrize=prize;
        this.stock=stock;
        this.categoryId=cat;
    }

    public String getId() {
        return this.productId;
    }


    public String getName() {
        return this.productName;
    }

    public void setName(String name) {
        this.productName = name;
    }

    public double getPrice() {
        return this.productPrize;
    }

    public void setPrice(float price) {
        this.productPrize = price;
    }

    public int getQuantity() {
        return this.stock;
    }

    public void setQuantity(int quantity) {
        this.stock = quantity;
    }
    @Override
    public String toString() {
        return this.productId + "," + this.productName+ "," + this.productPrize + "," + this.stock+","+this.existflag;
    }
    public static Product fromDocument(Document doc) {
        String id = doc.getString("productId");
        String name = doc.getString("productName");
        double price = doc.containsKey("productPrice") ? doc.getDouble("productPrice") : 0.0;
        int quantity = doc.containsKey("productStock") ? doc.getInteger("productStock") : 0;
        String Category=doc.getString("categoryId");
        return new Product(id, name, price, quantity,Category);
    }
}
