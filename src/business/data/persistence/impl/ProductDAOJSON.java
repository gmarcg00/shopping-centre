package business.data.persistence.impl;

import business.data.model.Product;
import business.data.persistence.ProductDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.lang.reflect.Type;
import java.util.List;

public class ProductDAOJSON implements ProductDAO {
    private ArrayList<Product> productList;
    private Gson gson;
    private final Path file_path;

    public ProductDAOJSON() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        file_path = Paths.get("info/product.json");
        productList = readProductsFromFile();
    }

    // Permite escribir en el json (teniendo en cuenta todos los atributos necesarios)
    public void addProduct(Product product){
        productList.add(product);
        writeProducts(productList);
    }

    //escribe en el json, no tocar
    public void writeProducts(ArrayList<Product> productList) {
        try (FileWriter writer = new FileWriter(String.valueOf(file_path))) {
            gson.toJson(productList, writer);
        } catch (IOException e) {
            System.out.println("ERROR couldn't create a new product");
        }
    }

    // Nos sirve para poder extraer toda la lista de productos
    // Nos permite sacar directamente el producto que necesitamos
    // Para controlar si existe o no
    public boolean checkStatus() throws IOException {
        File file = new File(file_path.toUri());
        return file.exists();   // Con este comando comprobamos is existe o no y devolvemos si existe o no
    }

    public List<Product> getProducts() {
        return readProductsFromFile();
    }

    public void deleteProduct(String productName) {
        // Leer la lista actual de productos desde el archivo
        boolean found = false;

        // Encontrar el producto por nombre y eliminarlo
        for (int i = 0; i < productList.size() && !found ; i++) {
            if (productList.get(i).getName().equals(productName)) {
                productList.remove(i);
                 //encontrar y eliminar el producto
                found=true;
                System.out.println("Producto eliminado");
            }
        }

        // Escribir la lista actualizada de productos de nuevo en el archivo
        writeProducts(productList);
    }

    public ArrayList<Product> readProductsFromFile() {
        try (Reader reader = new FileReader(file_path.toFile())) {
            Type type = new TypeToken<List<Product>>(){}.getType();
            productList = gson.fromJson(reader, type);
            return productList;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void updateProduct(Product product){
        for(int i = 0; i < productList.size();i++){
            if(productList.get(i).getName().equals(product.getName())){
                productList.set(i,product);
                writeProducts(productList);
                break;
            }
        }
    }
}