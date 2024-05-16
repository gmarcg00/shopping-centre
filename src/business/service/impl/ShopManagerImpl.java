package business.service.impl;

import business.data.model.Shop;
import business.service.ShopManager;
import business.data.persistence.impl.ShopDAOAPI;
import business.data.persistence.ShopDAO;
import business.data.persistence.impl.ShopDAOJSON;

import java.io.IOException;
import java.util.ArrayList;

//falta cambiar instancias a shopDAO

public class ShopManagerImpl implements ShopManager {

    private final ShopDAO shopDAO;

    public ShopManagerImpl(int option) throws IOException {
        if(option == 1){this.shopDAO = new ShopDAOAPI();}
        else{this.shopDAO = new ShopDAOJSON();}
    }

    public void addShop(Shop shop){
        shopDAO.addShop(shop);
    }

    @Override
    public boolean deleteShop(String shopName) {
        return false;
    }

    public ShopDAO getJson(){
        return shopDAO;
    }

    public boolean checkShopName(String shop_name){
        //buscar en el archivo json shop si existe un nombre igual
        //devuelve false si ya existe y true en caso de que no

            for(int i=0;i<getSize();i++){
                if(shopDAO.readShopsFromFile().get(i).getName().equals(shop_name)){
                    return false;
                }
            }
            return true;
    }

    public Shop getShopByProduct(Float price){
        //leer json y coger el shop que tenga el producto
        for(int i=0;i<getSize();i++){
            for(int k = 0; k< shopDAO.readShopsFromFile().get(i).getCatalogue().getProducts().size(); k++){
                if(shopDAO.readShopsFromFile().get(i).getCatalogue().getPrices().get(k).equals(price)){
                    return shopDAO.readShopsFromFile().get(i);
                }
            }
        }
        return null;
    }

    public Shop getShopByShopNumber(int index) {
        return shopDAO.readShopsFromFile().get(index);
    }
    public Shop getShopByName (String name){
        return shopDAO.findShopByName(name);
    }
    public int getSize(){
        return shopDAO.readShopsFromFile().size();
    }

    @Override
    public ArrayList<Shop> getShops() {
        return null;
    }

    @Override
    public void updateShop(Shop shop) {

    }

    @Override
    public boolean checkStatus() {
        return false;
    }

    @Override
    public ArrayList<Shop> readShopsFromFile() {
        return null;
    }

    @Override
    public void writeShops(ArrayList<Shop> shopList) {

    }

    public void updateCatalogue(Shop shop){
        shopDAO.updateShopList(shop);
    }

}
