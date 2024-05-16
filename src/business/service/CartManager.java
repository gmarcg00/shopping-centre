package business.service;

import business.data.model.Cart;

public interface CartManager {
    float calcPrice(Cart cart);
    Cart getCart();
    void emptyCart();
}
