package business;

import java.util.ArrayList;

public class Catalogue {

    private ArrayList<Product> products;

    private ArrayList<Float> prices;

    public Catalogue(){
        this.products=new ArrayList<>();
        this.prices=new ArrayList<>();
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Float> getPrices(){
        return prices;
    }
}
