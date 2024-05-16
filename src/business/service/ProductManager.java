package business.service;

import business.data.model.Product;
import business.data.persistence.ProductDAO;

import java.io.IOException;
import java.util.List;

public interface ProductManager {
    void addProduct(Product product);

    void deleteProduct(String productName);

    List<Product> getProducts();

    void addRating(Product product,String rating);

    void updateProduct(Product product);

    boolean checkStatus() throws IOException;

    List<Product> readProductsFromFile();

    boolean checkProductName(String product_name);

    Product getProductByName(String name);

    Product getProductByIndex(int index);

    Product getProductByBrand(String brand);

    int getSize();

    ProductDAO getJson();
}
