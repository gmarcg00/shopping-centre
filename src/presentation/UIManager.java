package presentation;

import business.data.model.Catalogue;
import business.data.model.Product;
import business.data.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class UIManager{

    public void apiOrJson(){
        System.out.println("\n\t1) Use API\n" +
                "\t2) Use JSON\n" +
                "\nChoose an option: ");
    }

    public void showMessage (String message) {
        System.out.print(message);
    }

    public void searchProducts(){
        System.out.println("Enter your query: ");
    }

    public void listProducts(List<Product> products){

        System.out.println("These are the currently available products:\n");

        for(int i=0; i< products.size();i++){
            System.out.println("\t" + (i+1) + ") " + products.get(i).getName() + " by " + products.get(i).getBrand());
        }

        System.out.println("\n\t" + (products.size()+1) + ") Back\n");
        System.out.println("Which one would you like to remove? ");
    }
    public void showFoundProduct(Product product,int num_products){
        System.out.println("\t"+num_products+") "+product.getName()+" by "+product.getBrand());
    }
    public void subMenuShops(){
        System.out.println("\n\t1) Create a Shop\n" +
                "\t2) Expand a Shop’s Catalogue\n" +
                "\t3) Reduce a Shop’s Catalogue\n" +
                "\n\t4) Back\n" +
                "\nChoose an option: ");
    }
    public void subMenuProducts(){
        System.out.println("\t1) Create a Product\n" +
                "\t2) Remove a Product\n" +
                "\n\t3) Back\n" +
                "\nChoose an option: ");
    }
    public void startMenu(){
        System.out.println("         ________ ____\n" +
                "  ___ / / ____/___ / __/_______\n" +
                " / _ \\/ / / / __ \\/ /_/ ___/ _ \\\n" +
                "/ __/ / /___/ /_/ / __/ / / __/\n" +
                "\\___/_/\\____/\\____/_/ /_/ \\___/\n" +
                "\nWelcome to elCofre Digital Shopping Experiences.\n\n");
    }
    public void startMenuAccess(){              //añadir al starUML
        System.out.println("\n\t1) Manage Products"+
                "\n\t2) Manage Shops"+
                "\n\t3) Search Products"+
                "\n\t4) List Shops"+
                "\n\t5) Your Cart"+
                "\n\n\t6) Exit"+
                "\n\nChoose a Digital Shopping Experience: ");
    }
    public void listShops(int size,ArrayList<Shop> shops){
        System.out.println("The elCofre family is formed by the following shops:\n");
        for(int i=0;i<size;i++){
            System.out.println("\t"+(i+1)+") "+shops.get(i).getName());//falta asignar el shop correspondiente a cada iteración
        }
        System.out.println("\n"+(size+1)+") Back\n\nWhich catalogue do you want to see? ");//el numero antes de la opción back del submenu depende del numero de shops

    }
    public void startMenuError(){               //añadir al starUML
        System.out.println("Error: The shops.json file can’t be accessed.\n" +
                "Shutting down...");
    }
    public void checkClearCart(){
        System.out.println("Are you sure you want to clear your cart? ");
    }
    public void showCart(List<Product> cart_products, List<Float> cart_prices){
        System.out.println("Your cart contains the following items:");
        for(int i=0;i<cart_products.size();i++){
            System.out.println("\n\t- "+cart_products.get(i).getName()+" by "+cart_products.get(i).getBrand()+"\n\t  Price: "+cart_prices.get(i)+"\n");
        }
    }
    public void clearCart(){
        System.out.println("\nYour cart has been cleared. ");
    }
    public void checkOut(){
        System.out.println("Are you sure you want to check out? ");
    }
    public void showCartSubMenu(){
        System.out.println("\n\t1) Checkout\n\t2) Clear cart\t\n\n3) Back\n\nChoose an option: ");
    }
    public void showTotal(float amount){
        System.out.println("Total: "+amount);
    }
    public void exit(){
        System.out.println("We hope to see you again!");
    }
    public void showCatalogue(Catalogue catalogue){
        for(int i=0;i<catalogue.getProducts().size();i++){
            System.out.println("\n\t"+(i+1)+") \""+catalogue.getProducts().get(i).getName()+"\" by \""+catalogue.getProducts().get(i).getBrand()+"\"\n\t  Price: "+catalogue.getPrices().get(i));
        }
    }
    public void subMenuCatalogue(){
        System.out.println("\t1) Read reviews\n\t2) Review product\n\t3) Add to cart\n\nChoose an option: ");
    }
    public void deletedProduct(String name,String brand){
        System.out.println("\n" + name + " by " + brand + " has been withdrawn from sale.\n");
    }
    public void creaTenda(){
        System.out.println("The system supports the following business models:\n" +
                "\n" +
                "\tA) Maximum Benefits\n" +
                "\tB) Loyalty\n" +
                "\tC) Sponsored\n");
    }
    public void reviews(){
        System.out.println("\nWhich one would you like to review?");
    }
    public void reviewMenu(){
        System.out.println("\n\t1) Read reviews\n\t2) Review Product\n\nChoose an option: ");
    }
    public void showFoundShop(String name,float price){
        System.out.println("\t\t- "+name+": "+price);
    }
    public void addedToCart(String name, String brand){
        System.out.println("1x "+name+" by "+brand+" has been added to your cart");
    }
    public void showReviews(String name, String brand, String reviews){
        System.out.println("These are the reviews for " + name + " by " + brand + ":\n");
        System.out.println(reviews);
    }
    public void showShopDescription(String shop_name,String description, int year){
            showMessage(shop_name+" - Since " + year + "\n"+description+"\n");
    }
    public void showShopRevenue(Shop shop,float amount){
        System.out.println(shop.getName()+" has earned " + amount + ", for an historic total of "+ shop.getTotalRevenue() + ".");
    }
    public void createProduct(){
            System.out.println("\tA) General\n\tB) Reduced Taxes\n\tC) Superreduced Taxes\n");
        }
    public void startMenuApi(){
        System.out.println("Checking API Status...");
    }
    public void startMenuApiError(){
        System.out.println("Error: The API isn’t available\n");
    }
    public void startMenuJson(){
        System.out.println("Verifying local files...");
    }
}