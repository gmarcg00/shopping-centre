package business.service.impl;

import business.data.model.Cart;
import business.service.CartManager;

public class CartManagerImpl implements CartManager {
    private final Cart cart;

    public CartManagerImpl() {
        this.cart= new Cart();
    }

    public float calcPrice(Cart cart){                          //calcula el precio de los productos acumulados
        float amount=0;

        for(int i=0;i<cart.getProducts().size();i++){           //recorre el arraylist de precios y los suma
            amount+=cart.getPrice(i);
        }

        return amount;
    }
    public Cart getCart(){
        return cart; //devuelve el carrito asociado
    }

    public void emptyCart(){
        cart.emptyCart();       //limpiamos arraylist de products
                                //limpiamos arraylist de precios
    }
}
