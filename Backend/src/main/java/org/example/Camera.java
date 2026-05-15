package org.example;

public class Camera {
    private final int id;
    private final String name;
    private final String imageUrl;
    private final String description;
    private final String brand;
    private final String model;
    private final int megapixels;
    private final String sensorType;
    private final boolean video4k;
    private final double price;

    public Camera(
            int id,
            String name,
            String imageUrl,
            String description,
            String brand,
            String model,
            int megapixels,
            String sensorType,
            boolean video4k,
            double price
    ) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.brand = brand;
        this.model = model;
        this.megapixels = megapixels;
        this.sensorType = sensorType;
        this.video4k = video4k;
        this.price = price;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public String getImageUrl() { return imageUrl; }

    public String getDescription() { return description; }

    public String getBrand() { return brand; }

    public String getModel() { return model; }

    public int getMegapixels() { return megapixels; }

    public String getSensorType() { return sensorType; }

    public boolean isVideo4k() { return video4k; }

    public double getPrice() { return price; }
}
