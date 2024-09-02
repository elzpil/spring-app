import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<BankAccount> accounts = new ArrayList<>();
        File file = new File("accounts.csv");

        // Load existing accounts from file
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0];
                double balance = Double.parseDouble(data[1]);
                accounts.add(new BankAccount(id, balance));
            }
            reader.close();
        }

        while (true) {
            System.out.println("\n--- Bank Account Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Check Balance");
            System.out.println("6. Save and Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter unique ID for the account: ");
                    String id = scanner.nextLine();
                    if (findAccountById(id, accounts) != null) {
                        System.out.println("Account with this ID already exists.");
                        break;
                    }
                    System.out.print("Enter initial balance: ");
                    double initialBalance = scanner.nextDouble();
                    accounts.add(new BankAccount(id, initialBalance));
                    System.out.println("Account created with ID: " + id + " and balance: " + initialBalance);
                    break;

                case 2:
                    System.out.print("Enter account ID to deposit to: ");
                    id = scanner.nextLine();
                    BankAccount depositAccount = findAccountById(id, accounts);
                    if (depositAccount != null) {
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = scanner.nextDouble();
                        depositAccount.deposit(depositAmount);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter account ID to withdraw from: ");
                    id = scanner.nextLine();
                    BankAccount withdrawAccount = findAccountById(id, accounts);
                    if (withdrawAccount != null) {
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = scanner.nextDouble();
                        withdrawAccount.withdraw(withdrawAmount);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case 4:
                    System.out.print("Enter account ID to transfer from: ");
                    String fromId = scanner.nextLine();
                    BankAccount fromAccount = findAccountById(fromId, accounts);
                    if (fromAccount == null) {
                        System.out.println("Account not found.");
                        break;
                    }
                    System.out.print("Enter target account ID to transfer to: ");
                    String toId = scanner.nextLine();
                    BankAccount toAccount = findAccountById(toId, accounts);
                    if (toAccount == null) {
                        System.out.println("Target account not found.");
                        break;
                    }
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    fromAccount.transfer(transferAmount, toAccount);
                    break;

                case 5:
                    for (BankAccount account : accounts) {
                        System.out.println("Account ID: " + account.getId() + " | Balance: " + account.getBalance());
                    }
                    break;

                case 6:
                    saveAccountsToFile(accounts);
                    System.out.println("Data saved. Exiting the program.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }

    private static BankAccount findAccountById(String id, ArrayList<BankAccount> accounts) {
        for (BankAccount account : accounts) {
            if (account.getId().equals(id)) {
                return account;
            }
        }
        return null;
    }

    private static void saveAccountsToFile(ArrayList<BankAccount> accounts) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("accounts.csv"));
        for (BankAccount account : accounts) {
            writer.write(account.getId() + "," + account.getBalance());
            writer.newLine();
        }
        writer.close();
    }
}
