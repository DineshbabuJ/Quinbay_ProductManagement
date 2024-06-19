import java.io.Serializable;

public class Product  {

    int productId;
    String productName;
    float productPrize;
    int stock;
    boolean existflag=true;

    Product(String name,float prize,int stock){
        System.out.println(Main.list.size());
        this.productId=Main.list.size()+1;
        this.productName=name;
        this.productPrize=prize;
        this.stock=stock;
    }
    Product(int id,String name,float prize,int stock,boolean flag){

        this.productId=id;
        this.productName=name;
        this.productPrize=prize;
        this.stock=stock;


    }
    Product(int id,String name,float prize,int stock){

        this.productId=id;
        this.productName=name;
        this.productPrize=prize;
        this.stock=stock;


    }

    public int getId() {
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
}
