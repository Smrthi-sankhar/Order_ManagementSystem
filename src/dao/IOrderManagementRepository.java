package dao;
import entity.Product;
import entity.User;
import java.util.List;

public interface IOrderManagementRepository {
	//createUser
	void createUser(User user) throws  Exception;
	//createProduct
    void createProduct(User user, Product product) throws Exception;
    //createOrder
    void createOrder(User user, List<Product> productList) throws Exception;
    //cancelOrder
    void cancelOrder(int userId, int orderId) throws Exception;
    //getAllProducts
    List<Product> getAllProducts() throws Exception;
    //getOrderByUser
    List<Product> getOrderByUser(User user) throws Exception;   
}
