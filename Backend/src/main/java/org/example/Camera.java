package org.example;

public class Camera {
    private int id; // Додаємо ID
    private String brand;
    private String model;
    private int megapixels; // Змінюємо назву для зручності фронтенду
    private double price;

    public Camera(int id, String model, String brand, int megapixels, double price) {
        this.id = id;
        this.model = model;
        this.brand = brand;
        this.megapixels = megapixels;
        this.price = price;
    }

    // Геттери обов'язкові для Gson!
    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getMegapixels() { return megapixels; }
    public double getPrice() { return price; }
}