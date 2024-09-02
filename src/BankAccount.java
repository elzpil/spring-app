public class BankAccount {
    private String id;
    private double balance;

    public BankAccount(String id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be more than zero");
            return;
        }
        balance += amount;
        System.out.println("Deposit complete: " + amount);
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds");
            return;
        }
        balance -= amount;
        System.out.println("Withdrawal complete: " + amount);
    }

    public void transfer(double amount, BankAccount targetAccount) {
        if (amount <= 0) {
            System.out.println("Amount must be more than zero");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds");
            return;
        }
        this.withdraw(amount);
        targetAccount.deposit(amount);
        System.out.println("Transfer completed: " + amount);
    }
}
