public class Account {
    private double balance;
    private String accountNumber;

    public Account(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance >= 0 ? initialBalance : 0;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Поповнено на: " + amount + ". Новий баланс: " + balance);
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Знято: " + amount + ". Залишок: " + balance);
        } else {
            System.out.println("Недостатньо коштів або некоректна сума.");
        }
    }

    public double getBalance() {
        return balance;
    }
}