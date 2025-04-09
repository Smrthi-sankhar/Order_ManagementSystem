package main;

import dao.IOrderManagementRepository;
import dao.OrderProcessor;
import entity.*;

import java.sql.SQLException;
import java.util.*;

public class MainModule {
    public static void main(String[] args) throws SQLException {
        Scanner sc = new Scanner(System.in);
        IOrderManagementRepository repo = new OrderProcessor();

        String continueChoice;

        System.out.println(" Welcome to the Order Management System ");

        do {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Create User");
            System.out.println("2. Create Product (Admin only)");
            System.out.println("3. Create Order");
            System.out.println("4. Cancel Order");
            System.out.println("5. Get All Products");
            System.out.println("6. Get Orders By User");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter User ID: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Username: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Password: ");
                        String pass = sc.nextLine();
                        System.out.print("Enter Role (Admin/User): ");
                        String role = sc.nextLine();

                        User user = new User(id, name, pass, role);
                        repo.createUser(user);
                        System.out.println("yeah!! User created successfully.");
                    }

                    case 2 -> {
                        User admin = getUserInput(sc);
                        System.out.print("Enter Product ID: ");
                        int pid = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Product Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Description: ");
                        String desc = sc.nextLine();
                        System.out.print("Enter Price: ");
                        double price = sc.nextDouble();
                        System.out.print("Enter Quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Enter Type (Electronics/Clothing): ");
                        String type = sc.nextLine();

                        Product product = null;
                        if (type.equalsIgnoreCase("Electronics")) {
                            System.out.print("Enter Brand: ");
                            String brand = sc.nextLine();
                            System.out.print("Enter Warranty Period (months): ");
                            int wp = sc.nextInt();
                            product = new Electronics(pid, name, desc, price, qty, type, brand, wp);
                        } else if (type.equalsIgnoreCase("Clothing")) {
                            System.out.print("Enter Size: ");
                            String size = sc.nextLine();
                            System.out.print("Enter Color: ");
                            String color = sc.nextLine();
                            product = new Clothing(pid, name, desc, price, qty, type, size, color);
                        }

                        repo.createProduct(admin, product);
                        System.out.println("oiii Product created successfully.");
                    }

                    case 3 -> {
                        User user = getUserInput(sc);
                        List<Product> products = new ArrayList<>();
                        System.out.print("How many products to order? ");
                        int count = sc.nextInt();

                        for (int i = 0; i < count; i++) {
                            System.out.print("Enter Product ID: ");
                            int pid = sc.nextInt();
                            Product p = new Product();
                            p.setProductId(pid);
                            products.add(p);
                        }

                        repo.createOrder(user, products);
                        System.out.println("Order placed successfully.");
                    }

                    case 4 -> {
                        System.out.print("Enter User ID: ");
                        int uid = sc.nextInt();
                        System.out.print("Enter Order ID: ");
                        int oid = sc.nextInt();
                        repo.cancelOrder(uid, oid);
                        System.out.println("Order canceled successfully.");
                    }

                    case 5 -> {
                        List<Product> productList = repo.getAllProducts();
                        System.out.println("--- All Products ---");
                        productList.forEach(System.out::println);
                    }

                    case 6 -> {
                        User user = getUserInput(sc);
                        List<Product> userOrders = repo.getOrderByUser(user);
                        System.out.println("--- Orders by User ---");
                        userOrders.forEach(System.out::println);
                    }

                    case 7 -> {
                        System.out.println("Exiting system. Goodbye!");
                        return;
                    }

                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println(" Error: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.print("Do you want to continue (yes/no)? ");
            continueChoice = sc.nextLine(); // ✅ FIXED: only one nextLine

        } while (continueChoice.equalsIgnoreCase("yes"));

        sc.close();
        System.out.println("Session ended. Thank you!");
    }

    private static User getUserInput(Scanner sc) {
        System.out.print("Enter User ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Username: ");
        String name = sc.nextLine();
        System.out.print("Enter Password: ");
        String pass = sc.nextLine();
        System.out.print("Enter Role: ");
        String role = sc.nextLine();
        return new User(id, name, pass, role);
    }
}


