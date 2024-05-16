package persistence;

import business.Shop;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;

public class ShopAPI implements persistence.ShopDAO {
    private final String apiUrl;
    private final Gson gson;

    public ShopAPI() {
        this.apiUrl = "";//cambiar URL al de la api
        this.gson = new Gson();
    }

    @Override
    public void checkShopFile() {
        // Este método no es necesario en la implementación de la API
    }

    @Override
    public void addShop(Shop shop) {
        try {
            URL url = new URL(apiUrl + "/shops");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(gson.toJson(shop));
            writer.flush();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                throw new IOException("Failed to add shop: HTTP error code " + connection.getResponseCode());
            }

            writer.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeShops(ArrayList<Shop> shopList) {
    }
    @Override
    public ArrayList<Shop> readShopsFromFile() {
        ArrayList<Shop> shops = new ArrayList<>();

        try {
            URL url = new URL(apiUrl + "/shops");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to retrieve shops: HTTP error code " + connection.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            shops = gson.fromJson(response.toString(), new TypeToken<ArrayList<Shop>>() {}.getType());

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return shops;
    }

    @Override
    public Shop findShopByName(String name) {
        Shop shop = null;

        try {
            URL url = new URL(apiUrl + "/shops?name=" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to retrieve shop: HTTP error code " + connection.getResponseCode());
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            shop = gson.fromJson(response.toString(), Shop.class);

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return shop;
    }

    @Override
    public void updateShopList(Shop shop) {
        try {
            URL url = new URL(apiUrl + "/shops/" );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(gson.toJson(shop));
            writer.flush();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to update shop: HTTP error code " + connection.getResponseCode());
            }

            writer.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
