package business.data.persistence;

import business.data.model.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ProductDAO {

    // Permite obtener todos los productos almacenados
    List<Product> getProducts();

    // Permite agregar un producto al almacenamiento
    void addProduct(Product product);

    // Permite actualizar un producto en el almacenamiento
    void updateProduct(Product product);

    // Permite eliminar un producto del almacenamiento
    void deleteProduct(String productName);

    // Verifica si existen productos almacenados
    boolean checkStatus() throws IOException;

    ArrayList<Product> readProductsFromFile();
}
