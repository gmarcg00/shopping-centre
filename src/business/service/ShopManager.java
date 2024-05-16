package business.service;

import business.data.model.Shop;
import business.data.persistence.ShopDAO;

import java.util.ArrayList;

public interface ShopManager {
    void addShop(Shop shop);
    boolean deleteShop(String shopName);
    int getSize();
    ArrayList<Shop> getShops();
    void updateShop(Shop shop);
    boolean checkStatus();
    ArrayList<Shop> readShopsFromFile();
    void writeShops(ArrayList<Shop> shopList);
    ShopDAO getJson();
    boolean checkShopName(String shop_name);
    Shop getShopByProduct(Float price);
    Shop getShopByShopNumber(int index);
    Shop getShopByName (String name);
    void updateCatalogue(Shop shop);
}
