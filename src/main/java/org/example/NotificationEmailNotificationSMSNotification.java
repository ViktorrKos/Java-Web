// Notification.java
public class Notification {
    public void send() {
        System.out.println("Надсилання загального сповіщення...");
    }
}

// EmailNotification.java
public class EmailNotification extends Notification {
    @Override
    public void send() {
        System.out.println("Надсилання Email сповіщення...");
    }
}

// SMSNotification.java
public class SMSNotification extends Notification {
    @Override
    public void send() {
        System.out.println("Надсилання SMS сповіщення...");
    }
}