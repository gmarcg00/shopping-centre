package presentation;

import business.data.model.Product;
import business.data.model.Shop;
import business.service.impl.CartManagerImpl;
import business.service.impl.ProductManagerImpl;
import business.service.impl.ShopManagerImpl;
import business.service.ProductManager;
import business.service.ShopManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private final UIManager uiManager;
    private final CartManagerImpl cartManager;

    private ShopManager shopManager;
    private ProductManager productManager;

    Scanner scanner = new Scanner(System.in);


    public Controller(UIManager uiManager, CartManagerImpl cartManager) {
        this.uiManager = uiManager;
        this.cartManager = cartManager;
    }

    public void run() throws IOException {
        int initialMenuOption = 0;

        uiManager.startMenu();         //ejecutar menu bienvenida
        uiManager.startMenuApi();

        do{
            uiManager.apiOrJson();
            initialMenuOption = askForInteger(2);
            switch (initialMenuOption){
                case 1:
                    choosePersistence(1);
                    break;
                case 2:
                    choosePersistence(2);
                    break;
                default:
                    break;
            }
        }while(initialMenuOption!=1 && initialMenuOption!=2);

        mainMenu();



        //if(!product_manager.checkApi()){
        //    ui_manager.startMenuApiError();
//
        //    if(!product_manager.checkProductFile()){
        //        ui_manager.startMenuError();
//
        //    }else{ui_manager.startMenuJson();}
//
        //}

        if(productManager.checkStatus()){
          uiManager.showMessage("Starting program...\n");

        }
    }

    private void choosePersistence(int option){
        try {
            this.productManager = new ProductManagerImpl(option);
            this.shopManager = new ShopManagerImpl(option);
        }catch (IOException e){
            uiManager.showMessage("Error loading the file\n");
        }
    }

    private void mainMenu() {
        int option;                     //opción del menu principal
        int sub_menu_option;
        do {
            uiManager.startMenuAccess();               //ejecutar menu de bienvenida
            option = askForInteger(6);
            switch (option) {                         //diferentes opciones del menu principal
                case 1:
                    do {
                        uiManager.subMenuProducts();
                        sub_menu_option = askForInteger(3);//ejecutar el submenu de Products y asociar un valor
                        switch (sub_menu_option) {                //en función de sub_menu_option se ejecuta un apartado u otro
                            case 1:
                                //crear producto nuevo
                                newProduct();
                                break;
                            case 2:
                                //borrar producto existente si hay productos
                                if(!productManager.getProducts().isEmpty()) {
                                    deleteProduct();                //falta implementar y falta parametro
                                }else{
                                    uiManager.showMessage("(ERROR) There are no products to delete\n\n");
                                }
                                break;
                        }
                    }while(sub_menu_option!=3);
                    break;
                case 2:
                    do{
                        uiManager.subMenuShops();
                        sub_menu_option=askForInteger(4);
                        switch (sub_menu_option) {
                            case 1:
                                creaTenda();                         //escribe una tienda en la persistencia
                                break;
                            case 2:
                                if(shopManager.getSize()>0) {
                                    expandCatalogue();
                                }else{
                                    uiManager.showMessage("(ERROR) There are no shops yet\n");
                                }
                                break;
                            case 3:
                                if(shopManager.getSize()>0) {
                                    reduceShopCatalogue();
                                }else{
                                    uiManager.showMessage("(ERROR) There are no shops yet\n");
                                }
                                break;
                        }
                    }while(sub_menu_option!=4);
                    break;
                case 3:
                    if(productManager.getSize()>0) {
                        searchProducts();
                    }else{
                        uiManager.showMessage("(ERROR) The products file is empty\n");}
                    break;
                case 4:
                    listShops();
                    break;
                case 5:
                    yourCart();
                    break;
                case 6:
                    uiManager.exit();
                    break;
            }
        } while (option != 6);
    }


    public void yourCart() {
        int sub_menu_option;
        String check_out;
        if(!cartManager.getCart().getProducts().isEmpty()){

        uiManager.showCart(cartManager.getCart().getProducts(), cartManager.getCart().getPrices());
        uiManager.showTotal(cartManager.calcPrice(cartManager.getCart())); //por confirmar(mostrar total carrito)

            do{

                uiManager.showCartSubMenu();
                sub_menu_option=askForInteger(3);

                switch (sub_menu_option){
                    case 1:
                        uiManager.checkOut();
                        check_out = askForString();
                            if(check_out.equalsIgnoreCase("Yes")){
                                buy(cartManager.getCart().getPrices(), cartManager.getCart().getProducts());
                                cartManager.emptyCart();
                                uiManager.clearCart();
                            }
                        break;
                    case 2:
                        uiManager.checkClearCart();
                        check_out = askForString();
                            if(check_out.equalsIgnoreCase("Yes")) {
                                cartManager.emptyCart();
                                uiManager.clearCart();
                            }
                        break;
                }
            }while(sub_menu_option!=3 && !cartManager.getCart().getProducts().isEmpty());
        }else{
            uiManager.showMessage("Your cart is empty\n");}
    }

    public void buy(List<Float> prices, List<Product> products) {

        Product product;
        float price;
        Shop shop;

        ArrayList<Shop> updated_shops = new ArrayList<>();
        ArrayList<Float> amount_updated = new ArrayList<>();

        for (int i = 0; i < products.size(); i++) {

            product = products.get(i);
            price = prices.get(i);
            shop = shopManager.getShopByProduct(price);

            if (shop != null ) {

                //falta una condición para

                float average_rating = calcAverageRating(product.getRating());

                //update amount spent by a client in this shop (before IVA for total shop revenue)
                shop.updateTotalSpentByClient(price);

                //iva substraction for actual maximum amount earned by the shop
                price = checkIva(price,product.getCategory(),average_rating);
                shop.updateTotalRevenue(price);

                shopManager.getJson().updateShopList(shop);
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
            uiManager.showShopRevenue(updated_shops.get(i), amount_updated.get(i));
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
            uiManager.showMessage("You must introduce a valid option");
        }
        //convertir a mayuscula la primera letra
        String aux = string.substring(0,1).toUpperCase() + string.substring(1);
        return aux;

    }

    public void newProduct(){
        String product_name;

        //comprobar que el nombre del producto no exista en el archivo
        do{
            uiManager.showMessage("Please enter the product’s name: ");
            product_name = askForString();
            if(!productManager.checkProductName(product_name)) {
                uiManager.showMessage("This product already exists.\n");
            }
        }while(!productManager.checkProductName(product_name));

        uiManager.showMessage("Please enter the product’s brand: ");
        String brand_name = askForString();
        uiManager.showMessage("Please enter the product’s maximum retail price: ");
        float amount = askForFloat();
        uiManager.showMessage("The system supports the following product categories: \n\n");
        uiManager.createProduct();
        char category=askForCharacter("Please pick the product’s category: ");
        Product product = new Product(product_name,amount,brand_name,category,"");
        productManager.addProduct(product);
        uiManager.showMessage("The product \""+product_name+"\" by \""+brand_name+"\" was added to the system. \n\n");
    }

    public void deleteProduct(){
        int option;

        do {

            uiManager.listProducts(productManager.getJson().getProducts());         //printear todos los productos del json falta implementar listProducts()
            option = askForInteger(productManager.getSize() + 1);

        }while( option<1 || option > productManager.getSize()+1);

        if(option< productManager.getSize()+1) {

            uiManager.showMessage("Are you sure you want to remove " + productManager.getProductByIndex(option - 1).getName() + " by " + productManager.getProductByIndex(option - 1).getBrand() + "? ");

            if (askForString().equalsIgnoreCase("Yes")){
                for(int i = 0; i< shopManager.getSize(); i++){
                    for(int j = 0; j< shopManager.getShopByShopNumber(i).getCatalogue().getProducts().size(); j++){
                        if(shopManager.getShopByShopNumber(i).getCatalogue().getProducts().get(j).getName().equals(productManager.getProductByIndex(option - 1).getName())){
                            Shop shop = shopManager.getShopByProduct(shopManager.getShopByShopNumber(i).getCatalogue().getPrices().get(j));
                            shop.reduceCatalogue(j);
                            shopManager.updateCatalogue(shop);
                        }
                    }
                }
                uiManager.deletedProduct(productManager.getProductByIndex(option - 1).getName(), productManager.getProductByIndex(option - 1).getBrand());
                productManager.deleteProduct(productManager.getProductByIndex(option - 1).getName());
            }
        }
    }
    public char askForCharacter(String string){
            char option;
            do {
                uiManager.showMessage(string);
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
                uiManager.showMessage("\tPlease enter a number from 1 to "+parameter+"\n");    //numero no valido
            }
        } catch (NumberFormatException e) {
            uiManager.showMessage("\n\tYou must introduce a number\n");                //no ha introducido un numero
            uiManager.showMessage("\tPlease enter a number from 1 to " + parameter + ": \n");
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
                uiManager.showMessage("Please enter a number bigger than 0 and lower than 2025: ");    //numero no valido
            }
        } catch (NumberFormatException e) {
            uiManager.showMessage("\tYou must introduce a number\n");                //no ha introducido un numero
            uiManager.showMessage("\tPlease enter a positive number\n\n");
        }
        return option;
    }

    public void expandCatalogue(){
        float amount;

        uiManager.showMessage("Please enter the shop’s name: ");
        String shop_name = askForString();

        if((shopManager.getShopByName(shop_name)!=null) && shop_name.contains(shopManager.getShopByName(shop_name).getName())){

            uiManager.showMessage("Please enter the product’s name: ");
            String product_name = askForString();

            if((productManager.getProductByName(product_name)!=null) && product_name.equals(productManager.getProductByName(product_name).getName())){
                do {
                    uiManager.showMessage("Please enter the product’s price at this shop: ");
                    amount = askForFloat();
                    if (amount > productManager.getProductByName(product_name).getMaxPrice()) {
                        uiManager.showMessage("\nThe price must be lower or equal to it's maximum price.\n");
                    }
                } while (amount > productManager.getProductByName(product_name).getMaxPrice());
                //muestra por pantalla y añade un producto junto al precio al catalogo correspondiente

                Product product = productManager.getProductByName(product_name);
                shopManager.getShopByName(shop_name).expandCatalogue(product,amount);
                shopManager.updateCatalogue(shopManager.getShopByName(shop_name));

            }else{
                System.out.println("This product doesn't exits");
            }
        }else{
            System.out.println("This shop doesn't exist");
        }
    }
    public void reduceShopCatalogue(){
        int item_to_remove;

        uiManager.showMessage("Please enter the shop’s name: ");
        String shop_name = askForString();

        if((shopManager.getShopByName(shop_name)!=null) && shop_name.contains(shopManager.getShopByName(shop_name).getName())){
            if(shopManager.getShopByName(shop_name).getCatalogue().getProducts().isEmpty()){
                System.out.println("\nThis shop's catalogue is empty.");
            }else{

                do {
                    uiManager.showMessage("\nThis shop sells the following products:\n");
                    uiManager.showCatalogue(shopManager.getShopByName(shop_name).getCatalogue());

                    uiManager.showMessage("\n\t" + (shopManager.getShopByName(shop_name).getCatalogue().getProducts().size() + 1) + ") Back\n\nWhich one would you like to remove? ");


                    item_to_remove = askForInteger(shopManager.getShopByName(shop_name).getCatalogue().getProducts().size() + 1);
                }while(item_to_remove<1 || item_to_remove> shopManager.getShopByName(shop_name).getCatalogue().getProducts().size()+1);

                if(item_to_remove!= shopManager.getShopByName(shop_name).getCatalogue().getProducts().size()+1) {

                    uiManager.showMessage(shopManager.getShopByName(shop_name).getCatalogue().getProducts().get(item_to_remove-1).getName()+ " by " + shopManager.getShopByShopNumber(item_to_remove-1).getCatalogue().getProducts().get(item_to_remove-1).getBrand()+ "is no longer being sold at " + shop_name + ".\n");
                    shopManager.getShopByName(shop_name).reduceCatalogue(item_to_remove - 1);
                    shopManager.updateCatalogue(shopManager.getShopByName(shop_name));
                }
            }
        }else{
            uiManager.showMessage("\nThis shop doesn't exist.");
        }
        }
    public float askForFloat() {
        while (true) {
            try {
                float option = Float.parseFloat(scanner.nextLine());

                if (option >= 0.0) {
                    return option;
                } else {
                    uiManager.showMessage("Please enter a positive number: ");
                }
            } catch (NumberFormatException e) {
                uiManager.showMessage("You must introduce float\nPlease enter the product's maximum retail price: ");
            }
        }
    }
    public void searchProducts() {
        int num_products = 0;
        int found, found_shop;
        float price = 0;
        ArrayList<Product> found_products = new ArrayList<>();  //arraylist para guardar los productos encontrados

        uiManager.searchProducts();
        String text_to_search = askForString();

        //buscar cada elemento que tenga ese string ya sea producto o tienda
        //sabiendo que cada producto puede estar en varias tiendas a precios distintos

        if (productManager.getProductByName(text_to_search) != null ) {

            for (int i = 0; i < productManager.getSize(); i++) {

                if (text_to_search.contains(productManager.getJson().getProducts().get(i).getName())) {
                    found = 0;
                    num_products++;
                    found_products.add(productManager.getJson().getProducts().get(i));

                    //lanzar el UI solo una vez al encontrar el primer producto
                    if (num_products == 1) {
                        uiManager.showMessage("\nThe following products where found:\n\n");
                    }

                    uiManager.showFoundProduct(productManager.getJson().getProducts().get(i), num_products);

                    found_shop = 0;

                    for (int k = 0; k < shopManager.getSize(); k++) {
                        for (int j = 0; j < shopManager.getShopByShopNumber(k).getCatalogue().getProducts().size(); j++) {
                            if (shopManager.getShopByShopNumber(k).getCatalogue().getProducts().get(j).getName().contains(text_to_search)) {
                                found_shop++;
                                if (found_shop == 1) {
                                    uiManager.showMessage("\tSold at: \n");
                                }

                                price = shopManager.getShopByShopNumber(k).getCatalogue().getPrices().get(j);

                                //si se encuentra el producto en una tienda se añade la tienda
                                found = 1;

                                uiManager.showFoundShop(shopManager.getShopByShopNumber(k).getName(), price);
                            }
                        }
                    }
                    //en el caso que no se encuentre el producto en ninguna tienda
                    if (found == 0) {
                        uiManager.showMessage("This product is not currently being sold in any shops.\n");
                    }
                }
            }
            //si se han encontrado productos lanzamos el menu de reviews
            subMenuReviews(found_products);

        }else if(productManager.getProductByBrand(text_to_search)!=null){

            for (int i = 0; i < productManager.getSize(); i++) {

            if (text_to_search.contains(productManager.getJson().getProducts().get(i).getBrand())) {
                found = 0;
                num_products++;
                Product product = productManager.getJson().getProducts().get(i);
                found_products.add(product);

                //lanzar el UI solo una vez al encontrar el primer producto
                if (num_products == 1) {
                    uiManager.showMessage("\nThe following products where found:\n\n");
                }

                uiManager.showFoundProduct(product, num_products);

                found_shop = 0;

                for (int k = 0; k < shopManager.getSize(); k++) {

                    for (int j = 0; j < shopManager.getShopByShopNumber(k).getCatalogue().getProducts().size(); j++) {

                        if (shopManager.getShopByShopNumber(k).getCatalogue().getProducts().get(j).getBrand().contains(text_to_search) && found_products.get(i).getName().equals(shopManager.getShopByShopNumber(k).getCatalogue().getProducts().get(j).getName())) {

                                    found_shop++;
                                    if (found_shop == 1) {
                                        uiManager.showMessage("\tSold at: \n");
                                    }

                                    price = shopManager.getShopByShopNumber(k).getCatalogue().getPrices().get(j);

                                    //si se encuentra el producto en una tienda se añade la tienda
                                    found = 1;

                                    uiManager.showFoundShop(shopManager.getShopByShopNumber(k).getName(), price);

                        }
                    }
                }
                //en el caso que no se encuentre el producto en ninguna tienda
                if (found == 0) {
                    uiManager.showMessage("This product is not currently being sold in any shops.\n");
                }
            }
        }
        //si se han encontrado productos lanzamos el menu de reviews
            subMenuReviews(found_products);
        }else{
            uiManager.showMessage("(ERROR) Couldn't find any product\n");
        }
    }
    public void subMenuReviews(ArrayList<Product> found_products) {

        if (!found_products.isEmpty()) {

            uiManager.showMessage("\n\t" + (found_products.size() + 1) + ") Back\n");
            uiManager.reviews();

            int product_to_review = askForInteger(found_products.size() + 1);

            if (product_to_review != found_products.size() + 1) {
                Product product = found_products.get(product_to_review-1);

                uiManager.reviewMenu();
                int review_option = askForInteger(2);

                switch (review_option) {
                    case 1:
                        if (!product.getRating().isEmpty() && product.getRating() != null) {
                            uiManager.showReviews(product.getName(), product.getBrand(), product.getRating());
                            float average_stars = calcAverageRating(product.getRating());
                            uiManager.showMessage("\nAverage rating: "+average_stars+"*\n");

                        } else {
                            uiManager.showMessage("There are no reviews for this product yet.\n");
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
            uiManager.showMessage("Please rate the product (1-5 stars):");
            stars = askForString();
            review_stars = stars.length();
        }while(review_stars>5 || review_stars <1);
        stars = review_stars + "* ";

        uiManager.showMessage("Please add a comment to your review:");
        String review = askForString();

        uiManager.showMessage("\nThank you for your review of " + product.getName() + " by " + product.getBrand() + ".\n");
        //concatenar los strings juntos y añadirlo al rating del producto
        review = stars.concat(review);

        productManager.addRating(product,review);
    }
    public void creaTenda(){
        int year;
        String shop_name;
        float loyalty_threshold = 0;
        String sponsor = "";

        //comprobar que el nombre del producto no exista en el archivo
        do{
            uiManager.showMessage("Please enter the shop’s name: ");
            shop_name = askForString();
            if(!shopManager.checkShopName(shop_name)) {
                uiManager.showMessage("This shop already exists.\n");
            }
        }while(!shopManager.checkShopName(shop_name));

        uiManager.showMessage("Please enter the shop’s description: ");
        String description = askForString();
        uiManager.showMessage("Please enter the shop’s founding year: ");
        do {
            year = askForInt();
            uiManager.showMessage("Please enter the shop’s founding year: ");
        }while(year<0 || year>2023);

        uiManager.creaTenda();
        char model = askForCharacter("Please pick the shop’s business model: ");

        //falta mirar aqui si es de tipo B o tipo C para aplicar el patrocinio o el fidelity threshold
        if(model=='b' || model =='B'){
            uiManager.showMessage("Please enter the shop’s loyalty threshold: ");
            loyalty_threshold = askForFloat();
        }
        if(model == 'c' || model == 'C'){
            uiManager.showMessage("Please enter the shop’s sponsoring brand: ");
            sponsor = askForString();
        }
        uiManager.showMessage(shop_name+" is now a part of the elCofre family\n");
        Shop shop = new Shop(shop_name,description,year,model,loyalty_threshold,sponsor);
        shopManager.addShop(shop);
    }
    public void listShops(){
        int sub_menu_catalogue;
        int index;

        if(shopManager.getSize()>0) {                                    //comprobar que hay al menos 1 shop

            uiManager.listShops(shopManager.getSize(), shopManager.getJson().readShopsFromFile());                 //falta implementar la lista y el submenu correspondiente
            int shop_number = askForInteger(shopManager.getSize() + 1);

            if (shop_number != shopManager.getSize() + 1) {      //solo se muestra el catalogo si el usuario selecciona cualquier opción menos la de "back"

                if (!shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().isEmpty()) {

                    uiManager.showShopDescription(shopManager.getShopByShopNumber(shop_number - 1).getName(), shopManager.getShopByShopNumber(shop_number - 1).getDescription(), shopManager.getShopByShopNumber(shop_number - 1).getYear());
                    uiManager.showCatalogue(shopManager.getShopByShopNumber(shop_number - 1).getCatalogue());   //mostrar catalogo de la tienda en cuestión
                    uiManager.showMessage("\n" + (shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().size() + 1) + ") Back\n");

                    do {
                        uiManager.showMessage("\nWhich one are you interested in?\n");
                        index = askForInteger(shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().size() + 1);
                    }
                    while ((index < 1) || (index > shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().size() + 1));

                    if (index!= shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().size() + 1) {
                        Product product_to_review = shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().get(index - 1);

                        do {
                            uiManager.subMenuCatalogue();
                            sub_menu_catalogue = askForInteger(3);
                        } while (sub_menu_catalogue < 1 || sub_menu_catalogue > 3);

                        switch (sub_menu_catalogue) {
                            case 1:
                                if (!productManager.getProductByName(product_to_review.getName()).getRating().isEmpty() && productManager.getProductByName(product_to_review.getName()).getRating() != null) {
                                    uiManager.showReviews(productManager.getProductByName(product_to_review.getName()).getName(), productManager.getProductByName(product_to_review.getName()).getBrand(), productManager.getProductByName(product_to_review.getName()).getRating());
                                    float average_stars = calcAverageRating(productManager.getProductByName(product_to_review.getName()).getRating());
                                    uiManager.showMessage("\nAverage rating: "+average_stars+"*\n");
                                } else {
                                    uiManager.showMessage("There are no reviews for this product yet.\n");
                                }
                                break;
                            case 2:
                                addReview(productManager.getProductByName(product_to_review.getName()));
                                break;
                            case 3:
                                //añadir al carrito el producto con su precio respectivo
                                cartManager.getCart().addProduct(shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().get(index - 1), shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getPrices().get(index - 1));
                                uiManager.addedToCart(shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().get(index - 1).getName(), shopManager.getShopByShopNumber(shop_number - 1).getCatalogue().getProducts().get(index - 1).getBrand());
                                break;
                        }
                    }
                } else {
                        uiManager.showMessage("There are no products in this shop's catalogue\n");
                    }
                }
            } else {
                uiManager.showMessage("(ERROR) There are no shops yet\n");
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