package rafy.uas.controller;

import rafy.uas.model.Journey;
import rafy.uas.model.JourneyDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ControllerPengelolaanTrip {

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

    @FXML
    public void initialize() {
        travelDAO = new JourneyDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        originColumn.setCellValueFactory(new PropertyValueFactory<>("origin"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        scheduleColumn.setCellValueFactory(new PropertyValueFactory<>("schedule"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        jumlahTiketColumn.setCellValueFactory(new PropertyValueFactory<>("jumlahTiket"));

        loadTravelData();
        checkLowTickets();
    }

    private void loadTravelData() {
        List<Journey> travels = travelDAO.getAllTravels();
        travelData.setAll(travels);
        travelTable.setItems(travelData);
    }

    private void checkLowTickets() {
        List<Journey> lowTicketTravels = travelDAO.getTravelsWithLowTickets(3);
        if (!lowTicketTravels.isEmpty()) {
            StringBuilder message = new StringBuilder("The following routes have less than 3 tickets left:\n");
            for (Journey travel : lowTicketTravels) {
                message.append(travel.getOrigin()).append(" to ").append(travel.getDestination())
                        .append(" at ").append(travel.getSchedule()).append(" - ")
                        .append(travel.getJumlahTiket()).append(" tickets left\n");
            }
            showAlert("Low Tickets", message.toString());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
