public class PurchasedItem {
    int productId;
    String productName;
    float productPrize;
    int quantity;
    PurchasedItem(Product item,int quantity){
        this.productId=item.productId;
        this.productName=item.productName;
        this.quantity=quantity;
    }
    @Override
    public String toString() {
        return this.productId + "," + this.productName+ "," + this.productPrize + "," + this.quantity;
    }
}
