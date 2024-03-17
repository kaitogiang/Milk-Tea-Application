package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("login-register-scene.fxml"));
			String css = this.getClass().getResource("application.css").toExternalForm();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(css);
			stage.setResizable(false);
//			Image icon = new Image("\\images\\bubble-tea-tab.png");
			Image icon = new Image("/images/bubble-tea-tab.png");
			stage.getIcons().add(icon);
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
