// Appliance.java
public abstract class Appliance {
    public abstract void turnOn();
}

// WashingMachine.java
public class WashingMachine extends Appliance {
    @Override
    public void turnOn() {
        System.out.println("Пральна машина увімкнена. Починається прання.");
    }
}

// Microwave.java
public class Microwave extends Appliance {
    @Override
    public void turnOn() {
        System.out.println("Мікрохвильовка увімкнена. Починається розігрів.");
    }
}