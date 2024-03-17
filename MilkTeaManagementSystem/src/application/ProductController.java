package application;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductController implements Initializable{


    @FXML
    private Button product_add_btn;

    @FXML
    private ImageView product_image;

    @FXML
    private Label product_name_and_size;

    @FXML
    private Label product_price;

    @FXML
    private Spinner<Integer> product_spinner;
    
    private Product product;
    
    private ProductController controller;
    
    
    private int productAmount;
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createSpinner();
		
		
	}
   
    public void createSpinner() {
    	SpinnerValueFactory<Integer> spinnerValue = new IntegerSpinnerValueFactory(1, 50);
    	spinnerValue.setValue(1);
    	product_spinner.setValueFactory(spinnerValue);
    }
    public void setData(Product product) {
    	this.product = product;
    	try {
//    	Create Image;
//    	String imageURL =product.getImage().replace("\\", "/");
    	//Tạo đường dẫn tuyệt đối tới file ảnh để không dính ngoại lệ. Cách này chắc chắn ko lỗi	
//    	Path imagePath = Paths.get("src/"+product.getImage());
//    	URL imageUrl = imagePath.toUri().toURL();
//    	Image image = new Image(imageUrl.toString());
    	
//  Use for debug System.out.println("Đường dẫn hình ảnh: " + imagePath.toUri().toURL());
    	//Cách thay thế, dùng để xuất file JAR ra, nên dùng cách này. Lúc trc nó lỗi
    	//Nhưng giờ ko biết sao lại hết lỗi
    	String url = product.getImage().replace("\\", "/").replace("//", "/");
    	Image image = new Image(url);
//    	Image image = new Image(this.product.getImage());
    	product_image.setImage(image);
    	//Set product name and size;
    	product_name_and_size.setText(this.product.getProductName()+" ("+this.product.getSize()+")");
    	product_price.setText(String.valueOf(this.product.getPrice()));
    	} catch(Exception e) {
    		System.out.println("Dang o ProductController");
    		e.printStackTrace();
    	}
    }
    public void addBtn() {
    	System.out.println(product.getProductName());
    	System.out.println(product_spinner.getValue());
    	productAmount = product_spinner.getValue();
//    	Stage stage = (Stage) product_add_btn.getScene().getWindow();
//    	System.out.println(stage.getScene());
    	Scene scene = product_add_btn.getScene();
//    	ListView<String> listview = (ListView<String>) scene.getUserData();
    	List<Object> l = (List<Object>) scene.getUserData();
    	//Set list view in list receiving from scene
    	ListView<String> listview = (ListView<String>) l.get(0);
    	//Set amount in list receiving from scene
    	TextField selling_amount = (TextField) l.get(1);
    	TextField selling_price = (TextField) l.get(2);
    	String productDetail = product.getProductName() + " | "+ product.getSize() + 
    							" | "+ product.getPrice() + " x"+productAmount;
    	int totalAmount = 0;
    	float totalPrice = 0.0f;
    	if (listview.getItems().isEmpty()) {
    		listview.getItems().add(productDetail);
    		//Initialize total amount and assign selling_amount if the list view is empty
    		totalAmount += productAmount;
    		selling_amount.setText(String.valueOf(totalAmount));
    		//Initialize price and assign selling_price if the list view is empty-------
    		int sizeIndex = productDetail.indexOf("| ")+1;
    		int priceBeginIndex = productDetail.indexOf("| ",sizeIndex)+2;
    		int priceEndIndex = productDetail.indexOf(" x");
    		float price = Float.valueOf(productDetail.substring(priceBeginIndex, priceEndIndex));
    		totalPrice = price * totalAmount;
    		//Round 2 decimal 
    		DecimalFormat df = new DecimalFormat("0.00");
    		totalPrice = Float.valueOf(df.format(totalPrice));
    		selling_price.setText(String.valueOf(totalPrice));
    		
    		
    	} else {
    		boolean test = false;
    		for (String list : listview.getItems()) {
    			int firstIndex = list.indexOf(" | ");
    			int secondIndex = list.lastIndexOf(" |");
    			String productInList = list.substring(0, firstIndex);
    			String sizeInList = list.substring(firstIndex+3, secondIndex);
    			if (productInList.equals(product.getProductName()) && sizeInList.equals(product.getSize())) {
    				test = true;
    			}
    		}
    		if (!test) {
    			listview.getItems().add(productDetail);
    			//-----increase the amount when user adds an item------------------
    			totalAmount = productAmount;
    			totalAmount += Integer.valueOf(selling_amount.getText());
    			selling_amount.setText(String.valueOf(totalAmount));
    			System.out.println("Tong la: "+totalAmount);
    			//-----increase price when user adds an item------------------------
    			int sizeIndex = productDetail.indexOf("| ")+1;
        		int priceBeginIndex = productDetail.indexOf("| ",sizeIndex)+2;
        		int priceEndIndex = productDetail.indexOf(" x");
        		float price = Float.valueOf(productDetail.substring(priceBeginIndex, priceEndIndex));
        		totalPrice = Float.valueOf(selling_price.getText());
        		totalPrice += (price * productAmount);
        		//Round two decimal
        		DecimalFormat df = new DecimalFormat("0.00");
        		totalPrice = Float.valueOf(df.format(totalPrice));
        		selling_price.setText(String.valueOf(totalPrice));
    		}
    	}
    	
    }
    public int getAmount() {
    	return productAmount;
    }
    
}
