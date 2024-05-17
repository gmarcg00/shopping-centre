package business.data.persistence.impl;

import api.Server;
import business.data.model.Product;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.google.gson.reflect.TypeToken;
import edu.salle.url.api.exception.ApiException;
import utils.JsonMapper;

public class ProductDAOAPI implements business.data.persistence.ProductDAO {
    private final String apiUrl;
    private final Server server;

    public ProductDAOAPI() {
        this.apiUrl = "https://balandrau.salle.url.edu/dpoo/S1_Project_190/products";//cambiar url al de la api
        this.server = new Server();
    }

    public List<Product> getProducts() {
        try {
            String productsResponse = server.getApiHelper().getFromUrl(apiUrl);
            return JsonMapper.getMapper().fromJson(productsResponse, new TypeToken<ArrayList<Product>>() {}.getType());
        } catch (ApiException e) {
            System.out.println("Failed to retrieve products: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public void addProduct(Product product) {
        try{
            server.getApiHelper().postToUrl(apiUrl, JsonMapper.getMapper().toJson(product));
        }catch (ApiException e){
            System.out.println("Failed to add product: " + e.getMessage());
        }
    }

    public void deleteProduct(String productName) {
        try {
            server.getApiHelper().deleteFromUrl(apiUrl + "?name=" + productName);
        }catch (ApiException e) {
            System.out.println("Failed to delete product: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(JsonMapper.getMapper().toJson(product));
            writer.flush();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to update product: HTTP error code " + connection.getResponseCode());
            }

            writer.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkStatus() throws IOException {
       return true;
    }

    @Override
    public ArrayList<Product> readProductsFromFile() {
        //falta implementar
        return null;
    }
}
