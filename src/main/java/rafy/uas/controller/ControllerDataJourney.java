package rafy.uas.controller;

import rafy.uas.model.Databases;
import rafy.uas.model.Journey;
import rafy.uas.model.JourneyDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ControllerDataJourney {
    @FXML
    private TextField originField, destinationField, scheduleField, priceField, jumlahTiketField;
    @FXML
    private TextField searchOriginField, searchDestinationField;
    @FXML
    private TableView<Journey> travelTable;
    @FXML
    private TableColumn<Journey, Integer> idColumn;
    @FXML
    private TableColumn<Journey, String> originColumn;
    @FXML
    private TableColumn<Journey, String> destinationColumn;
    @FXML
    private TableColumn<Journey, String> scheduleColumn;
    @FXML
    private TableColumn<Journey, Double> priceColumn;
    @FXML
    private TableColumn<Journey, Integer> jumlahTiketColumn;

    private JourneyDAO travelDAO;
    private ObservableList<Journey> travelData = FXCollections.observableArrayList();

    public void initialize() {
        travelDAO = new JourneyDAO();
        Databases.createTable();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        originColumn.setCellValueFactory(new PropertyValueFactory<>("origin"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        jumlahTiketColumn.setCellValueFactory(new PropertyValueFactory<>("jumlahTiket"));
        loadTravelData();

        travelTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showTravelDetails(newValue));
    }

    private void showTravelDetails(Journey travel) {
        if (travel != null) {
            originField.setText(travel.getOrigin());
            destinationField.setText(travel.getDestination());
            scheduleField.setText(travel.getSchedule());
            priceField.setText(Double.toString(travel.getPrice()));
            jumlahTiketField.setText(Integer.toString(travel.getJumlahTiket()));
        } else {
            originField.setText("");
            destinationField.setText("");
            scheduleField.setText("");
            priceField.setText("");
            jumlahTiketField.setText("");
        }
    }

    @FXML
    private void addTravel() {
        String origin = originField.getText();
        String destination = destinationField.getText();
        String schedule = scheduleField.getText();
        double price = Double.parseDouble(priceField.getText());
        int jumlahTiket = Integer.parseInt(jumlahTiketField.getText());

        Journey travel = new Journey(0, origin, destination, schedule, price, jumlahTiket);
        travelDAO.addTravel(travel);
        loadTravelData();
    }

    @FXML
    private void editTravel() {
        Journey selectedTravel = travelTable.getSelectionModel().getSelectedItem();
        if (selectedTravel != null) {
            boolean saveClicked = showEditTravelDialog(selectedTravel);
            if (saveClicked) {
                travelDAO.updateTravel(selectedTravel); // Update database with new travel data
                updateTravelData(selectedTravel);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Travel Selected");
            alert.setContentText("Please select a travel in the table.");
            alert.showAndWait();
        }
    }

    private boolean showEditTravelDialog(Journey travel) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/rafy/uas/EditDataJourney.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Travel");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ControllerEditDataJourney controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTravel(travel);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void deleteTravel() {
        Journey selectedTravel = travelTable.getSelectionModel().getSelectedItem();
        if (selectedTravel != null) {
            travelDAO.deleteTravel(selectedTravel.getId());
            loadTravelData();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Travel Selected");
            alert.setContentText("Please select a travel in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void searchTravel() {
        String origin = searchOriginField.getText();
        String destination = searchDestinationField.getText();
        List<Journey> travels = travelDAO.searchTravels(origin, destination);
        travelData.setAll(travels);
        travelTable.setItems(travelData);
    }

    private void loadTravelData() {
        List<Journey> travels = travelDAO.getAllTravels();
        travelData.setAll(travels);
        travelTable.setItems(travelData);
    }

    private void updateTravelData(Journey travel) {
        travelTable.refresh();
    }
}
