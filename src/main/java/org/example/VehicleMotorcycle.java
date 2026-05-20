// Vehicle.java
public class Vehicle {
    protected String make;
    protected String model;

    public Vehicle(String make, String model) {
        this.make = make;
        this.model = model;
    }

    public void displayInfo() {
        System.out.println("Транспортний засіб: " + make + " " + model);
    }
}

// Motorcycle.java
public class Motorcycle extends Vehicle {
    private int engineCapacity;

    public Motorcycle(String make, String model, int engineCapacity) {
        super(make, model);
        this.engineCapacity = engineCapacity;
    }

    @Override
    public void displayInfo() {
        System.out.println("Мотоцикл: " + make + " " + model + ", Об'єм двигуна: " + engineCapacity + " куб.см");
    }
}