package model;

public class MobileDetails {

    private String name;
    private String description;
    private String rating;
    private double price;
    private String pic;

    public MobileDetails( String name, String description, String rating, double price, String pic) {

        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.pic = pic;
    }
    // Getter and setter methods for the fields


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public String getPic() {
        return pic;
    }
    public void setPic(String pic) {
        this.pic = pic;
    }
}

