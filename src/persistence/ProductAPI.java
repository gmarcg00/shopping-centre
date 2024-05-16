package persistence;

import business.Product;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;

public class ProductAPI implements persistence.ProductDAO {
    private final String apiUrl;
    private final Gson gson;

    public ProductAPI() {
        this.apiUrl = "";//cambiar url al de la api
        this.gson = new Gson();
    }

    @Override
    public void addProduct(Product product) {
        try {
            URL url = new URL(apiUrl + "/products");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(gson.toJson(product));
            writer.flush();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new IOException("Failed to add product: HTTP error code " + connection.getResponseCode());
            }

            writer.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Product> getProducts() {
        ArrayList<Product> products = new ArrayList<>();

        try {
            URL url = new URL(apiUrl + "/products");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to retrieve products: HTTP error code " + connection.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            products = gson.fromJson(response.toString(), new TypeToken<ArrayList<Product>>() {}.getType());

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public boolean deleteProduct(String productName) {
        try {
            URL url = new URL(apiUrl + "/products?name=" + productName);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to delete product: HTTP error code " + connection.getResponseCode());
            }

            connection.disconnect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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
