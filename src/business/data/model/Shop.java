package business.data.model;

public class Shop {
    private String name;
    private String description;
    private int year;
    private char model;
    private float totalRevenue;
    private Catalogue catalogue;
    private float loyaltyThreshold;
    private String sponsor;
    private float totalSpentByClient;

    public Shop (String name, String description, int year, char model, float loyaltyThreshold, String sponsor){
        this.catalogue = new Catalogue();
        this.name=name;
        this.description=description;
        this.year= year;
        this.model=model;
        this.loyaltyThreshold = loyaltyThreshold;
        this.sponsor = sponsor;
        totalRevenue =0;
        totalSpentByClient =0;
    }

    public void expandCatalogue(Product product, float amount){
        catalogue.getProducts().add(product);
        catalogue.getPrices().add(amount);
    }

    public void reduceCatalogue(int itemToRemove){
        catalogue.getProducts().remove(itemToRemove);                        //elimina el producto
        catalogue.getPrices().remove(itemToRemove);                          //elimina el precio asociado a ese producto
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
        return totalRevenue;
    }

    public void updateTotalRevenue(Float amount){
        totalRevenue = totalRevenue +amount;
    }

    public Catalogue getCatalogue(){
        return catalogue;
    }

    public void updateTotalSpentByClient(float amount){
        totalSpentByClient += amount;
    }

    public float getLoyaltyThreshold() {return loyaltyThreshold;}

    public String getSponsor() {return sponsor;}

    public float getTotalSpentByClient() {return totalSpentByClient;}

}
