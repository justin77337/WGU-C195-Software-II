package controller;

import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import helper.DataAccess;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import model.*;

/**
 * Customer Screen Controller Class
 * @author Justonna Naing
 */
public class customerscreen implements Initializable {
    private static Customer selectedcustomer;
    public TableView customerTable;
    public TableColumn customerIdCol;
    public TableColumn customerNameCol;
    public TableColumn customerPhoneCol;
    public TableColumn customerAddressCol;
    public TableColumn customerCountryCol;
    public TableColumn customerProvincesCol;
    public TableColumn customerPostalCol;

    /*
     * Return selected customers
     */
    public static Customer customerModify(){
        return selectedcustomer;
    }


    /**
     * Initialize the controller and populate customer data in the table.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am in Customer Screen!");

        ObservableList<Customer> customerList = DataAccess.getAllCustomers();

        customerTable.setItems(DataAccess.getAllCustomers());

        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phonenumber"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        customerProvincesCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("postalcode"));

        customerTable.getSortOrder().addAll(customerIdCol); //sort by Customer ID column.


    }

    /**
     * Go back to Main screen
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
     * Load Add Customer Screen
     * @param actionEvent on Click Add Button
     * @throws IOException
     */
    public void onClickAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/addcustomerscreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,400,550);
        stage.setTitle("Add Customer Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Load Customer Modify Screen
     * @param actionEvent on Click Modify Button
     * @throws IOException
     */
    public void onClickModifyCustomer(ActionEvent actionEvent) throws IOException {

        selectedcustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();

        /*
         * Show the warning message if there is no selection.
         */
        if (selectedcustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Select an customer to modify!");
            alert.setContentText("Customer was not selected");
            alert.showAndWait();
        }
        /**
         * if an customer was selected, go to Modify Customer Screen
         */
        else {
            Parent root = FXMLLoader.load(getClass().getResource("/view/modifycustomerscreen.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 400, 550);
            stage.setTitle("Modify Customer Screen");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Delete selected Customer from the Database
     * @param actionEvent on Click Delete Button
     * @throws IOException
     * @throws SQLException
     */
    public void onClickDelete(ActionEvent actionEvent) throws IOException, SQLException {
        boolean deleted = false;

        selectedcustomer = (Customer) customerTable.getSelectionModel().getSelectedItem();



        /**
         * show warning message if there is no selection
         */
        if (selectedcustomer == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Select an Customer to delete!");
            alert.setContentText("Customer was not selected");
            alert.showAndWait();

            /**
             * confirmation message
              */
        } else {
            int customerID = selectedcustomer.getId();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setHeaderText("Selected Customer and all the associated appointments will be deleted!");
            alert.setContentText("Are you sure you want to delete selected Customer?");

            Optional<ButtonType> result = alert.showAndWait();

            /**
             * on Click "OK" button, delete selected customer from the database.
             */
            if (result.get() == ButtonType.OK) {

                //Delete Appointment First
                String sql_appointments = "DELETE FROM appointments WHERE Customer_ID = ?";
                PreparedStatement ps_appointment = JDBC.getConnection().prepareStatement(sql_appointments);
                ps_appointment.setInt(1, customerID);
                ps_appointment.executeUpdate();

                //After Appointments are deleted, Delete Customer!!
                String sql = "DELETE FROM customers WHERE Customer_ID = ?";
                PreparedStatement ps_customer = JDBC.getConnection().prepareStatement(sql);
                ps_customer.setInt(1, customerID);
                ps_customer.executeUpdate();

                deleted = true;

                /**
                 * show this message after an customer was successfully deleted from the database.
                 */
                if (deleted) {

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(null);
                    alert.setHeaderText(null);
                    alert.setContentText("An Customer ID " + customerID + " was successfully deleted!");
                    alert.showAndWait();

                    /**
                     * Load Customer Screen.
                     */
                    Parent root = FXMLLoader.load(getClass().getResource("/view/customerscreen.fxml"));
                    Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root,1000,400);
                    stage.setTitle("Customers Screen");
                    stage.setScene(scene);
                    stage.show();
                }

                /**
                 * if user clicked cancel, Load Customer Screen!
                 */
            } else {
                Parent root = FXMLLoader.load(getClass().getResource("/view/customerscreen.fxml"));
                Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root,1000,400);
                stage.setTitle("Customers Screen");
                stage.setScene(scene);
                stage.show();
            }
        }

    }
}
