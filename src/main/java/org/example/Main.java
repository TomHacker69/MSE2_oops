
package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        OrderDAO dao = new OrderDAO();

        try (Connection con = DBConnection.getConnection()) {

            System.out.println("Database Connected Successfully!");

            int choice;

            do {

                System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
                System.out.println("1. Insert Orders");
                System.out.println("2. Search Orders");
                System.out.println("3. Increase Quantity");
                System.out.println("4. Update Status");
                System.out.println("5. Display Total Order Value");
                System.out.println("6. Delete Cancelled Order");
                System.out.println("0. Exit");

                System.out.print("Enter choice: ");
                choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {

                    case 1:
                        dao.insertOrders(con);
                        break;

                    case 2:
                        System.out.print("Enter status: ");
                        String status = sc.nextLine();

                        dao.searchOrders(con, status);
                        break;

                    case 3:
                        System.out.print("Enter Order ID: ");
                        int id = sc.nextInt();

                        System.out.print("Enter additional quantity: ");
                        int qty = sc.nextInt();
                        sc.nextLine();

                        dao.increaseQuantity(con, id, qty);
                        break;

                    case 4:
                        System.out.print("Enter Order ID: ");
                        int orderId = sc.nextInt();
                        sc.nextLine();

                        System.out.print("Enter new status: ");
                        String newStatus = sc.nextLine();

                        dao.updateStatus(con, orderId, newStatus);
                        break;

                    case 5:
                        dao.displayTotal(con);
                        break;

                    case 6:
                        System.out.print("Enter Order ID: ");
                        int deleteId = sc.nextInt();
                        sc.nextLine();

                        dao.deleteOrder(con, deleteId);
                        break;

                    case 0:
                        System.out.println("Thank you!");
                        break;

                    default:
                        System.out.println("Invalid choice.");
                }

            } while (choice != 0);

        } catch (SQLException e) {

            System.out.println("Database Error: " + e.getMessage());

        } finally {
            sc.close();
        }
    }
}
