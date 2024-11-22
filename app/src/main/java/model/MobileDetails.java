package model;

import java.io.Serializable;

public class MobileDetails implements Serializable{

    // Fields to store mobile details
    private String name;        // Name of the mobile
    private String description; // Description of the mobile
    private String rating;      // Rating of the mobile
    private double price;       // Price of the mobile
    private String pic;         // Picture URL of the mobile

    // Constructor to initialize MobileDetails object
    public MobileDetails(String name, String description, String rating, double price, String pic) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.price = price;
        this.pic = pic;
    }

    // Getter method for name
    public String getName() {
        return name;
    }

    // Setter method for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter method for description
    public String getDescription() {
        return description;
    }

    // Setter method for description
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter method for rating
    public String getRating() {
        return rating;
    }

    // Setter method for rating
    public void setRating(String rating) {
        this.rating = rating;
    }

    // Getter method for price
    public double getPrice() {
        return price;
    }

    // Setter method for price
    public void setPrice(double price) {
        this.price = price;
    }

    // Getter method for picture URL
    public String getPic() {
        return pic;
    }

    // Setter method for picture URL
    public void setPic(String pic) {
        this.pic = pic;
    }
}
