package business.data.model;

import java.util.ArrayList;
import java.util.List;

public class Catalogue {

    private List<Product> products;

    private List<Float> prices;

    public Catalogue(){
        this.products = new ArrayList<>();
        this.prices = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Float> getPrices(){
        return prices;
    }
}
