package org.example;

public class Sample {
    private int id;
    private String brand;
    private String model;
    private int megapixels;
    private double price;
    private String description;

    public Sample() {
    }

    public Sample(int id, String brand, String model, int megapixels, double price, String description) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.megapixels = megapixels;
        this.price = price;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMegapixels() {
        return megapixels;
    }

    public void setMegapixels(int megapixels) {
        this.megapixels = megapixels;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
