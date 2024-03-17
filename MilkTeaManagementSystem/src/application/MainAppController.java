package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainAppController implements Initializable {

	@FXML
	private AnchorPane main_pane;

	@FXML
	private AnchorPane user_exitpane;

	@FXML
	private Button user_logout_btn;

	@FXML
	private TableColumn<Product, String> product_name;

	@FXML
	private TableColumn<Product, Float> product_price;

	@FXML
	private TableColumn<Product, Integer> product_quantity;

	@FXML
	private TableColumn<Product, String> product_size;

	@FXML
	private TableColumn<Product, Integer> product_status;

	@FXML
	private TableView<Product> product_table;

	@FXML
	private TextField product_name_field;

	@FXML
	private TextField product_price_field;

	@FXML
	private TextField product_quantity_field;

	@FXML
	private ChoiceBox<String> product_size_field;

	@FXML
	private ChoiceBox<Integer> product_status_field;

	@FXML
	private ImageView product_image;

	@FXML
	private Button product_import_btn;

	private String imagePath;

	private int product_id;
	
	private Image image;
	
	private String currentProduct;
	
	private String currentSize;
	
    @FXML
    private GridPane selling_container;
	
    @FXML
    private ScrollPane selling_scroll;
    
    @FXML
    private TextField selling_amount;

    @FXML
    private Button selling_pay_btn;

    @FXML
    private ListView<String> selling_product;

    @FXML
    private TextField selling_phone;
    
    @FXML
    private Button selling_receipt_btn;

    @FXML
    private Button selling_remove_btn;
    
    @FXML
    private TextField selling_total;
    
    @FXML
    private Button tab_dashboard;

    @FXML
    private Button tab_statictics;

    @FXML
    private Button tab_product;

    @FXML
    private Button tab_selling;
    
    @FXML
    private AnchorPane main_daskboard;
    
    @FXML
    private AnchorPane main_product;

    @FXML
    private AnchorPane main_selling;

    @FXML
    private AnchorPane main_statictics;
    
    @FXML
    private Label main_title;
    
    @FXML
    private Label dash_daily_revenue;

    @FXML
    private Label dash_quantity;

    @FXML
    private Label dash_revenue;

    @FXML
    private Label dash_sold;

    private int productAmount = 0;
    
    @FXML
    private TableView<Customer> stat_customer_table;
    
    @FXML
    private TableColumn<Customer, Date> stat_customer_date;

    @FXML
    private TableColumn<Customer, Integer> stat_customer_id;

    @FXML
    private TableColumn<Customer, Integer> stat_customer_phone;

    @FXML
    private TableColumn<Customer, Integer> stat_customer_times;

    @FXML
    private VBox stat_container;
    
    @FXML
    private HBox search_bar;

    @FXML
    private Button search_btn;

    @FXML
    private TextField search_input;
    
    @FXML
    private Label username;
    
	int countClick = 0;

	private int customer_id;
	
	String employee_id;
	
	ObservableList<Product> productList;

	ObservableList<Customer> customerList;
	
	String[] sizeList = {"M", "L", "XL"};
	
	Integer[] statusList = {1,0};
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		showProduct();
		addDataToChoiceBox();
	 	getSelectionProduct();
	 	try {
			showProductCard();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 	//show customer list in statistics
	 	showCustomer();
	 	showChart();
	 	setDashboard();
	 	//hide search bar
	 	search_bar.setVisible(false);
	 	System.out.println(username);
	}
	//test
	public void setUserName(String usr) {
		this.username.setText(usr);
	}
	
	public void getSelectionProduct() {
		product_table.getSelectionModel().selectedItemProperty().addListener(e->{
			//Get current selected product
			int index = product_table.getSelectionModel().getSelectedIndex();
			Product currentProduct = product_table.getSelectionModel().getSelectedItem();
			if (currentProduct!=null) {
				String productName = currentProduct.getProductName();
				int quantity = currentProduct.getQuantity();
				String size = currentProduct.getSize();
				float price = currentProduct.getPrice();
				Integer status = currentProduct.getStatus();
				String imagePath = currentProduct.getImage();
				this.imagePath = imagePath;
				//Set value to adding fields
				product_name_field.setText(productName);
				product_quantity_field.setText(String.valueOf(quantity));
				product_size_field.getSelectionModel().select(size);
				product_price_field.setText(String.valueOf(price));
				product_status_field.getSelectionModel().select(status);
//Code cu		Image image = new Image(imagePath, 71, 68, false, true);
//fix loi relative
//				try {
//					FileInputStream input = new FileInputStream(imagePath);
//					Image image = new Image(input);
//					product_image.setImage(image);
//				} catch(Exception es) {
//					System.out.println(es);
//				}
				//convert the path
				imagePath = imagePath.replace("\\", "/").replace("//", "/");
				Image image = new Image(imagePath);
				product_image.setImage(image);
				product_id = searchProductId(currentProduct);
				System.out.println(product_id);
				System.out.println("Duong dan tai ham getSelection: "+imagePath);
				//Get current product and Size
				this.currentProduct = productName;
				this.currentSize = size;
				this.imagePath = imagePath;
				System.out.println(this.currentProduct+" - "+this.currentSize);
			}
			
			
		});
	}
	public int searchProductId(Product product) {
		String sql = "SELECT productid FROM product WHERE productname = ? AND"
				+ " size = ?";
		Connection con = Database.connectDB();
		int id = 0;
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, product.getProductName());
			ps.setString(2, product.getSize());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}
	public int searchProductId(String productName, String size) {
		String sql = "SELECT productid FROM product WHERE productname = ? AND"
				+ " size = ?";
		Connection con = Database.connectDB();
		int id = 0;
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, productName);
			ps.setString(2, size);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		return id;
	}
	public ObservableList<Product> getProductList(int statusCondition) {
		ObservableList<Product> productArray = FXCollections.observableArrayList();
		Connection con = Database.connectDB();
		//modify 
		//if statusCondition is 1 then show full productlist, else show only product with status = 1
		String sql;
		if (statusCondition==1) {
			sql = "SELECT * FROM product";
		}
		 else {
			sql = "SELECT * FROM product WHERE status != 0";
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// Add column into variables
				String productName = rs.getString(2);
				int quantity = rs.getInt(3);
				String size = rs.getString(4);
				float price = rs.getFloat(5);
				int status = rs.getInt(6);
				String image = rs.getString(7);
				// Create product object to save these variables
				Product milktea = new Product(productName, quantity, size, price, status, image);
				productArray.add(milktea);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productArray;
	}
	
	public ObservableList<Product> getProductList(String searchText) {
		ObservableList<Product> productArray = FXCollections.observableArrayList();
		Connection con = Database.connectDB();
		String sql = "SELECT * FROM product WHERE productname LIKE '%" + searchText + "%'";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
//			ps.setString(1, searchText);
//			ps.setString(2, searchText);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// Add column into variables
				String productName = rs.getString(2);
				int quantity = rs.getInt(3);
				String size = rs.getString(4);
				float price = rs.getFloat(5);
				int status = rs.getInt(6);
				String image = rs.getString(7);
				// Create product object to save these variables
				Product milktea = new Product(productName, quantity, size, price, status, image);
				productArray.add(milktea);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productArray;
	}
	
	public void showProduct() {
		productList = getProductList(1);
		product_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
		product_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		product_size.setCellValueFactory(new PropertyValueFactory<>("size"));
		product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
		product_status.setCellValueFactory(new PropertyValueFactory<>("status"));
		product_table.setItems(productList);

	}
	public void showProduct(ObservableList<Product> list) {
		productList = list;
		product_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
		product_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		product_size.setCellValueFactory(new PropertyValueFactory<>("size"));
		product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
		product_status.setCellValueFactory(new PropertyValueFactory<>("status"));
		product_table.setItems(productList);
	}
	public void showExit(MouseEvent e) {
		countClick += e.getClickCount();
		System.out.println("Count click is: " + countClick);
		if (e.getButton().equals(MouseButton.PRIMARY)) {
			if (countClick % 2 != 0) {
				user_exitpane.setVisible(true);
			} else {
				user_exitpane.setVisible(false);
			}
		}
	}

	public void logout(ActionEvent e) throws IOException {
		Stage stage = (Stage) user_logout_btn.getScene().getWindow();
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setContentText("Do you want to log out?");
		if (alert.showAndWait().get().equals(ButtonType.OK)) {
			stage.close();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("login-register-scene.fxml"));
			AnchorPane form = loader.load();
			Scene scene = new Scene(form);
			Stage newStage = new Stage();
			newStage.setScene(scene);
			newStage.show();
		}
	}
	
	public void addDataToChoiceBox() {
		product_size_field.getItems().addAll(sizeList);
		product_status_field.getItems().addAll(statusList);
	}
	
	public void addProduct() {
		if (product_name_field.getText().isEmpty()
			|| product_quantity_field.getText().isEmpty()
			|| product_price_field.getText().isEmpty()
			|| product_size_field.getSelectionModel().isEmpty() 
			|| product_status_field.getSelectionModel().isEmpty()
			|| product_image.getImage()==null) {
			showError("Please fill all blank fields");
		} else if (checkExistingProduct(product_name_field.getText())) {
			showError("The product already exists");
		} else if (checkFormatNumber()) {
			showError("The format of quantity or price is not correct");
			} else {
			//Copy image to iamges folder of the project and return file path
			this.imagePath = copyImageToImagesFolder(imagePath);
			Connection con = Database.connectDB();
			String sql = "INSERT INTO product(productname, quantity, size, price, status, image) "
					+ "VALUES(?,?,?,?,?,?)";
			try {
				String productname = product_name_field.getText();
				int quantity = Integer.parseInt(product_quantity_field.getText());
				float price = Float.parseFloat(product_price_field.getText());
				String size = product_size_field.getSelectionModel().getSelectedItem();
				int status = product_status_field.getSelectionModel().getSelectedItem();
				String imagePath = this.imagePath;
				imagePath = imagePath.replace("\\", "\\\\");
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, productname);
				ps.setInt(2, quantity);
				ps.setString(3, size);
				ps.setFloat(4, price);
				ps.setInt(5, status);
				ps.setString(6, imagePath);
				ps.executeUpdate();
				showSuccess("1 product inserted");
				showProduct();
				clearAddingInput();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
	public boolean checkFormatNumber() {
		boolean test = false;
		String quantityRegex = "\\d+";
		String priceRegex = "([0-9]+\\.?[0-9]*)|(\\.[0-9]+)";
		Pattern p1 = Pattern.compile(quantityRegex);
		Pattern p2 = Pattern.compile(priceRegex);
		Matcher m1 = p1.matcher(product_quantity_field.getText());
		Matcher m2 = p2.matcher(product_price_field.getText());
		if (!m1.matches() || !m2.matches()) {
			test = true;
		}
		return test;
	}
	public void deleteProduct() {
		if (product_name_field.getText().isEmpty()
				|| product_quantity_field.getText().isEmpty()
				|| product_price_field.getText().isEmpty()
				|| product_size_field.getSelectionModel().isEmpty() 
				|| product_status_field.getSelectionModel().isEmpty()
				|| product_image.getImage()==null) {
				showError("Please fill all blank fields");
			} else {
//				deleteImage();
				Connection con = Database.connectDB();
				String sql = "DELETE FROM product WHERE productname = ? AND size = ?";
				if (showWarning("Do you want to delete it?").equals(ButtonType.OK)) {
					try {
						PreparedStatement ps = con.prepareStatement(sql);
						ps.setString(1, product_name_field.getText());
						ps.setString(2, product_size_field.getSelectionModel().getSelectedItem());
						ps.executeUpdate();
						con.close();
						clearAddingInput();
						showSuccess("1 product deleted!");
						showProduct();
						//Clear current selection because it throws an error if selecting emtpy item
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
	}
	public void deleteImage() {
		String currentDirectory = System.getProperty("user.dir");
		String filepath = currentDirectory + "\\src\\"+this.imagePath;
		File fileToDelete = new File(filepath);
		if (fileToDelete.delete()) {
			System.out.println("File deleted successfully "+ filepath);
		} else {
			System.err.println("Failed to delete the file "+filepath);
		}
	}
	public void updateProduct() {
		if (product_name_field.getText().isEmpty()
				|| product_quantity_field.getText().isEmpty()
				|| product_price_field.getText().isEmpty()
				|| product_size_field.getSelectionModel().isEmpty() 
				|| product_status_field.getSelectionModel().isEmpty()
				|| product_image.getImage()==null) {
				showError("Please fill all blank fields");
			} else if (currentProduct.equals(product_name_field.getText())==false) {
				showError("You cannot change product name");
			} else if (currentSize.equals(product_size_field.getSelectionModel().getSelectedItem())==false) {
				showError("You cannot change the size");
			} else {
				//Get connection
				Connection con = Database.connectDB();
				int id = product_id;
				String sql = "UPDATE product SET quantity = ?, price = ?, status = ?, image = ? WHERE productid = ?";
				try {
					PreparedStatement ps = con.prepareStatement(sql);
					int quantity = Integer.valueOf(product_quantity_field.getText());
					float price = Float.valueOf(product_price_field.getText());
					int status = product_status_field.getSelectionModel().getSelectedItem();
					String imagePath = this.imagePath;
					ps.setInt(1, quantity);
					ps.setFloat(2, price);
					ps.setInt(3, status);
					ps.setString(4, imagePath);
					ps.setInt(5, id);
					ps.executeUpdate();
					showSuccess("1 product updated");
					showProduct();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	}
	public void clearAddingInput() {
		product_name_field.setText("");
		product_quantity_field.setText("");
		product_size_field.getSelectionModel().clearSelection();
		product_status_field.getSelectionModel().clearSelection();
		product_price_field.setText("");
		product_image.setImage(null);
	}
	public boolean checkExistingProduct(String productname) {
		boolean test = false;
		Connection con = Database.connectDB();
		String sql = "SELECT * FROM product WHERE productname = ? AND size = ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, product_name_field.getText());
			ps.setString(2, product_size_field.getSelectionModel().getSelectedItem());
			ResultSet rs = ps.executeQuery();
			test = rs.next();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return test;
	}
	public void importImageBtn() {
		FileChooser openFile = new FileChooser();
		openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png","*jpg"));
		File file = openFile.showOpenDialog(main_pane.getScene().getWindow());
		if (file != null) {
			imagePath = new String(file.getAbsolutePath());
			//test			//---
//			int index = imagePath.lastIndexOf("\\")+1;
			System.out.println(imagePath);
			
			image = new Image(file.toURI().toString(), 71, 68, false, true);
			product_image.setImage(image);
		}
	}
	public String copyImageToImagesFolder(String source) {
		//Get current folder of the project
		String currentDirectory = System.getProperty("user.dir");
		//get image name
		int imageIndex = source.lastIndexOf("\\")+1;
		String imageName = source.substring(imageIndex);
		String destinationImagePath = currentDirectory + "/src/images/"+imageName;
		Path sourcePath = Paths.get(source);
		Path destinationPath = Paths.get(destinationImagePath);
		try {
			Files.copy(sourcePath, destinationPath);
			System.out.println("Image file copied successfully to dir");;
		} catch(IOException e) {
			System.err.println("Error copying image file: "+e.getMessage());
		}
		return "images\\"+imageName;
	}
	public void showProductCard() throws IOException {
		int column = 0;
		int row = 0;
		selling_container = new GridPane();
		selling_container.setHgap(23);
		selling_container.setVgap(24);
		selling_scroll.setContent(selling_container);
		selling_scroll.setStyle("-fx-background-color: transparent;");
		ObservableList<Product> productCard = getProductList(0);
		for (Product prod: productCard) {
			try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("product.fxml"));
			VBox cardItem = loader.load();
			ProductController proController = loader.getController();
			proController.setData(prod);
			if (column == 3) {
				column = 0;
				++row;
			}
			selling_container.add(cardItem, column, row);
			column++;
			} catch(Exception e) {
				System.out.println("Loi load product.fxml");
				e.printStackTrace();
			}
		}
		VBox vbox = new VBox();
		vbox.setPrefHeight(100);
		selling_container.addRow(0, vbox);
		
	}
	public void showProductCard(ObservableList<Product> e) throws IOException {
		int column = 0;
		int row = 0;
		selling_container = new GridPane();
		selling_container.setHgap(23);
		selling_container.setVgap(24);
		selling_scroll.setContent(selling_container);
		selling_scroll.setStyle("-fx-background-color: transparent;");
		ObservableList<Product> productCard = e;
		for (Product prod: productCard) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("product.fxml"));
			VBox cardItem = loader.load();
			ProductController proController = loader.getController();
			proController.setData(prod);
			if (column == 3) {
				column = 0;
				++row;
			}
			selling_container.add(cardItem, column, row);
			column++;
		}
		VBox vbox = new VBox();
		vbox.setPrefHeight(100);
		selling_container.addRow(0, vbox);
		
	}
	public void payButton() {
		//Get data from list view and push them into database
		int customerid = 0;
		if (selling_product.getItems().isEmpty()) {
			showError("Please add product to menu");
		} else if (selling_phone.getText().isEmpty()) {
			showError("Please input customer phone");
		} else {
			int phone = Integer.valueOf(selling_phone.getText());
			//Check whether customer exists or not
			boolean customerExist = checkExistingCustomer(phone);
			//Get customer id
			customerid = customerExist ? getCustomerId(phone):generateCustomerId();
			Timestamp currentDate = new Timestamp(0);
			for(String e: selling_product.getItems()) {
				String productName = getProductNameFromListView(e);
				String productSize = getSizeFromListView(e);
				int productId = searchProductId(productName, productSize);
				int productAmount = getAmountFromListView(e);
				float productTotal = getPriceFromListView(e);
				Timestamp date = new Timestamp(getCurrentDate());
				currentDate = date;
				String username = this.username.getText();
				//Add fields to order table
				orderProduct(username, productAmount, productTotal, date, customerid, productId);
				
			}
			//If customer does not exist, add new customer otherwise get purchase times and update
			//existing customer
			if (!customerExist) {
				addNewCustomer(customerid, phone, 1, currentDate);
				System.out.println("Add new Customer call");
			} else {
				int times = getPurchaseTimes(customerid) + 1;
				updateCustomer(customerid, times, currentDate);
				System.out.println("Update customer call");
			}
			showSuccess("Selling product successfully!");
			customer_id = customerid;
		}
		
	}
	//generate new customer id and get customer id
	public int generateCustomerId() {
		int id=0;
		try {
			String sql = "SELECT customerid FROM customer";
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			//if table is empty then assign 1 for first person
			while(rs.next()) {
				id = rs.getInt(1);
			}
			id++;
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return id;
	}
	//get customer id through phone 
	public int getCustomerId(int phone) {
		String sql = "SELECT customerid FROM customer WHERE phone = ?";
		int id = 0;
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, phone);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
			}
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		return id;
	}
	//Check existing customer, customer don't exist
	public boolean checkExistingCustomer(int phone) {
		//if customer exists then return true, else false
		boolean existing = false;
		try {
			String sql = "SELECT * FROM customer WHERE phone = ?";
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, phone);
			ResultSet rs = ps.executeQuery();
			existing = rs.next();
			con.close();
			
		} catch(Exception e) {
			System.out.println(e);
		}
		return existing;
	}
	//Add new customer to database
	public void addNewCustomer(int id, int phone, int times, Timestamp date) {
		String sql = "INSERT INTO customer VALUES(?,?,?,?)";
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setInt(2, phone);
			ps.setInt(3, times);
			ps.setTimestamp(4, date);
			ps.executeUpdate();
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	//Get information about customer such as times 
	public int getPurchaseTimes(int id) {
		int times = 0;
		String sql = "SELECT purchasetimes FROM customer WHERE customerid = ?";
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				times = rs.getInt(1);
			}
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		return times;
	}
	//Update a customer
	public void updateCustomer(int id, int times, Timestamp date) {
		String sql = "UPDATE customer SET purchasetimes = ?, recentlydate = ? WHERE customerid = ?";
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, times);
			ps.setTimestamp(2, date);
			ps.setInt(3, id);
			ps.executeUpdate();
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public String getProductNameFromListView(String product) {
		int index = product.indexOf(" |");
		return product.substring(0, index);
	}
	public String getSizeFromListView(String product) {
		int firstIndex = product.indexOf("| ")+2;
		int lastIndex = product.lastIndexOf(" |");
		return product.substring(firstIndex, lastIndex);
	}
	public int getAmountFromListView(String product) {
		int index = product.indexOf("x")+1;
		return Integer.valueOf(product.substring(index));
	}
	public float getPriceFromListView(String product) {
		int firstIndex = product.lastIndexOf("| ")+2;
		int lastIndex = product.indexOf(" x");
		return Float.valueOf(product.substring(firstIndex, lastIndex));
	}
	public long getCurrentDate() {
		java.util.Date date = new Date();
		return date.getTime();
	}
	public void orderProduct(String userName, int amount, float totalPrice, Timestamp date,int phone, int productId) {
		Connection con = Database.connectDB();
		String sql = "INSERT INTO orders(username, amount, totalprice, date, customerid, productid)"
				+ "VALUES(?,?,?,?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, userName);
			ps.setInt(2, amount);
			ps.setFloat(3, totalPrice);
			ps.setTimestamp(4, date);
			ps.setInt(5, phone);
			ps.setInt(6, productId);
			int i = ps.executeUpdate();
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}
	public void recepit() {
		Stage stage = (Stage) main_selling.getScene().getWindow();
		//Check all of field is empty, if empty then return
		if (checkEmptyBill()) {
			showError("There is no customer phone or products");
			return;
		}
		FileChooser file = new FileChooser();
		file.setTitle("Open Save File");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)","*.pdf");
		file.getExtensionFilters().add(extFilter);
		File path = file.showSaveDialog(stage);
		if (path!=null) {
			System.out.println(path.getAbsolutePath());
		} else {
			return;
		}
		//Get data
			String amount = selling_amount.getText();
			String total = selling_total.getText();
			String customerPhone = selling_phone.getText();
			List<Product> productList = new ArrayList<>();
			Product item;
			for (String e: selling_product.getItems()) {
				String productName = getProductNameFromListView(e);
				String productSize = getSizeFromListView(e);
				int productId = searchProductId(productName, productSize);
				int productAmount = getAmountFromListView(e);
				float productTotal = getPriceFromListView(e);
				item = new Product(productName, productAmount, productSize, productTotal);
				productList.add(item);
			}
		//Creating PDF
		try {
			Document document = new Document();
			OutputStream outputStream = new FileOutputStream(path);
			//create PdfWriter object------------------------
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12 , Font.BOLD);
			PdfPTable table = new PdfPTable(5);
			//Header of table------------------------
			PdfPCell cell1_row1 = new PdfPCell(new Paragraph("No",font));
			PdfPCell cell2_row1 = new PdfPCell(new Paragraph("Product Name",font));
			PdfPCell cell3_row1 = new PdfPCell(new Paragraph("Size",font));
			PdfPCell cell4_row1 = new PdfPCell(new Paragraph("Amount",font));
			PdfPCell cell5_row1 = new PdfPCell(new Paragraph("Price",font));
			cell1_row1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2_row1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell3_row1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell4_row1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell5_row1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell1_row1);
			table.addCell(cell2_row1);
			table.addCell(cell3_row1);
			table.addCell(cell4_row1);
			table.addCell(cell5_row1);
			//Add row into table
			Font itemFont = new Font(Font.FontFamily.TIMES_ROMAN);
			int i = 0;
			for(Product e: productList) {
				PdfPCell cell1 = new PdfPCell(new Paragraph(String.valueOf(++i),itemFont));
				PdfPCell cell2 = new PdfPCell(new Paragraph(e.getProductName(),itemFont));
				PdfPCell cell3 = new PdfPCell(new Paragraph(e.getSize(),itemFont));
				PdfPCell cell4 = new PdfPCell(new Paragraph(String.valueOf(e.getQuantity()),itemFont));
				PdfPCell cell5 = new PdfPCell(new Paragraph(String.valueOf(e.getPrice()),itemFont));
				table.addCell(cell1);
				table.addCell(cell2);
				table.addCell(cell3);
				table.addCell(cell4);
				table.addCell(cell5);
			}
			//Create Total Amount, Total Price, and Customerphone, And Title----------------
			
			Paragraph amountPara = new Paragraph("Amount: "+amount,itemFont);
			Paragraph totalPara = new Paragraph("Total: "+total);
			Paragraph phonePara = new Paragraph("Customer phone: "+customerPhone);
			Paragraph title = new Paragraph("Bill", new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD));
			//Right align these element in pdf
			amountPara.setAlignment(Element.ALIGN_RIGHT);
			totalPara.setAlignment(Element.ALIGN_RIGHT);
			//Set right spacing of amount and total
			amountPara.setIndentationRight(55);
			totalPara.setIndentationRight(55);
			phonePara.setIndentationLeft(55);
			//Set center title
			title.setAlignment(Element.ALIGN_CENTER);
			//Set spacing
			title.setSpacingAfter(20);
			phonePara.setSpacingAfter(20);
			//Add these above element to document
			document.add(title);
			document.add(phonePara);
			document.add(table);
			document.add(amountPara);
			document.add(totalPara);
			//show success message
			showSuccess("Created bill successfully");
			document.close();
			outputStream.close();
			addReceipt(customer_id, Float.valueOf(total), new Timestamp(getCurrentDate()));
		} catch(Exception e) {
			System.out.println(e);
		}
		
		
	}
	public void addReceipt(int id, float total, Timestamp date) {
		String sql = "INSERT INTO receipt (customerid, total, date) VALUES(?,?,?)";
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.setFloat(2, total);
			ps.setTimestamp(3, date);
			ps.executeUpdate();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	public boolean checkEmptyBill() {
		if (selling_product == null	|| selling_phone.getText().isEmpty()) {
			return true;
		}
		return false;

	}
//	public void switchToSelling() {
//		Scene scene = selling_pay_btn.getScene();
//		scene.setUserData(selling_product);
//	}
	//the remove button of selling tab
	public void removeButton(MouseEvent e) {
		if (selling_product.getItems().isEmpty()) {
			showError("There is no product here");
		} else if (e.getClickCount() == 2) {
			System.out.println(e.getClickCount());
			//clear all items in list view
			selling_product.getItems().clear();
			selling_amount.setText("");
			selling_total.setText("");
			selling_phone.setText("");
		} else {
			//Get deleted index
			int index = selling_product.getSelectionModel().getSelectedIndex();
			if (index == -1) {
				return;
			}
			//Decrease current amount when user removes an item
			int deletedAmount = decreaseAmount();
			decreasePrice(deletedAmount);
			//Remove selected item from list view
			selling_product.getItems().remove(index);

		}
	}
	public int decreaseAmount() {
		//Get selected item
		String productDetail = selling_product.getSelectionModel().getSelectedItem();
		//Find amount in productDetail and cut it and assign to variable
		int deletedIndex = productDetail.indexOf("x")+1;
		int deletedAmount = Integer.valueOf(productDetail.substring(deletedIndex));
		//Calculate the current amount again and assign it selling_amount
		int currentAmount = Integer.valueOf(selling_amount.getText());
		int returnedAmount = deletedAmount;
		currentAmount -= deletedAmount;
		selling_amount.setText(String.valueOf(currentAmount));
		return returnedAmount;
	}
	public void decreasePrice(int amount) {
		String productDetail = selling_product.getSelectionModel().getSelectedItem();
		int deletedAmount = amount;
		//cut price from product detail
		int priceBeginIndex = productDetail.lastIndexOf("|")+2;
		int priceEndIndex = productDetail.indexOf(" x");
		float price = Float.valueOf(productDetail.substring(priceBeginIndex, priceEndIndex));
		float deletedPrice = price * deletedAmount;
		//round two decimal 
		DecimalFormat df = new DecimalFormat("0.00");
		deletedPrice = Float.valueOf(df.format(deletedPrice));
		float totalPrice = Float.valueOf(selling_total.getText());
		totalPrice -= deletedPrice;
		DecimalFormat df_total = new DecimalFormat("0.00");
		totalPrice = Float.valueOf(df_total.format(totalPrice));
		selling_total.setText(String.valueOf(totalPrice));
	}
	//switch tab 
	public void switchTab(ActionEvent e) {
		if (e.getSource().equals(tab_dashboard)) {
			main_daskboard.setVisible(true);
			main_product.setVisible(false);
			main_selling.setVisible(false);
			main_statictics.setVisible(false);
			search_bar.setVisible(false);
			main_title.setText("Dashboard");
			setDashboard();
		}else if (e.getSource().equals(tab_product)) {
			main_daskboard.setVisible(false);
			main_product.setVisible(true);
			main_selling.setVisible(false);
			main_statictics.setVisible(false);
			search_bar.setVisible(true);
			search_input.setText("");
			showProduct();
			//set the main title is Product
			main_title.setText("Product");
		} else if (e.getSource().equals(tab_selling)) {
			main_daskboard.setVisible(false);
			main_product.setVisible(false);
			main_selling.setVisible(true);
			main_statictics.setVisible(false);
			search_bar.setVisible(true);
			search_input.setText("");
			//Set the main title is Selling
			main_title.setText("Selling");
			//send data to product controller for adding item into list view
			//I will send list view of MainAppController through scene because two of them are same scene
			Scene scene = selling_pay_btn.getScene();
			//Create a list consists of selling_product, selling_amount and selling_total
			List<Object> list = new ArrayList<>();
			list.add(selling_product);
			list.add(selling_amount);
			list.add(selling_total);
			scene.setUserData(list);
			//refresh product list when adding new product
			try {
				showProductCard();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(tab_statictics)) {
			main_title.setText("Statictics");
			main_daskboard.setVisible(false);
			main_statictics.setVisible(true);
			main_product.setVisible(false);
			main_selling.setVisible(false);
			search_bar.setVisible(false);
			showCustomer();
			stat_container.getChildren().removeIf(node -> !(node.equals(stat_customer_table)));
			showChart();
		}
	}
	public void showError(String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	public void showSuccess(String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
	public ButtonType showWarning(String content) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText(null);
		alert.setContentText(content);
		return alert.showAndWait().get();
	}
	public ListView<String> getListView() {
		return selling_product;
	}
	//Get list of customer
	public ObservableList<Customer> getCustomerList() {
		ObservableList<Customer> list = FXCollections.observableArrayList();
		try {
			Connection con = Database.connectDB();
			String sql = "SELECT * FROM customer ORDER BY purchasetimes DESC";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int customerId = rs.getInt(1);
				int customerPhone = rs.getInt(2);
				int customerTimes = rs.getInt(3);
				Date date = rs.getTimestamp(4);
				Customer customer = new Customer(customerId, customerPhone, customerTimes, date);
				list.add(customer);
			}
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		return list;
	}
	public void showCustomer() {
		customerList = getCustomerList();
		stat_customer_id.setCellValueFactory(new PropertyValueFactory<>("id"));
		stat_customer_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		stat_customer_times.setCellValueFactory(new PropertyValueFactory<>("times"));
		stat_customer_date.setCellValueFactory(new PropertyValueFactory<>("date"));
		stat_customer_table.setItems(customerList);
	}
	public void showChart() {
		//Creating piechart 
		PieChart pieChart = new PieChart();
		pieChart.setClockwise(true);
		pieChart.setLabelsVisible(true);
		pieChart.setStartAngle(180);
		pieChart.getData().addAll(getProductSold());
		pieChart.setPrefWidth(852);
		//creating bar chart
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Product");
		xAxis.setTickLabelFill(Color.WHITE);
		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Quantity sold");
		yAxis.setTickLabelFill(Color.WHITE);
		BarChart barChart = new BarChart(xAxis,yAxis);
		XYChart.Series data = new XYChart.Series();
		data.setName("Products Sold");
		getProductSold().forEach(i->{
			data.getData().add(new XYChart.Data(i.getName(),i.getPieValue()));
		});
		barChart.getData().add(data);
		HBox group = new HBox();
		group.getStyleClass().add("product-adding");
		BorderPane pane = new BorderPane();
		pane.getStyleClass().add("product-adding");
		group.setMaxWidth(852);
		pane.setMaxWidth(852);
		group.setAlignment(Pos.CENTER);
		group.getChildren().add(pieChart);
		pane.setCenter(barChart);
		stat_container.getChildren().addAll(pane,group);
		
	}
	public List<PieChart.Data> getProductSold() {
		List<PieChart.Data> list = new ArrayList<>();
//		String sql = "SELECT productname, COUNT(*) FROM `orders` GROUP BY productname";
		String sql = "SELECT product.productname, COUNT(*) FROM `orders` JOIN `product` ON orders.productid = product.productid GROUP BY productname";
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				String name = rs.getString(1);
				int sold = rs.getInt(2);
				PieChart.Data slice = new PieChart.Data(name, sold);
				list.add(slice);
			}
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
		return list;
	}
	public void setDashboard() {
		int sold = getSoldQuantity();
		int quantity = getQuantity();
		float revenue = getRevenue();
		float dailyRevenue = getDailyRevenue();
		dash_sold.setText(String.valueOf(sold));
		dash_quantity.setText(String.valueOf(quantity));
		dash_revenue.setText(String.valueOf(revenue));
		dash_daily_revenue.setText(String.valueOf(dailyRevenue));
		
	}
	public float getRevenue() {
		float money = 0.0f;
		//Using order to calculate total price
//		String sql = "SELECT COUNT(total) FROM receipt";
		String sql = "SELECT ROUND(SUM(totalprice),2) FROM orders";
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) money = rs.getFloat(1);
			con.close();
		} catch(Exception e) {
			System.out.println("getReveneue"+e);
		}
		return money;
	}
	public float getDailyRevenue() {
		String sql = "SELECT SUM(total) FROM receipt WHERE date LIKE '"+java.time.LocalDate.now().toString()+"%'";
		float money = 0.0f;
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				money = rs.getFloat(1);
			}
			con.close();
		} catch(Exception e) {
			System.out.println("getDailyRevenue"+e);
		}
		return money;
	}
	public int getQuantity() {
		String sql = "SELECT SUM(quantity) FROM product";
		int quantity = 0;
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) quantity = rs.getInt(1);
			con.close();
		} catch(Exception e) {
			System.out.println("getQuantity"+e);
		}
		return quantity;
	}
	public int getSoldQuantity() {
		String sql = "SELECT SUM(amount) FROM orders";
		int sold = 0;
		try {
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) sold = rs.getInt(1);
		} catch(Exception e) {
			System.out.println("getSoldQuantity"+e);
		}
		return sold;
	}
	public void searchButton() throws IOException {
		String searchText = search_input.getText();
		if (searchText.isEmpty()) {
			showProduct();
			showProductCard();
		} else {
			showProduct(getProductList(searchText));
			showProductCard(getProductList(searchText));
		}
	}
}
