package business.data.persistence.impl;

import api.Server;
import business.data.model.Product;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.google.gson.reflect.TypeToken;
import edu.salle.url.api.exception.ApiException;

public class ProductDAOAPI implements business.data.persistence.ProductDAO {
    private String apiUrl;
    private final Gson gson;
    private Server server;

    public ProductDAOAPI() {
        this.apiUrl = "https://balandrau.salle.url.edu/dpoo";//cambiar url al de la api
        this.gson = new Gson();
        this.server = new Server();
    }

    public List<Product> getProducts() {
        try {
            String productsResponse = server.getApiHelper().getFromUrl(apiUrl + "/products");
            return gson.fromJson(productsResponse, new TypeToken<ArrayList<Product>>() {}.getType());
        } catch (ApiException e) {
            System.out.println("Failed to retrieve products: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public void addProduct(Product product) {
        try{
            server.getApiHelper().postToUrl(apiUrl + "/products", gson.toJson(product));
        }catch (ApiException e){
            System.out.println("Failed to add product: " + e.getMessage());
        }
    }

    public void deleteProduct(String productName) {
        try {
            server.getApiHelper().deleteFromUrl(apiUrl + "/products?name=" + productName);
        }catch (ApiException e) {
            System.out.println("Failed to delete product: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) {
        try {
            URL url = new URL(apiUrl + "/products/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(gson.toJson(product));
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
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<Product> readProductsFromFile() {
        //falta implementar
        return null;
    }
}
