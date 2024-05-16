package persistence;

import business.Product;

import java.io.IOException;
import java.util.ArrayList;

public interface ProductDAO {
    // Permite agregar un producto al almacenamiento
    void addProduct(Product product);

    // Permite eliminar un producto del almacenamiento
    boolean deleteProduct(String productName);

    // Permite obtener todos los productos almacenados
    ArrayList<Product> getProducts();

    // Permite actualizar un producto en el almacenamiento
    void updateProduct(Product product);

    // Verifica si existen productos almacenados
    boolean checkStatus() throws IOException;

    ArrayList<Product> readProductsFromFile();
}
