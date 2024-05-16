package business;

public class Product {
    private final String name;
    private final char category;
    private final float max_price;
    private String rating;
    private final String brand;

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

    public float getMax_price() {
        return max_price;
    }
    public Product(String name,float max_price,String brand,char category,String review){
        this.name=name;
        this.category=category;
        this.max_price=max_price;
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
}
