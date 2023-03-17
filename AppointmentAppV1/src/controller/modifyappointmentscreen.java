package controller;

import helper.DataAccess;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Modify Appointment Screen Controller Class
 * @author Justonna Naing
 **/
public class modifyappointmentscreen implements Initializable {
    public TextField idField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public ComboBox contactCombo;
    public TextField typeField;
    public DatePicker startDateField;
    public ComboBox startTimeCombo;
    public DatePicker endDateField;
    public ComboBox endTimeCombo;
    public ComboBox customerIDCombo;
    public ComboBox userIDCombo;
    public TextField hiddenID;
    private Appointment selectedAppointment;

    /**
     * Initialize Modify Appointment Screen.
     * Populate the text fields with selected Appointment Data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am in Modify Appointment Screen!");

        selectedAppointment = appointmentscreen.appointmentModify();

        /**
         * Disable Past Dates and Weekends for StartDateField Combo
         */
        startDateField.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0 || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });

        /**
         * Disable Past Dates and Weekends for endDateField Combo
         */
        endDateField.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) < 0 || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });

        /**
         * Populating time data to the start time and end time combo.
         */
        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();

        /*
         Before
        LocalTime startTime = LocalTime.of(8,00);
        LocalTime endTime = LocalTime.of(22, 00);
        */

        LocalTime startTime = LocalTime.of(00,00);
        LocalTime endTime = LocalTime.of(23,45);

        /**
         * Adding 15minutes from 8am until 10pm.
         */
        appointmentTimes.add((String.valueOf(startTime)));
        while(startTime.isBefore(endTime)){

            startTime = startTime.plusMinutes(15);
            appointmentTimes.add((String.valueOf(startTime)));
        }

        /**
         * Populate time data into the combo boxes.
         */
        startTimeCombo.setItems(appointmentTimes);
        endTimeCombo.setItems(appointmentTimes);

        ObservableList<Customer> customerList = DataAccess.getAllCustomers();
        ObservableList<Integer> customerID = FXCollections.observableArrayList();

        /**
         * Iterating all the customers and get CustomerID.
         * Populate CustomerID Combo box with Customer ID data.
         */
        customerList.forEach(Customer -> customerID.add(Customer.getId()));
        Collections.sort(customerID); //sorting the list before setting the items to the combo box.
        customerIDCombo.setItems(customerID);

        ObservableList<Contact> contactList = DataAccess.getAllContacts();
        ObservableList<String> contactID = FXCollections.observableArrayList();

        /**
         * Populate Contact Combo box with Contact Name data.
         */
        contactList.forEach(Contact -> contactID.add(Contact.getName()));
        Collections.sort(contactID); //sorting the list before setting the items to the combo box.
        contactCombo.setItems(contactID);

        ObservableList<User> userList = DataAccess.getAllUsers();
        ObservableList<Integer> userID = FXCollections.observableArrayList();

        /**
         * Populate UserID Combo box with User ID data.
         */
        userList.forEach(User -> userID.add(User.getId()));
        Collections.sort(userID); //sorting the list before setting the items to the combo box.
        userIDCombo.setItems(userID);

        idField.setText(String.valueOf(selectedAppointment.getId()));
        titleField.setText(String.valueOf(selectedAppointment.getTitle()));
        descriptionField.setText(String.valueOf(selectedAppointment.getDescription()));
        locationField.setText(String.valueOf(selectedAppointment.getLocation()));
        contactCombo.setValue(String.valueOf((contactID.get(0))));
        typeField.setText(String.valueOf(selectedAppointment.getType()));
        startDateField.setValue(selectedAppointment.getStart().toLocalDate());
        startTimeCombo.setValue(String.valueOf(selectedAppointment.getStart().toLocalTime()));
        endDateField.setValue(selectedAppointment.getEnd().toLocalDate());
        endTimeCombo.setValue(String.valueOf(selectedAppointment.getEnd().toLocalTime()));
        customerIDCombo.setValue(String.valueOf(selectedAppointment.getCustomerID()));
        userIDCombo.setValue(String.valueOf(selectedAppointment.getUserID()));
        hiddenID.setText(String.valueOf(selectedAppointment.getContact()));

    }

    /**
     * Load Main Screen.
     * @param actionEvent on Click Cancel Button
     * @throws IOException
     */
    public void onClickCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/appointmentscreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Save modified appointment data to the database.
     * @param actionEvent on Click Save Button
     */
    public void onClickSave(ActionEvent actionEvent) {

        ObservableList<Appointment> appointmentList = DataAccess.getAllAppointments();

        try {
            int appointmentID = Integer.parseInt(idField.getText());
            String title = titleField.getText().toString();
            String description = descriptionField.getText().toString();
            String location = locationField.getText().toString();
            String contact = contactCombo.getValue().toString();
            String type = typeField.getText().toString();
            LocalDate startDate = startDateField.getValue();
            LocalTime startTime = LocalTime.parse(startTimeCombo.getValue().toString(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalDate endDate = endDateField.getValue();
            LocalTime endTime = LocalTime.parse(endTimeCombo.getValue().toString(), DateTimeFormatter.ofPattern("HH:mm"));
            int customerID = Integer.parseInt(customerIDCombo.getValue().toString());
            int userID = Integer.parseInt(userIDCombo.getValue().toString());

            /**
             * creating LocalDateTime.
             */
            LocalDateTime l_startDateTime = LocalDateTime.of(startDate, startTime);
            LocalDateTime l_endDateTime = LocalDateTime.of(endDate, endTime);

            /**
             * Converting to ZoneDatTime of systemDefault.
             */
            ZonedDateTime z_startDateTime = ZonedDateTime.of(l_startDateTime, ZoneId.systemDefault());
            ZonedDateTime z_endDateTime = ZonedDateTime.of(l_endDateTime, ZoneId.systemDefault());

            /**
             * Change System Default Time to EST (New York) time.
             */
            ZonedDateTime z_startDateTimeEST = z_startDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));
            ZonedDateTime z_endDateTimeEST = z_endDateTime.withZoneSameInstant(ZoneId.of("America/New_York"));

            //ZonedDateTime z_startDateTimeUTC = z_startDateTime.withZoneSameInstant(ZoneId.of("UTC"));
            //ZonedDateTime z_endDateTimeUTC = z_endDateTime.withZoneSameInstant(ZoneId.of("UTC"));

            boolean isValid = false;
            boolean passed = false;

            /**
             * Empty fields check and show alert if some or all fields are blank.
             */
            if(title.isBlank() || description.isBlank() || location.isBlank() ||
                    contact == null || type.isBlank())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setHeaderText("Error updating appointment!");
                alert.setContentText("Form contains blank fields");
                alert.showAndWait();
                return;
            }

            /**
             * Enforcing dates to be the same.
             */
            else if (!startDate.isEqual(endDate)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");alert.setHeaderText("Error adding appointment!");
                alert.setContentText("Start Date and End Date must be same!");
                alert.showAndWait();
                return;
            }

            /**
             * Check to see the start time is after the End Time.
             */
            else if(l_startDateTime.isAfter(l_endDateTime))
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");alert.setHeaderText("Error updating appointment!");
                alert.setContentText("Start Time cannot be after End Time!");
                alert.showAndWait();
                return;
            }

            /**
             * Check to see the start time and end time are the same.
             */
            else if(l_startDateTime.isEqual(l_endDateTime) || l_endDateTime.isEqual(l_startDateTime))
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");alert.setHeaderText("Error updating appointment!");
                alert.setContentText("Start Time and End Time cannot be same!");
                alert.showAndWait();
                return;
            }


            /**
             * Check to see if the time (converted to EST) is outside of Business Hours - 8am - 10pm EST.
             */
            else if(z_startDateTimeEST.toLocalTime().isBefore(LocalTime.of(8,00)) ||
                    z_startDateTimeEST.toLocalTime().isAfter(LocalTime.of(22,00)) ||
                    z_endDateTimeEST.toLocalTime().isBefore(LocalTime.of(8,00))||
                    z_endDateTimeEST.toLocalTime().isAfter(LocalTime.of(22,00)))
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");alert.setHeaderText("Error updating appointment!");
                alert.setContentText("Appointment Times are outside of the Business Hours 8:00 - 22:00 EST!");
                alert.showAndWait();
                return;
            }

            /**
             * Assinged boolean  true to the passed variable. if all the above checks are passed.
             */
            else {
                passed = true;
            }

            /**
             * Appointment Overlapping Check using 3 condition method.
             */
            for (Appointment appointment : DataAccess.getAllAppointments()) {

                if ((appointmentID != appointment.getId()) && (customerID == appointment.getCustomerID()) && ((l_startDateTime.isAfter(appointment.getStart()) || (l_startDateTime.isEqual(appointment.getStart()))) &&
                        (l_startDateTime.isBefore(appointment.getEnd()))))
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error updating appointment!");
                    alert.setContentText("Overlapping with existing appointments!");
                    alert.showAndWait();
                    return;
                }
                if ((appointmentID != appointment.getId()) && (customerID == appointment.getCustomerID()) && (l_endDateTime.isAfter(appointment.getStart()) &&
                        ((l_endDateTime.isBefore(appointment.getEnd()) || (l_endDateTime.isEqual(appointment.getEnd()))))))
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error updating appointment!");
                    alert.setContentText("Overlapping with existing appointments!");
                    alert.showAndWait();
                    return;
                }
                if ((appointmentID != appointment.getId()) && (customerID == appointment.getCustomerID()) && (((l_startDateTime.isBefore(appointment.getStart()) || l_startDateTime.isEqual(appointment.getStart()))) &&
                        ((l_endDateTime.isAfter(appointment.getEnd())) || l_endDateTime.isEqual(appointment.getEnd()))))
                {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error updating appointment!");
                    alert.setContentText("Overlapping with existing appointments!");
                    alert.showAndWait();
                    return;
                }

            }
            isValid = true;

            /**
             * If all the conditions are passed and there is no overlapped.
             * Insert new Appointment to the Database.
             */
            if(passed && isValid) {

                String sql = "UPDATE appointments SET Title = ?, Description = ?, " +
                        "Location = ?, Type = ?, Start = ?, End = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, " +
                        "Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

                ps.setString(1, title);
                ps.setString(2, description);
                ps.setString(3, location);
                ps.setString(4, type);
                ps.setTimestamp(5, Timestamp.valueOf((z_startDateTime).toLocalDateTime()));
                ps.setTimestamp(6, Timestamp.valueOf((z_endDateTime).toLocalDateTime()));
                //  ps.setTimestamp(6, Timestamp.valueOf((z_startDateTimeUTC).toLocalDateTime()));
                //  ps.setTimestamp(7, Timestamp.valueOf((z_endDateTimeUTC).toLocalDateTime()));
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(8, "test");
                ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(10, "test");
                ps.setInt(11, customerID);
                ps.setInt(12, userID);
                ps.setInt(13, Integer.parseInt(hiddenID.getText()));
                ps.setInt(14, appointmentID);

                ps.executeUpdate();

                /**
                 * Show message after an appointment was updated to the database.
                 */
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText("An Appointment was updated in the database!");
                alert.showAndWait();

                /**
                 * Go back to the Appointment Screen.
                 */
                Parent root = FXMLLoader.load(getClass().getResource("/view/appointmentscreen.fxml"));
                Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root,1000,400);
                stage.setTitle("Appointments Screen");
                stage.setScene(scene);
                stage.show();

            }

        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Error updating appointment!");
            alert.setContentText("Form contains blank fields");
            alert.showAndWait();
        }

    }

    /**
     * Get the ContactID for hiddedID Field. ID is required instead of Contact Name to insert appointment data properly to the database.
     * @param actionEvent on Click Contact Combo Box.
     */
    public void onClickContactCombo(ActionEvent actionEvent) {

        ObservableList<Integer> contactID = FXCollections.observableArrayList();

        try {
            String sql = "select Contact_ID, Contact_Name from contacts where Contact_Name = '" + contactCombo.getValue() + "'";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();

            while(queryResult.next()) {
                int d_id = queryResult.getInt("Contact_ID");
                contactID.add(d_id);
            }
            if(!contactID.isEmpty()) {
                hiddenID.setText(String.valueOf(contactID.get(0)));
            }
        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Error updating Appointment!");
            alert.setContentText("Form contains blank fields");
            alert.showAndWait();
        }
    }
}
