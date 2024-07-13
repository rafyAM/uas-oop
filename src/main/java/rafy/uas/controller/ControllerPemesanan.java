package rafy.uas.controller;

import rafy.uas.model.PesanTiket;
import rafy.uas.model.PesanTiketDAO;
import rafy.uas.model.Journey;
import rafy.uas.model.JourneyDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ControllerPemesanan {

    @FXML
    private TableView<PesanTiket> bookingTable;
    @FXML
    private TableColumn<PesanTiket, Integer> idColumn;
    @FXML
    private TableColumn<PesanTiket, String> customerNameColumn;
    @FXML
    private TableColumn<PesanTiket, String> travelOriginColumn;
    @FXML
    private TableColumn<PesanTiket, String> travelDestinationColumn;
    @FXML
    private TableColumn<PesanTiket, String> travelScheduleColumn;
    @FXML
    private TableColumn<PesanTiket, Integer> ticketCountColumn;
    @FXML
    private TableColumn<PesanTiket, Double> totalCostColumn;

    @FXML
    private TextField customerNameField;
    @FXML
    private TextField ticketCountField;
    @FXML
    private ChoiceBox<Journey> travelChoiceBox;

    private PesanTiketDAO bookingDAO;
    private JourneyDAO travelDAO;
    private ObservableList<PesanTiket> bookingData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        bookingDAO = new PesanTiketDAO();
        travelDAO = new JourneyDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        travelOriginColumn.setCellValueFactory(new PropertyValueFactory<>("travelOrigin"));
        travelDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("travelDestination"));
        travelScheduleColumn.setCellValueFactory(new PropertyValueFactory<>("travelSchedule"));
        ticketCountColumn.setCellValueFactory(new PropertyValueFactory<>("ticketCount"));
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));

        loadBookingData();
        loadTravelChoices();
    }

    private void loadBookingData() {
        bookingData.setAll(bookingDAO.getAllBookings());
        bookingTable.setItems(bookingData);
    }

    private void loadTravelChoices() {
        List<Journey> travels = travelDAO.getAllTravels();
        travelChoiceBox.getItems().setAll(travels);
        travelChoiceBox.setConverter(new javafx.util.StringConverter<Journey>() {
            @Override
            public String toString(Journey travel) {
                if (travel == null) {
                    return "";
                }
                return travel.getOrigin() + " to " + travel.getDestination() + " at " + travel.getSchedule();
            }

            @Override
            public Journey fromString(String string) {
                return travelChoiceBox.getItems().stream()
                        .filter(ap -> (ap.getOrigin() + " to " + ap.getDestination() + " at " + ap.getSchedule())
                                .equals(string))
                        .findFirst().orElse(null);
            }
        });
    }

    @FXML
    private void addBooking() {
        String customerName = customerNameField.getText();
        Journey selectedTravel = travelChoiceBox.getValue();
        if (selectedTravel == null) {
            showAlert("No Travel Selected", "Please select a travel route.");
            return;
        }
        int ticketCount;
        try {
            ticketCount = Integer.parseInt(ticketCountField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Ticket Count", "Please enter a valid number for ticket count.");
            return;
        }

        if (ticketCount > selectedTravel.getJumlahTiket()) {
            showAlert("Insufficient Tickets", "Not enough tickets available.");
            return;
        }

        double totalCost = ticketCount * selectedTravel.getPrice();

        PesanTiket booking = new PesanTiket(0, customerName, selectedTravel.getId(), selectedTravel.getOrigin(),
                selectedTravel.getDestination(), selectedTravel.getSchedule(), ticketCount, totalCost);
        bookingDAO.addBooking(booking);
        loadBookingData();
    }

    @FXML
    private void printTicket() {
        PesanTiket selectedBooking = bookingTable.getSelectionModel().getSelectedItem();
        if (selectedBooking == null) {
            showAlert("No Booking Selected", "Please select a booking to print the ticket.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rafy/uas/ViewCetakTrip.fxml"));
            Scene scene = new Scene(loader.load());

            ControllerCetakTrip controller = loader.getController();
            controller.setBooking(selectedBooking);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Print Ticket");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
