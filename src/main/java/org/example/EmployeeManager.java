// Employee.java
public class Employee {
    protected String name;
    protected String position;

    public Employee(String name, String position) {
        this.name = name;
        this.position = position;
    }

    public void displayInfo() {
        System.out.println("Співробітник: " + name + ", Посада: " + position);
    }
}

// Manager.java
public class Manager extends Employee {
    private int teamSize;

    public Manager(String name, String position, int teamSize) {
        super(name, position);
        this.teamSize = teamSize;
    }

    @Override
    public void displayInfo() {
        System.out.println("Менеджер: " + name + ", Посада: " + position + ", Розмір команди: " + teamSize + " осіб");
    }
}