package rafy.uas.controller;

import rafy.uas.model.PesanTiket;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ControllerCetakTrip {

    @FXML
    private TextArea ticketTextArea;

    private PesanTiket booking;

    public void setBooking(PesanTiket booking) {
        this.booking = booking;
        displayTicketDetails();
    }

    private void displayTicketDetails() {
        if (booking != null) {
            String ticketDetails = String.format(
                    "Customer Name: %s\nTravel Route: %s to %s\nSchedule: %s\nTicket Count: %d\nTotal Cost: %.2f",
                    booking.getCustomerName(), booking.getTravelOrigin(), booking.getTravelDestination(),
                    booking.getTravelSchedule(), booking.getTicketCount(), booking.getTotalCost());
            ticketTextArea.setText(ticketDetails);
        }
    }
}
