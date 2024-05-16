package presentation;

import business.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {
    private final presentation.UIManager ui_manager;
    private final ShopManager shop_manager;
    private final ProductManager product_manager;
    private final CartManager cart_manager;
    Scanner scanner = new Scanner(System.in);

    public void run() throws IOException {
        int option;                     //opción del menu principal
        int sub_menu_option;            //opción de cualquier submenu

        ui_manager.startMenu();         //ejecutar menu bienvenida
        ui_manager.startMenuApi();

       // if(!product_manager.checkApi()){
       //     ui_manager.startMenuApiError();
//
       //     if(!product_manager.checkProductFile()){
       //         ui_manager.startMenuError();
//
       //     }else{ui_manager.startMenuJson();}
//
       // }

        //if(product_manager.checkStatus()){
            ui_manager.showMessage("Starting program...\n");
            do {
                ui_manager.startMenuAccess();               //ejecutar menu de bienvenida
                option = askForInteger(6);
                switch (option) {                         //diferentes opciones del menu principal
                    case 1:
                        do {
                            ui_manager.subMenuProducts();
                            sub_menu_option = askForInteger(3);//ejecutar el submenu de Products y asociar un valor
                            switch (sub_menu_option) {                //en función de sub_menu_option se ejecuta un apartado u otro
                                case 1:
                                    //crear producto nuevo
                                    newProduct();
                                    break;
                                case 2:
                                    //borrar producto existente si hay productos
                                    if(!product_manager.getJson().getProducts().isEmpty()) {
                                        deleteProduct();                //falta implementar y falta parametro
                                    }else{
                                        ui_manager.showMessage("(ERROR) There are no products to delete\n\n");
                                    }
                                    break;
                            }
                        }while(sub_menu_option!=3);
                        break;
                    case 2:
                        do{
                        ui_manager.subMenuShops();
                        sub_menu_option=askForInteger(4);
                            switch (sub_menu_option) {
                                case 1:
                                    creaTenda();                         //escribe una tienda en la persistencia
                                    break;
                                case 2:
                                    if(shop_manager.getSize()>0) {
                                        expandCatalogue();
                                    }else{
                                        ui_manager.showMessage("(ERROR) There are no shops yet\n");
                                    }
                                    break;
                                case 3:
                                    if(shop_manager.getSize()>0) {
                                        reduceShopCatalogue();
                                    }else{
                                        ui_manager.showMessage("(ERROR) There are no shops yet\n");
                                    }
                                    break;
                            }
                        }while(sub_menu_option!=4);
                        break;
                    case 3:
                        if(product_manager.getSize()>0) {
                            searchProducts();
                        }else{ui_manager.showMessage("(ERROR) The products file is empty\n");}
                            break;
                    case 4:
                        listShops();
                        break;
                    case 5:
                        yourCart();
                        break;
                    case 6:
                        ui_manager.exit();
                        break;
                }
            } while (option != 6);
        //}
    }
    public void yourCart() {
        int sub_menu_option;
        String check_out;
        if(!cart_manager.getCart().getProducts().isEmpty()){

        ui_manager.showCart(cart_manager.getCart().getProducts(),cart_manager.getCart().getPrices());
        ui_manager.showTotal(cart_manager.calcPrice(cart_manager.getCart())); //por confirmar(mostrar total carrito)

            do{

                ui_manager.showCartSubMenu();
                sub_menu_option=askForInteger(3);

            switch (sub_menu_option){
                case 1:
                    ui_manager.checkOut();
                    check_out = askForString();
                        if(check_out.equalsIgnoreCase("Yes")){
                            buy(cart_manager.getCart().getPrices(),cart_manager.getCart().getProducts());
                            cart_manager.emptyCart();
                            ui_manager.clearCart();
                        }
                    break;
                case 2:
                    ui_manager.checkClearCart();
                    check_out = askForString();
                        if(check_out.equalsIgnoreCase("Yes")) {
                            cart_manager.emptyCart();
                            ui_manager.clearCart();
                        }
                    break;
            }
        }while(sub_menu_option!=3 && !cart_manager.getCart().getProducts().isEmpty());
        }else{ui_manager.showMessage("Your cart is empty\n");}
    }
    public void buy(ArrayList<Float> prices, ArrayList<Product> products) {

        Product product;
        float price;
        Shop shop;

        ArrayList<Shop> updated_shops = new ArrayList<>();
        ArrayList<Float> amount_updated = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {

            product = products.get(i);
            price = prices.get(i);
            shop = shop_manager.getShopByProduct(price);

            if (shop != null ) {

                //falta una condición para

                float average_rating = calcAverageRating(product.getRating());

                //update amount spent by a client in this shop (before IVA for total shop revenue)
                shop.updateTotalSpentByClient(price);

                //iva substraction for actual maximum amount earned by the shop
                price = checkIva(price,product.getCategory(),average_rating);
                shop.updateTotalRevenue(price);

                shop_manager.getJson().updateShopList(shop);
                prices.set(i,price);

                    int index = updated_shops.indexOf(shop);

                    if (updated_shops.contains(shop)) {

                        float newAmount = amount_updated.get(index) + price;
                        amount_updated.set(index, newAmount);

                        updated_shops.remove(shop);
                        amount_updated.remove(updated_shops.indexOf(shop));
                    } else {
                        updated_shops.add(shop);
                        amount_updated.add(price);
                    }
                    // Update total revenue of the shop
            }
        }

        //encontrar tiendas repetidas y quitarlas para que solo aparezcan una vez junto a la suma de precios

        for(int i=0;i<updated_shops.size();i++){

            Shop definitive_shop = updated_shops.get(i);

            for(int j=1;j<updated_shops.size();j++){
                if(definitive_shop.getName().equals(updated_shops.get(j).getName())){
                    updated_shops.set(i,updated_shops.get(j));
                    amount_updated.set(i,amount_updated.get(j)+amount_updated.get(i));
                    updated_shops.remove(j);
                    amount_updated.remove(j);
                }
            }
            ui_manager.showShopRevenue(updated_shops.get(i), amount_updated.get(i));
        }

        }
        public float checkIva(float price,char category,float average_rating){

            if(category == 'a' || category == 'A'){
                return (float) (price-(price*0.21));
            }
            if(category == 'b' || category == 'B'){
                if(average_rating<3.5) {
                    return (float) (price - (price * 0.1));
                }else{
                    return (float) (price - (price * 0.05));
                }
            }
            if(category == 'c' || category == 'C'){
                if(price<100) {
                    return (float) (price - (price * 0.04));
                }else{
                    return price;
                }
            }
            return -1;
        }
    public String askForString(){
        String string="";
        try {
            string = scanner.nextLine();
        } catch (Exception e) {
            ui_manager.showMessage("You must introduce a valid option");
        }
        //convertir a mayuscula la primera letra
        String aux = string.substring(0,1).toUpperCase() + string.substring(1);
        return aux;

    }
    public void newProduct(){
        String product_name;

        //comprobar que el nombre del producto no exista en el archivo
        do{
            ui_manager.showMessage("Please enter the product’s name: ");
            product_name = askForString();
            if(!product_manager.checkProductName(product_name)) {
                ui_manager.showMessage("This product already exists.\n");
            }
        }while(!product_manager.checkProductName(product_name));

        ui_manager.showMessage("Please enter the product’s brand: ");
        String brand_name = askForString();
        ui_manager.showMessage("Please enter the product’s maximum retail price: ");
        float amount = askForFloat();
        ui_manager.showMessage("The system supports the following product categories: \n\n");
        ui_manager.createProduct();
        char category=askForCharacter("Please pick the product’s category: ");
        Product product = new Product(product_name,amount,brand_name,category,"");
        product_manager.getJson().addProduct(product);
        ui_manager.showMessage("The product \""+product_name+"\" by \""+brand_name+"\" was added to the system. \n\n");
    }
    public void deleteProduct(){
        int option;

        do {

            ui_manager.listProducts(product_manager.getJson().getProducts());         //printear todos los productos del json falta implementar listProducts()
            option = askForInteger(product_manager.getSize() + 1);

        }while( option<1 || option > product_manager.getSize()+1);

        if(option<product_manager.getSize()+1) {

            ui_manager.showMessage("Are you sure you want to remove " + product_manager.getProductByIndex(option - 1).getName() + " by " + product_manager.getProductByIndex(option - 1).getBrand() + "? ");

            if (askForString().equalsIgnoreCase("Yes")){
                for(int i = 0 ;i<shop_manager.getSize();i++){
                    for(int j=0;j<shop_manager.getShopByShopNumber(i).getCatalogue().getProducts().size();j++){
                        if(shop_manager.getShopByShopNumber(i).getCatalogue().getProducts().get(j).getName().equals(product_manager.getProductByIndex(option - 1).getName())){
                            Shop shop = shop_manager.getShopByProduct(shop_manager.getShopByShopNumber(i).getCatalogue().getPrices().get(j));
                            shop.reduceCatalogue(j);
                            shop_manager.updateCatalogue(shop);
                        }
                    }
                }
                ui_manager.deletedProduct(product_manager.getProductByIndex(option - 1).getName(), product_manager.getProductByIndex(option - 1).getBrand());
                product_manager.deleteProduct(product_manager.getProductByIndex(option - 1));
            }
        }
    }
    public char askForCharacter(String string){
            char option;
            do {
                ui_manager.showMessage(string);
                option = scanner.nextLine().charAt(0);
            }while(option != 'a' && option!= 'b' && option!='c' && option!='A' && option!='B' && option!='C');

            return option;
    }
    public int askForInteger(int parameter) {            //parameter es para poder reutilizar la función en cada submenu
        int option=0;                                     //para no tener que hacer una función en función de las opciones distintas que aporte un submenu
        try {
            option = Integer.parseInt(scanner.nextLine());

            if (option > 0 && option <= parameter) {          //si el numero es valido
                return option;
            } else {
                ui_manager.showMessage("\tPlease enter a number from 1 to "+parameter+"\n");    //numero no valido
            }
        } catch (NumberFormatException e) {
            ui_manager.showMessage("\n\tYou must introduce a number\n");                //no ha introducido un numero
            ui_manager.showMessage("\tPlease enter a number from 1 to " + parameter + ": \n");
        }
        return option;
    }
    public int askForInt(){
        int option=0;                                     //para no tener que hacer una función en función de las opciones distintas que aporte un submenu
        try {
            option = Integer.parseInt(scanner.nextLine());
            if (option > 0 && option<2025) {          //si el numero es valido
                return option;
            } else {
                ui_manager.showMessage("Please enter a number bigger than 0 and lower than 2025: ");    //numero no valido
            }
        } catch (NumberFormatException e) {
            ui_manager.showMessage("\tYou must introduce a number\n");                //no ha introducido un numero
            ui_manager.showMessage("\tPlease enter a positive number\n\n");
        }
        return option;
    }
    public Controller(UIManager ui_manager, ShopManager shop_manager, ProductManager product_manager, CartManager cart_manager) {
        this.ui_manager = ui_manager;
        this.shop_manager = shop_manager;
        this.product_manager = product_manager;
        this.cart_manager = cart_manager;
    }
    public void expandCatalogue(){
        float amount;

        ui_manager.showMessage("Please enter the shop’s name: ");
        String shop_name = askForString();

        if((shop_manager.getShopByName(shop_name)!=null) && shop_name.contains(shop_manager.getShopByName(shop_name).getName())){

            ui_manager.showMessage("Please enter the product’s name: ");
            String product_name = askForString();

            if((product_manager.getProductByName(product_name)!=null) && product_name.equals(product_manager.getProductByName(product_name).getName())){
                do {
                    ui_manager.showMessage("Please enter the product’s price at this shop: ");
                    amount = askForFloat();
                    if (amount > product_manager.getProductByName(product_name).getMax_price()) {
                        ui_manager.showMessage("\nThe price must be lower or equal to it's maximum price.\n");
                    }
                } while (amount > product_manager.getProductByName(product_name).getMax_price());
                //muestra por pantalla y añade un producto junto al precio al catalogo correspondiente

                Product product = product_manager.getProductByName(product_name);
                shop_manager.getShopByName(shop_name).expandCatalogue(product,amount);
                shop_manager.updateCatalogue(shop_manager.getShopByName(shop_name));

            }else{
                System.out.println("This product doesn't exits");
            }
        }else{
            System.out.println("This shop doesn't exist");
        }
    }
    public void reduceShopCatalogue(){
        int item_to_remove;

        ui_manager.showMessage("Please enter the shop’s name: ");
        String shop_name = askForString();

        if((shop_manager.getShopByName(shop_name)!=null) && shop_name.contains(shop_manager.getShopByName(shop_name).getName())){
            if(shop_manager.getShopByName(shop_name).getCatalogue().getProducts().isEmpty()){
                System.out.println("\nThis shop's catalogue is empty.");
            }else{

                do {
                    ui_manager.showMessage("\nThis shop sells the following products:\n");
                    ui_manager.showCatalogue(shop_manager.getShopByName(shop_name).getCatalogue());

                    ui_manager.showMessage("\n\t" + (shop_manager.getShopByName(shop_name).getCatalogue().getProducts().size() + 1) + ") Back\n\nWhich one would you like to remove? ");


                    item_to_remove = askForInteger(shop_manager.getShopByName(shop_name).getCatalogue().getProducts().size() + 1);
                }while(item_to_remove<1 || item_to_remove>shop_manager.getShopByName(shop_name).getCatalogue().getProducts().size()+1);

                if(item_to_remove!=shop_manager.getShopByName(shop_name).getCatalogue().getProducts().size()+1) {

                    ui_manager.showMessage(shop_manager.getShopByName(shop_name).getCatalogue().getProducts().get(item_to_remove-1).getName()+ " by " + shop_manager.getShopByShopNumber(item_to_remove-1).getCatalogue().getProducts().get(item_to_remove-1).getBrand()+ "is no longer being sold at " + shop_name + ".\n");
                    shop_manager.getShopByName(shop_name).reduceCatalogue(item_to_remove - 1);
                    shop_manager.updateCatalogue(shop_manager.getShopByName(shop_name));
                }
            }
        }else{
            ui_manager.showMessage("\nThis shop doesn't exist.");
        }
        }
    public float askForFloat() {
        while (true) {
            try {
                float option = Float.parseFloat(scanner.nextLine());

                if (option >= 0.0) {
                    return option;
                } else {
                    ui_manager.showMessage("Please enter a positive number: ");
                }
            } catch (NumberFormatException e) {
                ui_manager.showMessage("You must introduce float\nPlease enter the product's maximum retail price: ");
            }
        }
    }
    public void searchProducts() {
        int num_products = 0;
        int found, found_shop;
        float price = 0;
        ArrayList<Product> found_products = new ArrayList<>();  //arraylist para guardar los productos encontrados

        ui_manager.searchProducts();
        String text_to_search = askForString();

        //buscar cada elemento que tenga ese string ya sea producto o tienda
        //sabiendo que cada producto puede estar en varias tiendas a precios distintos

        if (product_manager.getProductByName(text_to_search) != null ) {

            for (int i = 0; i < product_manager.getSize(); i++) {

                if (text_to_search.contains(product_manager.getJson().getProducts().get(i).getName())) {
                    found = 0;
                    num_products++;
                    found_products.add(product_manager.getJson().getProducts().get(i));

                    //lanzar el UI solo una vez al encontrar el primer producto
                    if (num_products == 1) {
                        ui_manager.showMessage("\nThe following products where found:\n\n");
                    }

                    ui_manager.showFoundProduct(product_manager.getJson().getProducts().get(i), num_products);

                    found_shop = 0;

                    for (int k = 0; k < shop_manager.getSize(); k++) {
                        for (int j = 0; j < shop_manager.getShopByShopNumber(k).getCatalogue().getProducts().size(); j++) {
                            if (shop_manager.getShopByShopNumber(k).getCatalogue().getProducts().get(j).getName().contains(text_to_search)) {
                                found_shop++;
                                if (found_shop == 1) {
                                    ui_manager.showMessage("\tSold at: \n");
                                }

                                price = shop_manager.getShopByShopNumber(k).getCatalogue().getPrices().get(j);

                                //si se encuentra el producto en una tienda se añade la tienda
                                found = 1;

                                ui_manager.showFoundShop(shop_manager.getShopByShopNumber(k).getName(), price);
                            }
                        }
                    }
                    //en el caso que no se encuentre el producto en ninguna tienda
                    if (found == 0) {
                        ui_manager.showMessage("This product is not currently being sold in any shops.\n");
                    }
                }
            }
            //si se han encontrado productos lanzamos el menu de reviews
            subMenuReviews(found_products);

        }else if(product_manager.getProductByBrand(text_to_search)!=null){

            for (int i = 0; i < product_manager.getSize(); i++) {

            if (text_to_search.contains(product_manager.getJson().getProducts().get(i).getBrand())) {
                found = 0;
                num_products++;
                Product product = product_manager.getJson().getProducts().get(i);
                found_products.add(product);

                //lanzar el UI solo una vez al encontrar el primer producto
                if (num_products == 1) {
                    ui_manager.showMessage("\nThe following products where found:\n\n");
                }

                ui_manager.showFoundProduct(product, num_products);

                found_shop = 0;

                for (int k = 0; k < shop_manager.getSize(); k++) {

                    for (int j = 0; j < shop_manager.getShopByShopNumber(k).getCatalogue().getProducts().size(); j++) {

                        if (shop_manager.getShopByShopNumber(k).getCatalogue().getProducts().get(j).getBrand().contains(text_to_search) && found_products.get(i).getName().equals(shop_manager.getShopByShopNumber(k).getCatalogue().getProducts().get(j).getName())) {

                                    found_shop++;
                                    if (found_shop == 1) {
                                        ui_manager.showMessage("\tSold at: \n");
                                    }

                                    price = shop_manager.getShopByShopNumber(k).getCatalogue().getPrices().get(j);

                                    //si se encuentra el producto en una tienda se añade la tienda
                                    found = 1;

                                    ui_manager.showFoundShop(shop_manager.getShopByShopNumber(k).getName(), price);

                        }
                    }
                }
                //en el caso que no se encuentre el producto en ninguna tienda
                if (found == 0) {
                    ui_manager.showMessage("This product is not currently being sold in any shops.\n");
                }
            }
        }
        //si se han encontrado productos lanzamos el menu de reviews
            subMenuReviews(found_products);
        }else{
            ui_manager.showMessage("(ERROR) Couldn't find any product\n");
        }
    }
    public void subMenuReviews(ArrayList<Product> found_products) {

        if (!found_products.isEmpty()) {

            ui_manager.showMessage("\n\t" + (found_products.size() + 1) + ") Back\n");
            ui_manager.reviews();

            int product_to_review = askForInteger(found_products.size() + 1);

            if (product_to_review != found_products.size() + 1) {
                Product product = found_products.get(product_to_review-1);

                ui_manager.reviewMenu();
                int review_option = askForInteger(2);

                switch (review_option) {
                    case 1:
                        if (!product.getRating().isEmpty() && product.getRating() != null) {
                            ui_manager.showReviews(product.getName(), product.getBrand(), product.getRating());
                            float average_stars = calcAverageRating(product.getRating());
                            ui_manager.showMessage("\nAverage rating: "+average_stars+"*\n");

                        } else {
                            ui_manager.showMessage("There are no reviews for this product yet.\n");
                        }
                        break;
                    case 2:
                        addReview(product);
                        break;
                }
            }
        }
    }
    public void addReview(Product product){
        int review_stars;
        String stars;
        do {
            ui_manager.showMessage("Please rate the product (1-5 stars):");
            stars = askForString();
            review_stars = stars.length();
        }while(review_stars>5 || review_stars <1);
        stars = review_stars + "* ";

        ui_manager.showMessage("Please add a comment to your review:");
        String review = askForString();

        ui_manager.showMessage("\nThank you for your review of " + product.getName() + " by " + product.getBrand() + ".\n");
        //concatenar los strings juntos y añadirlo al rating del producto
        review = stars.concat(review);

        product_manager.addRating(product,review);
    }
    public void creaTenda(){
        int year;
        String shop_name;
        float loyalty_threshold = 0;
        String sponsor = "";

        //comprobar que el nombre del producto no exista en el archivo
        do{
            ui_manager.showMessage("Please enter the shop’s name: ");
            shop_name = askForString();
            if(!shop_manager.checkShopName(shop_name)) {
                ui_manager.showMessage("This shop already exists.\n");
            }
        }while(!shop_manager.checkShopName(shop_name));

        ui_manager.showMessage("Please enter the shop’s description: ");
        String description = askForString();
        ui_manager.showMessage("Please enter the shop’s founding year: ");
        do {
            year = askForInt();
            ui_manager.showMessage("Please enter the shop’s founding year: ");
        }while(year<0 || year>2023);

        ui_manager.creaTenda();
        char model = askForCharacter("Please pick the shop’s business model: ");

        //falta mirar aqui si es de tipo B o tipo C para aplicar el patrocinio o el fidelity threshold
        if(model=='b' || model =='B'){
            ui_manager.showMessage("Please enter the shop’s loyalty threshold: ");
            loyalty_threshold = askForFloat();
        }
        if(model == 'c' || model == 'C'){
            ui_manager.showMessage("Please enter the shop’s sponsoring brand: ");
            sponsor = askForString();
        }
        ui_manager.showMessage(shop_name+" is now a part of the elCofre family\n");
        Shop shop = new Shop(shop_name,description,year,model,loyalty_threshold,sponsor);
        shop_manager.addShop(shop);
    }
    public void listShops(){
        int sub_menu_catalogue;
        int index;

        if(shop_manager.getSize()>0) {                                    //comprobar que hay al menos 1 shop

            ui_manager.listShops(shop_manager.getSize(), shop_manager.getJson().readShopsFromFile());                 //falta implementar la lista y el submenu correspondiente
            int shop_number = askForInteger(shop_manager.getSize() + 1);

            if (shop_number != shop_manager.getSize() + 1) {      //solo se muestra el catalogo si el usuario selecciona cualquier opción menos la de "back"

                if (!shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().isEmpty()) {

                    ui_manager.showShopDescription(shop_manager.getShopByShopNumber(shop_number - 1).getName(), shop_manager.getShopByShopNumber(shop_number - 1).getDescription(), shop_manager.getShopByShopNumber(shop_number - 1).getYear());
                    ui_manager.showCatalogue(shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue());   //mostrar catalogo de la tienda en cuestión
                    ui_manager.showMessage("\n" + (shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().size() + 1) + ") Back\n");

                    do {
                        ui_manager.showMessage("\nWhich one are you interested in?\n");
                        index = askForInteger(shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().size() + 1);
                    }
                    while ((index < 1) || (index > shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().size() + 1));

                    if (index!= shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().size() + 1) {
                        Product product_to_review = shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().get(index - 1);

                        do {
                            ui_manager.subMenuCatalogue();
                            sub_menu_catalogue = askForInteger(3);
                        } while (sub_menu_catalogue < 1 || sub_menu_catalogue > 3);

                        switch (sub_menu_catalogue) {
                            case 1:
                                if (!product_manager.getProductByName(product_to_review.getName()).getRating().isEmpty() && product_manager.getProductByName(product_to_review.getName()).getRating() != null) {
                                    ui_manager.showReviews(product_manager.getProductByName(product_to_review.getName()).getName(), product_manager.getProductByName(product_to_review.getName()).getBrand(), product_manager.getProductByName(product_to_review.getName()).getRating());
                                    float average_stars = calcAverageRating(product_manager.getProductByName(product_to_review.getName()).getRating());
                                    ui_manager.showMessage("\nAverage rating: "+average_stars+"*\n");
                                } else {
                                    ui_manager.showMessage("There are no reviews for this product yet.\n");
                                }
                                break;
                            case 2:
                                addReview(product_manager.getProductByName(product_to_review.getName()));
                                break;
                            case 3:
                                //añadir al carrito el producto con su precio respectivo
                                cart_manager.getCart().addProduct(shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().get(index - 1), shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getPrices().get(index - 1));
                                ui_manager.addedToCart(shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().get(index - 1).getName(), shop_manager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().get(index - 1).getBrand());
                                break;
                        }
                    }
                } else {
                        ui_manager.showMessage("There are no products in this shop's catalogue\n");
                    }
                }
            } else {
                ui_manager.showMessage("(ERROR) There are no shops yet\n");
            }
    }
    public float calcAverageRating(String rating){
        int total = 0;
        int count=0;

        if(rating.isEmpty()){return 0;}

        for (int i = 0; i < rating.length() + 1; i++) {

            if (i == rating.length() - 1 ) {
                return (float) total / count;
            }
            if(rating.charAt(i)=='*') {
                total += Character.getNumericValue(rating.charAt(i-1));
                count++;
            }

        }
        //en caso que falle algo devuelve 0
        return 0;
    }
}