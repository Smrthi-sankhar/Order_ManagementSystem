package dao;
import entity.*;
import util.DBConnUtil;

import java.sql.*;
import java.util.*;

public class OrderProcessor implements IOrderManagementRepository {
	
	private static Connection connection;

    public OrderProcessor() throws SQLException {
        connection = DBConnUtil.getDbConnection();
    }

    public void createUser(User user) throws Exception {
        String sql = "INSERT INTO users (userId, username, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();
            System.out.println("User created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
            throw new Exception("Failed to create user", e);
        }
    }
    
    @Override
    public List<Product> getAllProducts() throws Exception {
        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String type = rs.getString("type");

                if (type.equalsIgnoreCase("Electronics")) {
                    Electronics e = new Electronics(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantityInStock"),
                        type,
                        rs.getString("brand"),
                        rs.getInt("warrantyPeriod")
                    );
                    products.add(e);
                } else if (type.equalsIgnoreCase("Clothing")) {
                    Clothing c = new Clothing(
                        rs.getInt("productId"),
                        rs.getString("productName"),
                        rs.getString("description"),
                        rs.getDouble("price"),
                        rs.getInt("quantityInStock"),
                        type,
                        rs.getString("size"),
                        rs.getString("color")
                    );
                    products.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving products: " + e.getMessage());
            throw new Exception("Failed to retrieve products", e);
        }

        return products;
    }

    @Override
    public void createProduct(User user, Product product) throws Exception {
        if (!user.getRole().equalsIgnoreCase("Admin")) {
            throw new Exception("Only admin can create a product.");
        }

        String query = "INSERT INTO products (productId, productName, description, price, quantityInStock, type, brand, warrantyPeriod, size, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getProductName());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getQuantityInStock());
            stmt.setString(6, product.getType());

            // Set subclass-specific fields
            if (product instanceof Electronics e) {
                stmt.setString(7, e.getBrand());
                stmt.setInt(8, e.getWarrantyPeriod());
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
            } else if (product instanceof Clothing c) {
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.INTEGER);
                stmt.setString(9, c.getSize());
                stmt.setString(10, c.getColor());
            } else {
                // For safety: if product is not Electronics or Clothing
                stmt.setNull(7, Types.VARCHAR);
                stmt.setNull(8, Types.INTEGER);
                stmt.setNull(9, Types.VARCHAR);
                stmt.setNull(10, Types.VARCHAR);
            }

            stmt.executeUpdate();
            System.out.println("Product created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating product: " + e.getMessage());
            throw new Exception("Failed to create product", e);
        }
    }

		@Override
		public void createOrder(User user, List<Product> productList) throws Exception {
		    try {
		        connection.setAutoCommit(false);

		        // Check if user exists
		        PreparedStatement userCheck = connection.prepareStatement("SELECT * FROM users WHERE userId = ?");
		        userCheck.setInt(1, user.getUserId());
		        ResultSet rs = userCheck.executeQuery();

		        if (!rs.next()) {
		            // User doesn't exist, create them
		            createUser(user);
		        }

		        // Insert into orders table
		        String orderQuery = "INSERT INTO orders (userId) VALUES (?)";
		        PreparedStatement orderStmt = connection.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS);
		        orderStmt.setInt(1, user.getUserId());
		        orderStmt.executeUpdate();

		        ResultSet generatedKeys = orderStmt.getGeneratedKeys();
		        int orderId = -1;
		        if (generatedKeys.next()) {
		            orderId = generatedKeys.getInt(1);
		        } else {
		            throw new Exception("Order ID generation failed.");
		        }

		        // Insert into order_products table
		        String orderProductQuery = "INSERT INTO order_products (orderId, productId) VALUES (?, ?)";
		        PreparedStatement orderProductStmt = connection.prepareStatement(orderProductQuery);

		        for (Product product : productList) {
		            orderProductStmt.setInt(1, orderId);
		            orderProductStmt.setInt(2, product.getProductId());
		            orderProductStmt.executeUpdate();
		        }

		        connection.commit();
		        System.out.println("Order created successfully with ID: " + orderId);
		    } catch (SQLException e) {
		        connection.rollback();
		        System.out.println("Order creation failed: " + e.getMessage());
		        throw new Exception("Failed to create order", e);
		    } finally {
		        connection.setAutoCommit(true);
		    }
		

		
		
	}


		@Override
		public void cancelOrder(int userId, int orderId) throws Exception {
		    try {
		        connection.setAutoCommit(false);

		        // Check if order exists and belongs to the user
		        PreparedStatement checkStmt = connection.prepareStatement("SELECT * FROM orders WHERE orderId = ? AND userId = ?");
		        checkStmt.setInt(1, orderId);
		        checkStmt.setInt(2, userId);
		        ResultSet rs = checkStmt.executeQuery();

		        if (!rs.next()) {
		            throw new Exception("Order ID or User ID not found!");
		        }

		        // Delete from order_products first
		        PreparedStatement deleteOP = connection.prepareStatement("DELETE FROM order_products WHERE orderId = ?");
		        deleteOP.setInt(1, orderId);
		        deleteOP.executeUpdate();

		        // Then delete from orders
		        PreparedStatement deleteOrder = connection.prepareStatement("DELETE FROM orders WHERE orderId = ?");
		        deleteOrder.setInt(1, orderId);
		        deleteOrder.executeUpdate();

		        connection.commit();
		        
		    } catch (SQLException e) {
		        connection.rollback();
		        throw new Exception("Failed to cancel order: " + e.getMessage(), e);
		    } finally {
		        connection.setAutoCommit(true);
		    }
		}




		@Override
		public List<Product> getOrderByUser(User user) throws Exception {
		    List<Product> products = new ArrayList<>();

		    String query = """
		        SELECT p.* FROM products p 
		        JOIN order_products op ON p.productId = op.productId 
		        JOIN orders o ON op.orderId = o.orderId 
		        WHERE o.userId = ?
		    """;

		    try (PreparedStatement stmt = connection.prepareStatement(query)) {
		        stmt.setInt(1, user.getUserId());
		        ResultSet rs = stmt.executeQuery();

		        while (rs.next()) {
		            String type = rs.getString("type");

		            if (type.equalsIgnoreCase("Electronics")) {
		                Electronics e = new Electronics(
		                    rs.getInt("productId"),
		                    rs.getString("productName"),
		                    rs.getString("description"),
		                    rs.getDouble("price"),
		                    rs.getInt("quantityInStock"),
		                    type,
		                    rs.getString("brand"),
		                    rs.getInt("warrantyPeriod")
		                );
		                products.add(e);
		            } else if (type.equalsIgnoreCase("Clothing")) {
		                Clothing c = new Clothing(
		                    rs.getInt("productId"),
		                    rs.getString("productName"),
		                    rs.getString("description"),
		                    rs.getDouble("price"),
		                    rs.getInt("quantityInStock"),
		                    type,
		                    rs.getString("size"),
		                    rs.getString("color")
		                );
		                products.add(c);
		            }
		        }
		    }
		    return products;
	}

}
