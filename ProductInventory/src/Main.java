//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.*;
import java.io.*;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static HashMap<Integer,Product> list=new HashMap<>();
    static{
//        loadProductsFromFile();
//        Customer.loadProductsFromFile();
        System.out.println("Starting to load products asynchronously...");
        CompletableFuture<Void> loadTask = CompletableFuture.runAsync(() -> {
            loadProductsFromFile();
        });

        CompletableFuture<Void> loadCustomerTask = CompletableFuture.runAsync(() -> {
            Customer.loadProductsFromFile();
        });
    }

    private static final String FILE_NAME = "inventory.txt";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Seller seller1=new Seller();
        Customer customer1= new Customer();
        System.out.println("-----------------------------------------");
        System.out.println("WELCOME TO PRODUCT STORE");
        boolean flagtoContinue=true;
        while(flagtoContinue){
            System.out.println("-----------------------------------------");
            System.out.println("CHOOSE WHO ARE YOU \n 1.Seller \n 2.Buyer");
            int option = sc.nextInt();

            if (option == 1) {
                System.out.println("List of Choices for you:\n 1.Enter product to the inventory \n 2.View single product \n 3.List All products in inventory\n 4.Update product stock\n 5.Update product prize\n 6.Remove product from inventory \n 7.Exit");
                int choice=sc.nextInt();
                switch(choice){
                    case 1:
//                        seller1.addProduct();
                        CompletableFuture.runAsync(()->seller1.addProduct()).join();
                        break;
                    case 2:
                        System.out.println("enter the product id");
                        int id=sc.nextInt();
                        seller1.viewProduct(id);
                        break;

                    case 3:
                        seller1.viewProduct();
                        break;

                    case 4:
                        System.out.println("enter the product id");
                        int id1=sc.nextInt();
                        if(!Main.list.containsKey(id1)){
                            System.out.println("No such product exists");
                            return;
                        }
                        System.out.println("Enter new Stock");
                        int newStock=sc.nextInt();
                        UpdateProduct.updateStock(id1,newStock);
                        break;

                    case 5:
                        System.out.println("enter the product id");
                        int id2=sc.nextInt();
                        if(!Main.list.containsKey(id2)){
                            System.out.println("No such product exists");
                            return;
                        }
                        System.out.println("Enter new Prize");
                        float newPrize=sc.nextFloat();
                        UpdateProduct.updatePrize(id2,newPrize);
                        break;

                    case 6:
                        System.out.println("enter the product id");
                        int id3=sc.nextInt();
                        seller1.deleteProduct(id3);
                        break;
                    case 7:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } else if (option == 2) {
                System.out.println("List of Choices for you:\n 1.View single product \n 2.List All products in inventory\n 3. Purchase Product \n 4.History of purchase \n 5.Exit");
                int choice=sc.nextInt();
                switch(choice){

                    case 1:
                        System.out.println("enter the product id");
                        int id=sc.nextInt();
                        customer1.viewProduct(id);
                        break;

                    case 2:
                        customer1.viewProduct();
                        break;

                    case 3:
                        System.out.println("Enter the product id to purchase ");
                        int id4=sc.nextInt();
                        if(!list.containsKey(id4) || list.get(id4).existflag==false) System.out.println("Invalid id");
                        else {
                            System.out.println("Enter the no of products to purchase ");
                            int count = sc.nextInt();
                            customer1.purchase(id4, count);
                        }
                        break;
                    case 4:
                        customer1.History();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            } else {
                System.out.println("Invalid input");
            }
            System.out.println("do you want to continue:(Y/N)");
            String shouldContinue=sc.next();
            if(!shouldContinue.equals("Y") && !shouldContinue.equals("y")) flagtoContinue=false;
        }
    }
     static void loadProductsFromFile() {
        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Product product = new Product(Integer.parseInt(parts[0]), parts[1], Float.parseFloat(parts[2]), Integer.parseInt(parts[3]),Boolean.parseBoolean(parts[4]));
                    if(Boolean.parseBoolean(parts[4]))
                        list.put(product.getId(), product);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void saveProductsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Product product : list.values()) {
                writer.write(product.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
