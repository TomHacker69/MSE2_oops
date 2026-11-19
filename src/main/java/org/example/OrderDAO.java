
package org.example;

import java.sql.*;

public class OrderDAO {

    // Insert three sample orders
    public void insertOrders(Connection con) throws SQLException {

        String sql = "INSERT INTO Orders VALUES (?, ?, ?, ?, ?, ?)";

        Object[][] data = {
                {101, "Ankit", "Laptop", 2, 55000.0, "Pending"},
                {102, "Rahul", "Mouse", 5, 500.0, "Shipped"},
                {103, "Aman", "Keyboard", 3, 1200.0, "Cancelled"}
        };

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            for (Object[] row : data) {

                try {
                    for (int i = 0; i < row.length; i++) {
                        ps.setObject(i + 1, row[i]);
                    }

                    ps.executeUpdate();
                    System.out.println("Order inserted: " + row[0]);

                } catch (SQLException e) {

                    if (e.getSQLState() != null &&
                            e.getSQLState().startsWith("23")) {

                        System.out.println("Duplicate Order ID: " + row[0]);

                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    // Search orders by status and quantity > 2
    public void searchOrders(Connection con, String status)
            throws SQLException {

        String sql = "SELECT * FROM Orders WHERE status=? " +
                "AND quantity>2 ORDER BY unit_price DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);

            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                System.out.println(
                        rs.getInt("order_id") + " | " +
                                rs.getString("customer_name") + " | " +
                                rs.getString("product_name") + " | " +
                                rs.getInt("quantity") + " | " +
                                rs.getDouble("unit_price") + " | " +
                                rs.getString("status")
                );
            }

            if (!found) {
                System.out.println("No matching orders found.");
            }
        }
    }

    // Increase order quantity
    public void increaseQuantity(Connection con, int id, int qty)
            throws SQLException {

        if (qty <= 0) {
            System.out.println("Quantity must be positive.");
            return;
        }

        String sql = "UPDATE Orders SET quantity=quantity+? " +
                "WHERE order_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, qty);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Quantity updated successfully.");
            else
                System.out.println("Order not found.");
        }
    }

    // Update order status
    public void updateStatus(Connection con, int id, String status)
            throws SQLException {

        String check = "SELECT * FROM Orders WHERE order_id=?";

        try (PreparedStatement ps = con.prepareStatement(check)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Order not found.");
                return;
            }
        }

        if (!status.equalsIgnoreCase("Pending") &&
                !status.equalsIgnoreCase("Shipped") &&
                !status.equalsIgnoreCase("Delivered")) {

            System.out.println("Invalid status.");
            return;
        }

        String sql = "UPDATE Orders SET status=? WHERE order_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);

            ps.executeUpdate();

            System.out.println("Status updated successfully.");
        }

        // Display updated order details
        String query = "SELECT * FROM Orders WHERE order_id=?";

        try (PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println(
                        "Order ID: " + rs.getInt("order_id") +
                                ", Product: " + rs.getString("product_name") +
                                ", Status: " + rs.getString("status")
                );
            }
        }
    }

    // Calculate total order value
    public void displayTotal(Connection con) throws SQLException {

        String sql = "SELECT SUM(quantity * unit_price) AS total " +
                "FROM Orders";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println(
                        "Total Order Value: " + rs.getDouble("total")
                );
            }
        }
    }

    // Delete only cancelled orders
    public void deleteOrder(Connection con, int id)
            throws SQLException {

        String sql = "DELETE FROM Orders " +
                "WHERE order_id=? AND status=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setString(2, "Cancelled");

            int rows = ps.executeUpdate();

            if (rows > 0)
                System.out.println("Order deleted successfully.");
            else
                System.out.println("Order not found or not Cancelled.");
        }
    }
}
