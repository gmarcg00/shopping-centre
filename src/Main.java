import api.Server;
import business.ShopManager;
import business.CartManager;
import business.ProductManager;
import presentation.Controller;
import presentation.UIManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Server.start();
        ShopManager shop_manager = new ShopManager();
        ProductManager product_manager = new ProductManager();
        CartManager cart_manager = new CartManager();
        UIManager ui_manager = new UIManager();
        Controller controller = new Controller(ui_manager, shop_manager,product_manager,cart_manager);
        controller.run();
    }
}