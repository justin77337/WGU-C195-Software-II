package controller;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import helper.DataAccess;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Appointment Screen Controller Class
 * @author Justonna Naing
 */
public class appointmentscreen implements Initializable {
    private static Appointment selectedAppointment;
    public TableView appointmentTable;
    public TableColumn idCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn customerIDCol;
    public TableColumn userIDCol;
    public RadioButton allRadio;
    public RadioButton monthlyRadio;
    public RadioButton weeklyRadio;

    /*
     * Return selected appointments
     */
    public static Appointment appointmentModify(){
        return selectedAppointment;
    }

    /**
     * Initialize the controller and populate appointments data in the table.
     * LAMBDA - Use of First Lambda can be found in onClickMonthlyRadio Method. It is used to iterate all the appointments to match Dates are between current Month.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am in Appointment Screen!");

        ObservableList<Appointment> appointmentList = DataAccess.getAllAppointments();

        appointmentTable.setItems(appointmentList);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        appointmentTable.getSortOrder().addAll(idCol);

    }

    /**
     * Go back to Main Screen.
     * @param actionEvent on Click Cancel Button
     * @throws IOException
     */
    public void onClickCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/mainscreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,250,400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Load Add Appointment Screen
     * @param actionEvent on Click Add Button.
     * @throws IOException
     */
    public void onClickAdd(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/addappointmentscreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,400,550);
        stage.setTitle("Add Appointment Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Load Modify Appointment Screen.
     * @param actionEvent on Click Modify Button.
     * @throws IOException
     */
    public void onClickModify(ActionEvent actionEvent) throws IOException{

        selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

        /*
         * Run Time Error.
         * Error was corrected by preventing User from passing "null" value.
         */
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Select an appointment to modify!");
            alert.setContentText("Appointment was not selected");
            alert.showAndWait();
        }
        else {
            /**
             * if an appointment was selected, go to Modify Appointment Screen
             */
            Parent root = FXMLLoader.load(getClass().getResource("/view/modifyappointmentscreen.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 400, 550);
            stage.setTitle("Modify Appointment Screen");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Show All Appointments in the Table View.
     * @param actionEvent on Click All Radio Button
     */
    public void onClickAllRadio(ActionEvent actionEvent) {
        allRadio.setSelected(true);
        weeklyRadio.setSelected(false);
        monthlyRadio.setSelected(false);

        try {
            ObservableList<Appointment> appointmentList = DataAccess.getAllAppointments();

            if (appointmentList != null) {
                appointmentTable.setItems(appointmentList);
                appointmentTable.getSortOrder().addAll(idCol);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * <p> LAMBDA - Use of First Lambda. It is used to iterate all the appointments to match Dates are between current Month.</p>
     * Shows Appointments that are in the Current Month in the Table View.
     * @param actionEvent on Click Monthly Radio Button
     */
    public void onClickMonthlyRadio(ActionEvent actionEvent) {
        monthlyRadio.setSelected(true);
        allRadio.setSelected(false);
        weeklyRadio.setSelected(false);

        try {
            ObservableList<Appointment> appointmentList = DataAccess.getAllAppointments();
            ObservableList<Appointment> monthlyAppointment = FXCollections.observableArrayList();

            if (appointmentList != null) {
                appointmentList.forEach(appointment -> {
                    if (appointment.getEnd().isAfter(LocalDateTime.now().minusMonths(1)) && appointment.getEnd().isBefore(LocalDateTime.now().plusMonths(1))) {
                        monthlyAppointment.add(appointment);
                    }
                    appointmentTable.setItems(monthlyAppointment);
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Shows Appointments that are in the Current Week in the Table View.
     * @param actionEvent on Click Weekly Radio Button
     */
    public void onClickWeeklyRadio(ActionEvent actionEvent) {
        weeklyRadio.setSelected(true);
        allRadio.setSelected(false);
        monthlyRadio.setSelected(false);

        try {
            ObservableList<Appointment> appointmentList = DataAccess.getAllAppointments();
            ObservableList<Appointment> weeklyAppointment = FXCollections.observableArrayList();

            if (appointmentList != null) {
                appointmentList.forEach(appointment -> {
                    if (appointment.getEnd().isAfter(LocalDateTime.now().minusWeeks(1)) && appointment.getEnd().isBefore(LocalDateTime.now().plusWeeks(1))) {
                        weeklyAppointment.add(appointment);
                    }
                    appointmentTable.setItems(weeklyAppointment);
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Delete Selected Appointment from the Database.
     * @param actionEvent on Click Delete Button
     * @throws IOException
     */
    public void onClickDelete(ActionEvent actionEvent) throws IOException {
        boolean deleted = false;

        try {
            selectedAppointment = (Appointment) appointmentTable.getSelectionModel().getSelectedItem();

            int appointmentID = selectedAppointment.getId();
            String appointmentType = selectedAppointment.getType();

            /**
             * show warning message if there is no selection
             */
            if (selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setHeaderText("Select an Appointment to delete!");
                alert.setContentText("Appointment was not selected");
                alert.showAndWait();
            } else {
                /**
                 * confirmation message
                 */
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Alert");
                alert.setHeaderText("Selected Appointment will be deleted!");
                alert.setContentText("Are you sure you want to delete selected Appointment?");

                Optional<ButtonType> result = alert.showAndWait();

                /**
                 * on Click "OK" button, delete selected appointment from the database.
                 */
                if (result.get() == ButtonType.OK) {

                    String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
                    PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

                    ps.setInt(1, appointmentID);

                    ps.executeUpdate();

                    deleted = true;

                    /**
                     * show this message after an customer was successfully deleted from the database.
                     */
                    if (deleted) {

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(null);
                        alert.setHeaderText(null);
                        alert.setContentText("An Appointment ID: '" + appointmentID + "'  Type: '" + appointmentType + "' was successfully deleted!");
                        alert.showAndWait();

                        /**
                         * Load Appointment Screen.
                         */
                        Parent root = FXMLLoader.load(getClass().getResource("/view/appointmentscreen.fxml"));
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root, 1000, 400);
                        stage.setTitle("Appointments Screen");
                        stage.setScene(scene);
                        stage.show();
                    }

                    /**
                     * if user clicked cancel, Load Appointment Screen!
                     */
                } else {
                    Parent root = FXMLLoader.load(getClass().getResource("/view/appointmentscreen.fxml"));
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 1000, 400);
                    stage.setTitle("Appointments Screen");
                    stage.setScene(scene);
                    stage.show();
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Select an Appointment to delete!");
            alert.setContentText("Appointment was not selected");
            alert.showAndWait();
        }
    }
}
