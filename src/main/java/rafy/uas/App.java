package rafy.uas;

import rafy.uas.model.Databases;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Font.loadFont(getClass().getResourceAsStream("/fonts/Archivo"), 18);

        Databases.getConnection();
        Databases.createTable();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/rafy/uas/Primary.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tiket Travel");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}