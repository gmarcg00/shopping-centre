package persistence;

import business.Shop;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ShopJSONDAO implements ShopDAO{

    private ArrayList<Shop> shopList;
    private final Gson gson;
    private final Path file_path;

    public ShopJSONDAO() throws IOException {
        gson = new GsonBuilder().setPrettyPrinting().create();
        file_path = Paths.get("info/shop.json");
        shopList = readShopsFromFile();
        checkShopFile();
    }
    public void checkShopFile() throws IOException {
        if(!new File(file_path.toUri()).exists()){
            new FileWriter(String.valueOf(file_path));
        }
    }
    public void addShop(Shop shop){
        shopList.add(shop);
        writeShops(shopList);
    }

    public void writeShops(ArrayList<Shop> shopList) {
        try (FileWriter writer = new FileWriter(file_path.toFile())) {
            gson.toJson(shopList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Shop> readShopsFromFile() {
        ArrayList<Shop> shopList = new ArrayList<>();
        try (Reader reader = new FileReader(file_path.toFile())) {
            Type type = new TypeToken<ArrayList<Shop>>(){}.getType();
            shopList = gson.fromJson(reader, type);
            return shopList;
        } catch (IOException e) {
            writeShops(shopList);
            return shopList;
        }
    }

    public Shop findShopByName(String name){
        for(int i=0;i<shopList.size();i++){
            if(shopList.get(i).getName().equals(name)){
                return shopList.get(i);
            }
        }
        return null;
    }
    public void updateShopList(Shop shop){
        for(int i=0;i<shopList.size();i++){
            if(shop.getName().equals(shopList.get(i).getName())){
                shopList.set(i,shop);
                writeShops(shopList);
                break;
            }
        }
    }
}