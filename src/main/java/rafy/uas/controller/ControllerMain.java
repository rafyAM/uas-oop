package rafy.uas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerMain {

    @FXML
    private void handleDataPerjalanan() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/rafy/uas/ViewDataJourney.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBooking() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/rafy/uas/ViewPesanTiket.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTicketManagement() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/rafy/uas/ViewPengelolaTrip.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
