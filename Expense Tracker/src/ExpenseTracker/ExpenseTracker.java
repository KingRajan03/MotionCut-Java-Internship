package ExpenseTracker;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ExpenseTracker {
    private Map<String, User> users;
    private User currentUser;

    public ExpenseTracker() {
        users = new HashMap<>();
        loadUsers();
    }

    public void registerUser(String username, String password) {
        if (users.containsKey(username)) {
            System.out.println("Username already exists.");
            return;
        }
        users.put(username, new User(username, password));
        saveUsers();
        System.out.println("User registered successfully.");
    }

    public boolean loginUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            currentUser = users.get(username);
            System.out.println("Login successful.");
            return true;
        }
        System.out.println("Invalid username or password.");
        return false;
    }

    public void addExpense(LocalDate date, String category, double amount) {
        if (currentUser != null) {
            currentUser.addExpense(new Expense(date, category, amount));
            saveUsers();
        } else {
            System.out.println("No user is logged in.");
        }
    }

    public void listExpenses() {
        if (currentUser != null) {
            List<Expense> expenses = currentUser.getExpenses();
            expenses.sort(Comparator.comparing(Expense::getDate));
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
        } else {
            System.out.println("No user is logged in.");
        }
    }

    public void showCategorySummary() {
        if (currentUser != null) {
            Map<String, Double> categorySummary = new HashMap<>();
            for (Expense expense : currentUser.getExpenses()) {
                categorySummary.put(expense.getCategory(),
                        categorySummary.getOrDefault(expense.getCategory(), 0.0) + expense.getAmount());
            }
            for (Map.Entry<String, Double> entry : categorySummary.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } else {
            System.out.println("No user is logged in.");
        }
    }

    private void loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.dat"))) {
            users = (Map<String, User>) ois.readObject();
        } catch (FileNotFoundException e) {
            // No users file found, continue with empty users map
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Add Expense");
            System.out.println("4. List Expenses");
            System.out.println("5. Category Summary");
            System.out.println("6. Exit");

            String input = scanner.nextLine();
            int choice = -1;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Enter username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();
                    tracker.registerUser(username, password);
                    break;
                case 2:
                    System.out.println("Enter username:");
                    String loginUsername = scanner.nextLine();
                    System.out.println("Enter password:");
                    String loginPassword = scanner.nextLine();
                    tracker.loginUser(loginUsername, loginPassword);
                    break;
                case 3:
                    if (tracker.currentUser == null) {
                        System.out.println("You need to login first.");
                        break;
                    }
                    try {
                        System.out.println("Enter date (YYYY-MM-DD):");
                        LocalDate date = LocalDate.parse(scanner.nextLine());
                        System.out.println("Enter category:");
                        String category = scanner.nextLine();
                        System.out.println("Enter amount:");
                        double amount = Double.parseDouble(scanner.nextLine());
                        if (amount < 0) {
                            throw new NumberFormatException("Amount cannot be negative.");
                        }
                        tracker.addExpense(date, category, amount);
                    } catch (Exception e) {
                        System.out.println("Invalid input: " + e.getMessage());
                    }
                    break;
                case 4:
                    tracker.listExpenses();
                    break;
                case 5:
                    tracker.showCategorySummary();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }
    }
}


