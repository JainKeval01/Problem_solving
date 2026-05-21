package BankMangement;

import java.util.*;

public class BankMain {
    public static void main(String[] args) {
        Bank b = new Bank();
        HashMap<String, User> user = b.getUsers();
        Scanner sc = new Scanner(System.in);
        System.out.print(" Welcome to the Rich BankMangement.Bank \n ");
        System.out.print(" Tell me are u already registered? 1 for yes and 2 for no. ");
        int registered = sc.nextInt();
        sc.nextLine();
        if (registered == 1) {
            System.out.println("Whats your name?");
            String name = sc.nextLine();
            System.out.print("Whats the password?");
            int pass = sc.nextInt();
            if (b.authentication(name, pass)) {

                System.out.print("Now u can perform below operations.");
                User us=user.get(name);
                logicCall(user,us, b, us.getName(), sc);
            }
        } else{
            System.out.print("User is not registered do want to connect with us ? \n press 1 for yes and 2 for no");
            int account = sc.nextInt();
            b.createAccount();
        }

    }

    public static void logicCall(HashMap<String,User> users,User userObj, Bank b, String name, Scanner sc) {
        FileHandling fh=new FileHandling();
        while (true) {
            System.out.println(
                    "\nEnter\n 1 for deposit \n 2 for withdraw \n 3 for view balance \n 4 to view transactions.\n 5 to Transfer amount \n 6 to check balance \n 7 exit.");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print(" Enter amount to Deposit: ");
                    int depo = sc.nextInt();
                    userObj.deposit(depo);
                    fh.updateList(users);
                    break;
                case 2:
                    System.out.print(" Enter amount to Withdraw: ");
                    int depoW = sc.nextInt();
                    try {
                        userObj.withdraw(depoW);
                    } catch (InsufficentBalance e) {
                        System.out.print(e.getMessage());
                    }
                    fh.updateList(users);

                    break;
                case 3:
                    userObj.showBalance();
                    break;
                case 4:
                    userObj.showTransactions();
                    break;
                case 5:
                    sc.nextLine();
                    System.out.print("Enter to whom u want to tranfer money?");
                    String transferName=sc.nextLine().toLowerCase();
                    System.out.print("Enter amount to transfer:");
                    int amount=sc.nextInt();
                    b.transferAmount(name,transferName,amount);
                    fh.updateList(users);
                    break;

                case 6:
                    sc.nextLine();
                    System.out.print("Enter whose account to check?");
                    String balanceCheck=sc.nextLine().toLowerCase();
                    b.showUserBalance(balanceCheck);
                    break;

                case 7:
                    System.out.print("Visit Again");
                    return;
                default:
                    System.out.print("Wrong choice");
                    break;

            }
        }
    }
}

class InsufficentBalance extends Exception {
    InsufficentBalance(String msg) {
        super(msg);
    }
}

class User {
    private String name;
    private int balance;
    private int password;

    private List<String> transaction = new ArrayList<>();

    User(String name, int balance, int password) {
        this.name = name;
        this.balance = balance;
        this.password = password;
    }


    public List<String> getTransaction() {
        return transaction;
    }

    public void setTransaction(ArrayList<String> transaction) {
        this.transaction = transaction;
    }

    String getName() {
        return name;
    }

    int getPassword() {
        return password;
    }

    int getBalance() {
        return balance;
    }

    void setBalance(int bal) {
        balance = bal;
    }

    void deposit(int amount) {
        if (amount <= 0) {
            System.out.println("Invalid amount");
        } else {
            balance += amount;
            System.out.println(amount + " amount Deposited");
            transaction.add(amount + " deposited ");
        }
    }

    void withdraw(int amount) throws InsufficentBalance {
        if (amount < 0) {
            System.out.println("Invalid amount");
            return;
        }
        if (amount > balance) {
            throw new InsufficentBalance("Insufficient balance");
        } else {

            balance -= amount;
            System.out.println(amount + " amount withdrawn");
            transaction.add(amount + " Withdrawn");
        }
    }

    void showBalance() {
        System.out.println("Current Balance: " + balance);
    }

    void showTransactions() {
        for (String t : transaction) {
            System.out.println("=>" + t);
        }
    }
}

class Bank {
    private HashMap<String, User> users = new HashMap<>();
    Scanner sc = new Scanner(System.in);
    FileHandling fileHandling=new FileHandling();
    Bank() {
        users=fileHandling.readFile();
    }

    void createAccount() {
        System.out.print(" Tell me your name to create account: ");
        String name = sc.nextLine().toLowerCase();
        if(!users.containsKey(name)) {
            System.out.print(" What will be password? ");
            int password = sc.nextInt();
            System.out.print(" Now tell me How much money you want to keep? ");
            int balance = sc.nextInt();
            users.put(name, new User(name, balance, password));
            System.out.println("Your acconut has been created.");
            fileHandling.newAccount(name,balance,password);
        }else{
            System.out.print("BankMangement.User already exists");
        }


    }

    HashMap<String, User> getUsers() {
        return users;
    }

    boolean authentication(String name, int password) {
        if (users.containsKey(name)) {
            User u = users.get(name);
            while (password != u.getPassword()) {
                System.out.println("Invalid password try again");
                System.out.print("=>");
                password = sc.nextInt();
            }
            return true;
        } else{
            System.out.print("No such user in our registry");
        }
            return false;
    }

    void transferAmount(String ownerName, String name, int amount) {

        if (!users.containsKey(name)) {
            System.out.println("No such user");
            return;
        }
        if (ownerName.equals(name)) {
            System.out.println("You cannot transfer money to yourself");
            return;
        }
        if (amount <= 0) {
            System.out.println("Invalid amount");
            return;
        }
        User sender = users.get(ownerName);
        User receiver = users.get(name);

        if (amount > sender.getBalance()) {
            System.out.println("Insufficient balance");
            return;
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        sender.getTransaction().add(amount + " transferred to " + name);
        receiver.getTransaction().add(amount + " received from " + ownerName);

        System.out.println(amount + " transferred successfully to " + name);
    }
    void showUserBalance(String name) {
        if(!users.containsKey(name)) {
            System.out.print("BankMangement.User not found");
            return;
        }
        User u=users.get(name);
        System.out.print("Balance of "+name+"is: "+u.getBalance());
    }
}

