package business;

import persistence.ProductAPI;
import persistence.ProductDAO;
import persistence.ProductJSONDAO;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//falta adaptar todo a la api, cambiar instancias a ProductDAO

public class ProductManager {
    private final ProductJSONDAO product_json_dao;
    private final ProductAPI api;
    private final ProductDAO productDAO;
    public void addRating(Product product,String rating){
        product.updateReview(rating);
        product_json_dao.updateProduct(product);
    }
    public ProductJSONDAO getJson(){
        return product_json_dao;
    }
    public boolean deleteProduct(Product product){
        return product_json_dao.deleteProduct(product.getName());
    }
    public boolean checkProductName(String product_name){
        //mirar si el nombre del producto ya existe en el archivo json
        //devuelve true si no existe y false si ya existe un nombre con ese producto

        for(int i=0;i<getSize();i++){
            if(product_json_dao.getProducts().get(i).getName().equals(product_name)){
                return false;
            }
        }
        return true;
    }
    public boolean checkProductFile() throws FileNotFoundException {
        try{
            return getJson().checkStatus();
        }catch(IOException e){
            return false;
        }
    }
    public Product getProductByName(String name){
        //si el nombre del producto encaja con el que buscamos
        //retorna null en caso que no se haya encontrado ese producto
        for(int i=0;i<getSize();i++){
            if(product_json_dao.getProducts().get(i).getName().equals(name)){
                return product_json_dao.getProducts().get(i);
            }
        }
        return null;
    }
    public Product getProductByIndex(int index){
        return product_json_dao.getProducts().get(index);
    }
    public int getSize(){
        return product_json_dao.getProducts().size();
    }
    public ProductManager(){
        this.product_json_dao = new ProductJSONDAO();
        this.api = new ProductAPI();
        this.productDAO = new ProductDAO() {
            @Override
            public void addProduct(Product product) {

            }

            @Override
            public boolean deleteProduct(String productName) {
                return false;
            }

            @Override
            public ArrayList<Product> getProducts() {
                return null;
            }

            @Override
            public void updateProduct(Product product) {

            }

            @Override
            public boolean checkStatus() throws IOException {
                return false;
            }

            @Override
            public ArrayList<Product> readProductsFromFile() {
                return null;
            }
        };
    }
    public Product getProductByBrand(String brand){
        for(int i=0;i<getSize();i++){
            if(product_json_dao.getProducts().get(i).getBrand().contains(brand)){
                return product_json_dao.getProducts().get(i);
            }
        }
        return null;
    }
    public boolean checkStatus() throws IOException {
        if(checkApi()){
            return true;
        }else {
            if (checkProductFile()) {
                return true;
            }
        }
        return false;
    }
    public boolean checkApi() throws IOException {
        return api.checkStatus();
    }

}
