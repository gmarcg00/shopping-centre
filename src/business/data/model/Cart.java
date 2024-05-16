package business.data.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<Product> products;
    //arraylist de productos en el carrito
    private List<Float> prices;
    //arraylist de precio asociado a cada producto del carrito
    public Cart() {
        products = new ArrayList<>();
        prices = new ArrayList<>();
    }

    public float getPrice(int index){
        return prices.get(index);                       //devuelve el precio de un producto en concreto
    }

    public void addProduct(Product product, Float price){
        products.add(product);
        prices.add(price);
    }

    public void emptyCart(){
        products = new ArrayList<>();
        prices = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Float> getPrices() {
        return prices;
    }
}
