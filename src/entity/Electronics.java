package entity;

public class Electronics extends Product {
	//instance variable
	private String brand;
    private int warrantyPeriod;
    
    //default constructor
	public Electronics() {
		super();
	}
	//parameterized constructor
	public Electronics(int productId, String productName, String description, double price, int quantityInStock,
			String type, String brand, int warrantyPeriod) {
		super(productId, productName, description, price, quantityInStock, type);
		this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
	}
	
	//getter and setter
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public int getWarrantyPeriod() {
		return warrantyPeriod;
	}
	public void setWarrantyPeriod(int warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}
	@Override
	public String toString() {
		return "Electronics [brand=" + brand + ", warrantyPeriod=" + warrantyPeriod + "]";
	}
	
	
}
