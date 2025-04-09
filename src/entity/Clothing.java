package entity;

public class Clothing  extends Product{
	//instance variable
	private String size;
    private String color;
    
    //default constructor
	public Clothing() {
		super();
	}
	
	//parameterized constructor
	public Clothing(int productId, String productName, String description, double price, int quantityInStock,
			String type, String size, String color) {
		super(productId, productName, description, price, quantityInStock, type);
		this.size = size;
        this.color = color;
	}
	
	//getter and setter
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Clothing [size=" + size + ", color=" + color + "]";
	}

	
}
