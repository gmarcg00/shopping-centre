package business;

import java.util.ArrayList;

public class Cart {
    private ArrayList<business.Product> products;                //arraylist de productos en el carrito
    private ArrayList<Float> prices;                    //arraylist de precio asociado a cada producto del carrito
    public Cart() {
        products = new ArrayList<>();
        prices = new ArrayList<>();
    }
    public ArrayList<Product> getProducts() {
       return products;
    }
    public ArrayList<Float> getPrices() {
        return prices;
    }
    public float getPrice(int index){
        return prices.get(index);                       //devuelve el precio de un producto en concreto
    }
    public void addProduct(business.Product product, Float price){
        products.add(product);
        prices.add(price);
    }
    public void emptyCart(){
        products = new ArrayList<>();
        prices = new ArrayList<>();
    }
}
