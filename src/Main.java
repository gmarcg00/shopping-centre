
import business.service.impl.CartManagerImpl;
import presentation.Controller;
import presentation.UIManager;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        CartManagerImpl cartManager = new CartManagerImpl();
        UIManager uiManager = new UIManager();
        Controller controller = new Controller(uiManager,cartManager);
        controller.run();
    }
}