package business.data.persistence;

import business.data.model.Shop;

import java.io.IOException;
import java.util.ArrayList;

public interface ShopDAO {
    // Agrega una tienda al almacenamiento
    void addShop(Shop shop);

    void writeShops(ArrayList<Shop> shopList);

    // Lee las tiendas desde el almacenamiento
    ArrayList<Shop> readShopsFromFile();

    // Encuentra una tienda por su nombre
    Shop findShopByName(String name);

    // Actualiza la lista de tiendas en el almacenamiento
    void updateShopList(Shop shop);

    // Verifica si existen tiendas en el almacenamiento
    void checkShopFile() throws IOException;
}
