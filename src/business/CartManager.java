package business;

public class CartManager {
    private final business.Cart cart;
    public CartManager() {
        this.cart= new business.Cart();
    }

    public float calcPrice(business.Cart cart){                          //calcula el precio de los productos acumulados
        float amount=0;

        for(int i=0;i<cart.getProducts().size();i++){           //recorre el arraylist de precios y los suma
            amount+=cart.getPrice(i);
        }

        return amount;
    }
    public business.Cart getCart(){
        return cart;                        //devuelve el carrito asociado
    }
    public void emptyCart(){

        cart.emptyCart();       //limpiamos arraylist de products
                                //limpiamos arraylist de precios
    }
}
