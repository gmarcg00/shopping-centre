package business.data.model;

public class Product {
    private final String name;
    private final char category;
    private final float maxPrice;
    private final String brand;
    private String rating;

    public Product(String name, float maxPrice, String brand, char category, String review){
        this.name=name;
        this.category=category;
        this.maxPrice = maxPrice;
        this.brand=brand;
        this.rating=review;
    }

    public void updateReview(String review){
        if(rating!=null && !rating.isEmpty()) {
            rating = rating + "\n\t" +review;
        }else{
            rating="\t"+review;
        }
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getRating() {
        return rating;
    }

    public char getCategory() {
        return category;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

}
