package business;

public class Shop {
    private String name;
    private String description;
    private int year;
    private char model;
    private float total_revenue;
    private Catalogue catalogue;
    private float loyalty_threshold;
    private String sponsor;
    private float total_spent_by_client;
    public Shop (String name,String description,int year,char model,float loyalty_threshold,String sponsor){
        this.catalogue = new Catalogue();
        this.name=name;
        this.description=description;
        this.year= year;
        this.model=model;
        this.loyalty_threshold = loyalty_threshold;
        this.sponsor = sponsor;
        total_revenue=0;
        total_spent_by_client=0;
    }
    public void expandCatalogue(Product product,float amount){
        catalogue.getProducts().add(product);
        catalogue.getPrices().add(amount);
    }
    public void reduceCatalogue(int item_to_remove){
        catalogue.getProducts().remove(item_to_remove);                        //elimina el producto
        catalogue.getPrices().remove(item_to_remove);                          //elimina el precio asociado a ese producto
    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }
    public char getModel() {
        return model;
    }
    public float getTotalRevenue() {
        return total_revenue;
    }
    public void updateTotalRevenue(Float amount){
        total_revenue = total_revenue+amount;
    }
    public Catalogue getCatalogue(){
        return catalogue;
    }
    public void updateTotalSpentByClient(float amount){
        total_spent_by_client += amount;
    }

}
