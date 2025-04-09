package entity;

public class Product {
	    private int productId;
	    private String productName;
	    private String description;
	    private double price;
	    private int quantityInStock;
	    private String type;
	    
	    //default constructor
		public Product() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		//parameterized constructor
		public Product(int productId, String productName, String description, double price, int quantityInStock,
				String type) {
			super();
			this.productId = productId;
			this.productName = productName;
			this.description = description;
			this.price = price;
			this.quantityInStock = quantityInStock;
			this.type = type;
		}
		
		//getter & setter
		public int getProductId() {
			return productId;
		}

		public void setProductId(int productId) {
			this.productId = productId;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public int getQuantityInStock() {
			return quantityInStock;
		}

		public void setQuantityInStock(int quantityInStock) {
			this.quantityInStock = quantityInStock;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return "Product [productId=" + productId + ", productName=" + productName + ", description=" + description
					+ ", price=" + price + ", quantityInStock=" + quantityInStock + ", type=" + type + "]";
		}
		
		
}
