package business;

import persistence.ShopDAO;
import persistence.ShopJSONDAO;

import java.io.IOException;
import java.util.ArrayList;

//falta cambiar instancias a shopDAO

public class ShopManager {
    private final ShopJSONDAO shop_json_dao;
    private ShopDAO shopDAO;

    public void addShop(Shop shop){
        shop_json_dao.addShop(shop);
    }
    public ShopJSONDAO getJson(){
        return shop_json_dao;
    }
    public boolean checkShopName(String shop_name){
        //buscar en el archivo json shop si existe un nombre igual
        //devuelve false si ya existe y true en caso de que no

            for(int i=0;i<getSize();i++){
                if(shop_json_dao.readShopsFromFile().get(i).getName().equals(shop_name)){
                    return false;
                }
            }
            return true;
    }
    public ShopManager() throws IOException {
        shop_json_dao = new ShopJSONDAO();
        shopDAO = new ShopDAO() {
            @Override
            public void addShop(Shop shop) {

            }

            @Override
            public void writeShops(ArrayList<Shop> shopList) {

            }

            @Override
            public ArrayList<Shop> readShopsFromFile() {
                return null;
            }

            @Override
            public Shop findShopByName(String name) {
                return null;
            }

            @Override
            public void updateShopList(Shop shop) {

            }

            @Override
            public void checkShopFile() throws IOException {

            }
        };
    }
    public Shop getShopByProduct(Float price){
        //leer json y coger el shop que tenga el producto
        for(int i=0;i<getSize();i++){
            for(int k=0;k<shop_json_dao.readShopsFromFile().get(i).getCatalogue().getProducts().size();k++){
                if(shop_json_dao.readShopsFromFile().get(i).getCatalogue().getPrices().get(k).equals(price)){
                    return shop_json_dao.readShopsFromFile().get(i);
                }
            }
        }
        return null;
    }

    public Shop getShopByShopNumber(int index) {
        return shop_json_dao.readShopsFromFile().get(index);
    }
    public Shop getShopByName (String name){
        return shop_json_dao.findShopByName(name);
    }
    public int getSize(){
        return shop_json_dao.readShopsFromFile().size();
    }
    public void updateCatalogue(Shop shop){
        shop_json_dao.updateShopList(shop);
    }

}
