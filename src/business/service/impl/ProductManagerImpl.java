package business.service.impl;

import business.data.model.Product;
import business.service.ProductManager;
import business.data.persistence.impl.ProductDAOAPI;
import business.data.persistence.ProductDAO;
import business.data.persistence.impl.ProductDAOJSON;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;

//falta adaptar todo a la productDAO, cambiar instancias a ProductDAO

public class ProductManagerImpl implements ProductManager {

    private final ProductDAO productDAO;

    public ProductManagerImpl(int option) {
        if(option == 1) {this.productDAO = new ProductDAOAPI();}
        else {this.productDAO = new ProductDAOJSON();}
    }

    public void addProduct(Product product) {
        productDAO.addProduct(product);
    }


    public void deleteProduct(String productName) {
        productDAO.deleteProduct(productName);
    }

    public List<Product> getProducts() {
        return productDAO.getProducts();
    }

    @Override
    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }

    @Override
    public List<Product> readProductsFromFile() {
        return productDAO.readProductsFromFile();
    }


    public boolean checkStatus() throws IOException {
        if(checkproductDAO()){
            return true;
        }else {
            if (checkProductFile()) {
                return true;
            }
        }
        return false;
    }

    public void addRating(Product product,String rating){
        product.updateReview(rating);
        productDAO.updateProduct(product);
    }

    public ProductDAO getJson(){
        return productDAO;
    }

    public boolean checkProductName(String product_name){
        //mirar si el nombre del producto ya existe en el archivo json
        //devuelve true si no existe y false si ya existe un nombre con ese producto

        for(int i=0;i<getSize();i++){
            if(productDAO.getProducts().get(i).getName().equals(product_name)){
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
            if(productDAO.getProducts().get(i).getName().equals(name)){
                return productDAO.getProducts().get(i);
            }
        }
        return null;
    }

    public Product getProductByIndex(int index){
        return productDAO.getProducts().get(index);
    }

    public int getSize(){
        return productDAO.getProducts().size();
    }

    public Product getProductByBrand(String brand){
        for(int i=0;i<getSize();i++){
            if(productDAO.getProducts().get(i).getBrand().contains(brand)){
                return productDAO.getProducts().get(i);
            }
        }
        return null;
    }

    public boolean checkproductDAO() throws IOException {
        return productDAO.checkStatus();
    }

}
