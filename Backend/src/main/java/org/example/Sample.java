package org.example;

public class Sample {
    private int id;
    private String name;
    private String material;
    private double volumeLiters;
    private double diameterCm;
    private boolean inductionCompatible;
    private double price;
    private String description;

    public Sample() {
    }

    public Sample(
            int id,
            String name,
            String material,
            double volumeLiters,
            double diameterCm,
            boolean inductionCompatible,
            double price,
            String description
    ) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.volumeLiters = volumeLiters;
        this.diameterCm = diameterCm;
        this.inductionCompatible = inductionCompatible;
        this.price = price;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getVolumeLiters() {
        return volumeLiters;
    }

    public void setVolumeLiters(double volumeLiters) {
        this.volumeLiters = volumeLiters;
    }

    public double getDiameterCm() {
        return diameterCm;
    }

    public void setDiameterCm(double diameterCm) {
        this.diameterCm = diameterCm;
    }

    public boolean isInductionCompatible() {
        return inductionCompatible;
    }

    public void setInductionCompatible(boolean inductionCompatible) {
        this.inductionCompatible = inductionCompatible;
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
