package application;

public class Product {

	private String productName;
	private int quantity;
	private String size;
	private float price;
	private int status;
	private String image;
	public Product(String productName, int quantity, String size, float price, int status, String image) {
		this.productName = productName;
		this.quantity = quantity;
		this.size = size;
		this.price = price;
		this.status = status;
		this.image = image;
	}
	public Product(String productName, int quantity, String size, float price) {
		this.productName = new String(productName);
		this.quantity = quantity;
		this.size = new String(size);
		this.price = price;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
