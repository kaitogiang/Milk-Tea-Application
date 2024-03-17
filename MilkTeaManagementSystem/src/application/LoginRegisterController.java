package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginRegisterController implements Initializable {

	@FXML
	private Button btnFromForgot;
	@FXML
	private Button btnFromRegister;
	@FXML
	private AnchorPane forgot_pass_form;

	@FXML
	private AnchorPane login_form;

	@FXML
	private AnchorPane register_form;

	@FXML
	private AnchorPane reset_pass_form;

	@FXML
	private TextField reg_answer;

	@FXML
	private PasswordField reg_password;

	@FXML
	private ComboBox<String> reg_question;

	@FXML
	private PasswordField reg_retypepassword;

	@FXML
	private TextField reg_username;

	@FXML
	private Button signUpBtn;

	@FXML
	private TextField reg_showpassword;

	@FXML
	private TextField reg_showretypepassword;

	@FXML
	private Button hideBtn;

	@FXML
	private Button showBtn;

	@FXML
	private Button log_hideBtn;

	@FXML
	private Button log_showBtn;

	@FXML
	private PasswordField log_password;

	@FXML
	private TextField log_showPassword;

	@FXML
	private TextField log_username;

	@FXML
    private TextField for_answer;

    @FXML
    private ComboBox<String> for_question;

    @FXML
    private TextField for_username;
    
    @FXML
    private PasswordField res_password;

    @FXML
    private PasswordField res_retypepassword;

    @FXML
    private TextField res_showpassword;

    @FXML
    private TextField res_showretypepassword;

    @FXML
    private Button res_hideBtn;

    @FXML
    private Button res_showBtn;
    
    @FXML
    private Button log_loginBtn;
    
  
	String[] questionList = { "What is your birthday?", "What is your favorite movie?",
			"What is your favorite number" };

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		addDataQuestion();
		//two way bind between text field and password field, when text field changes then the password
		//also changes.
		log_showPassword.textProperty().bindBidirectional(log_password.textProperty());
		reg_showpassword.textProperty().bindBidirectional(reg_password.textProperty());
		reg_showretypepassword.textProperty().bindBidirectional(reg_retypepassword.textProperty());
	}
	
	
	public void addDataQuestion() {
		reg_question.getItems().addAll(questionList);
		reg_question.getSelectionModel().selectedItemProperty().addListener(e -> {
			System.out.println(reg_question.getSelectionModel().getSelectedItem());
		});
		for_question.getItems().addAll(questionList);
		
	}

	public void registerButton(ActionEvent e) throws SQLException {
		Connection con = Database.connectDB();
		Pattern p = Pattern.compile("[a-zA-Z]+\\d*");
		Matcher m = p.matcher(reg_username.getText());
		if (reg_username.getText().isEmpty() || reg_password.getText().isEmpty()
				|| reg_retypepassword.getText().isEmpty() || reg_answer.getText().isEmpty()) {
			showError("Please fill all blank fields");
		} else if (!m.matches()) {
			showError("Username must start with letter and cannot contain special character");
		} else if (checkUsername()) {
			showError("The account already exists");
		} else if (!passwordSimilarityAuthentication()) {
			showError("Password is not match");
		} else if (passwordLenghtAuthentication()) {
			showError("Password must be at least 8 character");
		} else {
			// Insert data into employee table in database
			String sql = "INSERT INTO employee VALUES(?,?,?,?,?)";
			String username = reg_username.getText();
			String password = reg_password.getText();
			String question = reg_question.getSelectionModel().getSelectedItem();
			String answer = reg_answer.getText();
			System.out.println(answer);
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, question);
			stmt.setString(4, answer);
			java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
			stmt.setTimestamp(5, date);
			int i = stmt.executeUpdate();
			System.out.println(i + "records inserted");
			con.close();
			showSuccess("Created Successfully");
			// Switch to loginForm
			RotateTransition rotate = new RotateTransition();
			rotate.setNode(register_form);
			rotate.setDuration(Duration.millis(1000));
			rotate.setAxis(Rotate.Y_AXIS);
			;
			rotate.setByAngle(360);
			rotate.setOnFinished(t -> {
				register_form.setVisible(false);
				login_form.setVisible(true);
			});
			rotate.play();
		}

	}

	public boolean passwordLenghtAuthentication() {
		String password = reg_password.getText();
		if (password.length() < 8)
			return true;
		else
			return false;
	}

	public boolean passwordSimilarityAuthentication() {
		String password = reg_password.getText();
		String retypepassword = reg_retypepassword.getText();
		return password.equals(retypepassword);
	}

	public boolean checkUsername() throws SQLException {
		String username = reg_username.getText();
		String sql = "SELECT username FROM employee WHERE username=?";
		Connection con = Database.connectDB();
		PreparedStatement pt = con.prepareStatement(sql);
		pt.setString(1, username);
		ResultSet rs = pt.executeQuery();
		System.out.println(rs);
		boolean test = rs.next();
		con.close();
		return test;
	}
	public void loginButton(ActionEvent e) throws SQLException {
		if (checkEmptyLoginField()) {
			showError("Please fill all blank fields");
		} else if (!checkExistAccount()) {
			showError("The account does not exist");
		} else {
			showSuccess("Login Successfully");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("main-scene.fxml"));
			System.out.println("URL la: "+getClass().getResource("main-scene.fxml"));
			String employeeId = log_username.getText();
			try {
				AnchorPane mainPane = loader.load();
				log_loginBtn.getScene().getWindow().hide();
				MainAppController controller = loader.getController();
				controller.setUserName(employeeId);
				Scene scene = new Scene(mainPane);
				Stage stage = (Stage) log_loginBtn.getScene().getWindow();
				//Change Icon for MainScane
//				Image icon = new Image("\\images\\bubble-tea-tab.png");
				Image icon = new Image("/images/bubble-tea-tab.png");
				stage.getIcons().add(icon);
				stage.setScene(scene);
				stage.setResizable(false);
				stage.show();
			} catch (IOException e1) {
				System.out.println("Loi.......");
				e1.printStackTrace();
//				System.out.println(e1);
			}
			
		}
		
		//test
		
	}

	public boolean checkExistAccount() throws SQLException {
		String username = log_username.getText();
		String password = log_password.getText();
		String sql = "SELECT * FROM employee WHERE username=? AND password=?";
		Connection con = Database.connectDB();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, username);
		ps.setString(2, password);
		ResultSet rs = ps.executeQuery();
		boolean check = rs.next();
		con.close();
		return check;
	}

	public boolean checkEmptyLoginField() {
		String username = log_username.getText();
		String password = log_password.getText();
		return username.isEmpty() || password.isEmpty();
	}
	public void proceedButton() throws SQLException {
		//Get values from user
		String username = for_username.getText();
		String question = for_question.getSelectionModel().getSelectedItem();
		String answer = for_answer.getText();
		String sql = "SELECT * FROM employee WHERE username=? AND question=? AND answer=?";
		Connection con = Database.connectDB();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, username);
		ps.setString(2, question);
		ps.setString(3, answer);
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			RotateTransition rotate = new RotateTransition();
			rotate.setNode(forgot_pass_form);
			rotate.setDuration(Duration.millis(1000));
			rotate.setAxis(Rotate.Y_AXIS);
			rotate.setByAngle(360);
			rotate.setOnFinished(t -> {
				forgot_pass_form.setVisible(false);
				reset_pass_form.setVisible(true);
			});
			rotate.play();
		} else {
			showError("Wrong! Something is not true");
		}
		
	}
	public void changePassword() throws SQLException {
		if (!checkChangePasswordField()) {
			showError("Password is not match");
		} else {
			java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
			String sql = "UPDATE employee SET password=?, date=? WHERE username=?";
			Connection con = Database.connectDB();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, res_password.getText());
			ps.setTimestamp(2, date);
			ps.setString(3, for_username.getText());
			int i = ps.executeUpdate();
			System.out.println(i+" record update ");
			showSuccess("Reset Password Successfully");
			//Switch to login form
			RotateTransition rotate = new RotateTransition();
			rotate.setNode(reset_pass_form);
			rotate.setDuration(Duration.millis(1000));
			rotate.setAxis(Rotate.Y_AXIS);
			;
			rotate.setByAngle(360);
			rotate.setOnFinished(t -> {
				reset_pass_form.setVisible(false);
				login_form.setVisible(true);
			});
			rotate.play();
		}
	}
	public boolean checkChangePasswordField() {
		String password = res_password.getText();
		String retypepassword = res_retypepassword.getText();
		return password.equals(retypepassword);
	}
	public void showError(String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public void showSuccess(String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(content);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}

	// Show hide password
	public void showHidePassword(ActionEvent e) {
		// Set password and show password fields
		if (e.getSource().equals(log_showBtn)) {
			log_showPassword.setText(log_password.getText());
		} else if (e.getSource().equals(log_hideBtn)) {
			log_password.setText(log_showPassword.getText());
		}

		if (e.getSource().equals(showBtn)) {
			// Set value to show field
			String password = reg_password.getText();
			String retypepassword = reg_retypepassword.getText();
			reg_showpassword.setText(password);
			reg_showretypepassword.setText(retypepassword);

			// display button show hide
			showBtn.setVisible(false);
			hideBtn.setVisible(true);
			// show password
			reg_showpassword.setVisible(true);
			reg_showretypepassword.setVisible(true);
			// hide password field
			reg_password.setVisible(false);
			reg_retypepassword.setVisible(false);

		} else if (e.getSource().equals(hideBtn)) {
			// Set value to hide field
			String password = reg_showpassword.getText();
			String retypepassword = reg_showretypepassword.getText();
			reg_password.setText(password);
			reg_retypepassword.setText(retypepassword);
			// Display button show hide
			showBtn.setVisible(true);
			hideBtn.setVisible(false);
			// show password
			reg_showpassword.setVisible(false);
			reg_showretypepassword.setVisible(false);
			// hide password field
			reg_password.setVisible(true);
			reg_retypepassword.setVisible(true);

		} else if (e.getSource().equals(log_showBtn)) {
			log_showBtn.setVisible(false);
			log_hideBtn.setVisible(true);
			log_password.setVisible(false);
			log_showPassword.setVisible(true);
		} else if (e.getSource().equals(log_hideBtn)) {
			log_showBtn.setVisible(true);
			log_hideBtn.setVisible(false);
			log_password.setVisible(true);
			log_showPassword.setVisible(false);
		} else if (e.getSource().equals(res_showBtn)) {
			//Set values to fields
			res_showpassword.setText(res_password.getText());
			res_showretypepassword.setText(res_retypepassword.getText());
			//display button
			res_hideBtn.setVisible(true);
			res_showBtn.setVisible(false);
			//display fields
			res_password.setVisible(false);
			res_retypepassword.setVisible(false);
			res_showpassword.setVisible(true);
			res_showretypepassword.setVisible(true);
		} else if (e.getSource().equals(res_hideBtn)) {
			res_password.setText(res_showpassword.getText());
			res_retypepassword.setText(res_showretypepassword.getText());
			//display button
			res_hideBtn.setVisible(false);
			res_showBtn.setVisible(true);
			//display fields
			res_password.setVisible(true);
			res_retypepassword.setVisible(true);
			res_showpassword.setVisible(false);
			res_showretypepassword.setVisible(false);
		}
	}

	public void clearRegisterForm() {
		reg_username.setText("");
		reg_password.setText("");
		reg_retypepassword.setText("");
		reg_question.getSelectionModel().clearSelection();
		reg_answer.setText("");
	}
	public void switchToRegister(ActionEvent e) {
		RotateTransition rotate = new RotateTransition();
		rotate.setNode(login_form);
		rotate.setDuration(Duration.millis(1000));
		rotate.setAxis(Rotate.Y_AXIS);
		rotate.setByAngle(360);
		rotate.setOnFinished(t -> {
			login_form.setVisible(false);
			register_form.setVisible(true);
		});
		rotate.play();
		System.out.println("switch to Register");
		//reset all filed
		reg_username.setText("");
		reg_password.setText("");
		reg_showpassword.setText("");
		reg_question.getSelectionModel().clearSelection();
		reg_answer.setText("");
		reg_retypepassword.setText("");
		reg_showretypepassword.setText("");
		showBtn.setVisible(true);
		hideBtn.setVisible(false);
		if (e.getSource().equals(btnFromRegister)) {

		}
	}

	public void backToLogin(ActionEvent e) {
		RotateTransition rotate = new RotateTransition();
		if (e.getSource().equals(btnFromRegister)) {
			rotate.setNode(register_form);
			rotate.setDuration(Duration.millis(1000));
			rotate.setAxis(Rotate.Y_AXIS);
			;
			rotate.setByAngle(360);
			rotate.setOnFinished(t -> {
				register_form.setVisible(false);
				login_form.setVisible(true);
			});
			rotate.play();
		} else {
			rotate.setNode(forgot_pass_form);
			rotate.setDuration(Duration.millis(1000));
			rotate.setAxis(Rotate.Y_AXIS);
			rotate.setByAngle(360);
			rotate.setOnFinished(t -> {
				forgot_pass_form.setVisible(false);
				login_form.setVisible(true);
			});
			rotate.play();
		}
		System.out.println("switch to login");

	}

	public void switchToForgotPass(ActionEvent e) {
		RotateTransition rotate = new RotateTransition();
		rotate.setNode(login_form);
		rotate.setDuration(Duration.millis(1000));
		rotate.setAxis(Rotate.Y_AXIS);
		;
		rotate.setByAngle(360);
		rotate.setOnFinished(t -> {
			login_form.setVisible(false);
			forgot_pass_form.setVisible(true);
		});
		rotate.play();
		System.out.println("switch to forgot password");
	}

	public void switchToResetPass(ActionEvent e) {
		RotateTransition rotate = new RotateTransition();
		rotate.setNode(forgot_pass_form);
		rotate.setDuration(Duration.millis(1000));
		rotate.setAxis(Rotate.Y_AXIS);
		;
		rotate.setByAngle(360);
		rotate.setOnFinished(t -> {
			forgot_pass_form.setVisible(false);
			reset_pass_form.setVisible(true);
		});
		rotate.play();
		System.out.println("switch to reset password");
	}

	public void backToForgotPass(ActionEvent e) {
		RotateTransition rotate = new RotateTransition();
		rotate.setNode(reset_pass_form);
		rotate.setDuration(Duration.millis(1000));
		rotate.setAxis(Rotate.Y_AXIS);
		;
		rotate.setByAngle(360);
		rotate.setOnFinished(t -> {
			reset_pass_form.setVisible(false);
			forgot_pass_form.setVisible(true);
		});
		rotate.play();
		System.out.println("switch to forgot password");
	}
}
