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
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Countries;
import model.Customer;
import helper.DataAccess;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Add Customer Screen Controller Class
 * @author Justonna Naing
 **/
public class addcustomerscreen implements Initializable {
    public ComboBox countryCombo;
    public ComboBox divisionCombo;
    public TextField idField;
    public TextField nameField;
    public TextField phoneField;
    public TextField addressField;
    public TextField postalField;
    public TextField hiddenID;


    /**
     * Initialize the Add Customer Screen
     * Load data in the Country Combo Box
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am in Add Customer Screen!");

        ObservableList<Countries> countryList = DataAccess.getAllCountries();
        countryCombo.setItems(DataAccess.getAllCountries()); //LOAD DATA IN COUNTRY COMBO BOX

    }

    /**
     * Load the Main Screen
     * @param actionEvent on Click Cancel Button
     * @throws IOException
     */
    public void onClickCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customerscreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Save new customer data to the database.
     * @param actionEvent on Click Save Button.
     * @throws IOException
     * @throws SQLException
     */
    public void onClickSave(ActionEvent actionEvent) throws IOException, SQLException {

            ObservableList<Customer> customerList = DataAccess.getAllCustomers();

            try {
                int id = customerList.size() + (int) (Math.random()*100); //generate random number for the customer ID.
                String name = nameField.getText().toString();
                String address = addressField.getText().toString();
                String postal = postalField.getText().toString();
                String phone = phoneField.getText().toString();
                String country = countryCombo.getValue().toString();
                String division = divisionCombo.getValue().toString();
                String division_ID = hiddenID.getText();

                /**
                 * Empty fields check and show alert if some or all fields are blank.
                 */
                if (name.isBlank() || address.isBlank() || postal.isBlank() || phone.isBlank()
                        || country == null || division == null || division_ID.isBlank()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Error adding customer!");
                    alert.setContentText("Form contains blank fields");
                    alert.showAndWait();
                }
                /**
                 * If fields are not Empty, Add new data to the database.
                 */
                else {
                    String sql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

                    ps.setInt(1, id);
                    ps.setString(2, name);
                    ps.setString(3, address);
                    ps.setString(4, postal);
                    ps.setString(5, phone);
                    ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(7, "test");
                    ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                    ps.setString(9, "test");
                    ps.setInt(10, Integer.parseInt(division_ID));

                    ps.executeUpdate();

                    /**
                     * Show message after a new customer is added to the database.
                     */
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(null);
                    alert.setHeaderText(null);
                    alert.setContentText("A new Customer Added to the database!");
                    alert.showAndWait();

                    /**
                     * Go back to the Customer Screen.
                     */
                    Parent root = FXMLLoader.load(getClass().getResource("/view/customerscreen.fxml"));
                    Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root,1000,400);
                    stage.setTitle("Customers Screen");
                    stage.setScene(scene);
                    stage.show();
                }

            }catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setHeaderText("Error adding customer!");
                alert.setContentText("Form contains blank fields");
                alert.showAndWait();
            }

    }

    /**
     * Get Data for Division Combo based on Country Combo box selection. Load Data into Division Combo box.
     * @param actionEvent on Click Country Combo Box.
     */
    public void comboSelection(ActionEvent actionEvent) {

        ObservableList<Divisions> divisionList = FXCollections.observableArrayList();

        try{
            String sql = "select f.Division, c.Country from first_level_divisions f inner join countries c \n" +
                    "on f.Country_ID = c.Country_ID where Country = '" + countryCombo.getValue() + "'";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next()){
                String country = queryResult.getString("Country");
                String division = queryResult.getString("Division");

                Divisions newlist = new Divisions (country, division);
                divisionList.add(newlist);
            }
            divisionCombo.setItems(divisionList);
        }
        catch (SQLException throwables){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Error adding customer!");
            alert.setContentText("Form contains blank fields");
            alert.showAndWait();
        }
    }

    /**
     * Get the Divison ID for hidden field. It was required to insert customer data properly into the Database.
     * @param actionEvent on Click Division Combo Box.
     */
    public void comboSelection2(ActionEvent actionEvent) {

        ObservableList<Integer> divisionID = FXCollections.observableArrayList();

        try {
            String sql = "select Division_ID, Division from first_level_divisions where Division = '" + divisionCombo.getValue() + "'";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();

            while(queryResult.next()) {
                int d_id = queryResult.getInt("Division_ID");
                divisionID.add(d_id);
            }
            if(!divisionID.isEmpty()) {
                hiddenID.setText(String.valueOf(divisionID.get(0)));
            }
        }catch (SQLException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Error adding customer!");
            alert.setContentText("Form contains blank fields");
            alert.showAndWait();
        }
    }
}
