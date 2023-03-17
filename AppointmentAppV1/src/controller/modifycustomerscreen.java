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
import model.Divisions;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Modify Customer Screen Controller Class
 * @author Justonna Naing
 **/
public class modifycustomerscreen implements Initializable {

    public TextField idField;
    public TextField nameField;
    public TextField phoneField;
    public ComboBox countryCombo;
    public TextField addressField;
    public ComboBox provincesCombo;
    public TextField postalField;
    public TextField hiddenID;
    private Customer  selectedcustomer;

    /**
     * Initialize Modify Customer Screen.
     * Populate the text fields with selected Customer Data.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am in Modify Customer Screen!");

        selectedcustomer = customerscreen.customerModify();
        ObservableList<Countries> countryList = DataAccess.getAllCountries();
        countryCombo.setItems(DataAccess.getAllCountries());
        provincesCombo.setItems(DataAccess.getAllDivisions());

        idField.setText(String.valueOf(selectedcustomer.getId()));
        nameField.setText(String.valueOf(selectedcustomer.getName()));
        phoneField.setText(String.valueOf(selectedcustomer.getPhonenumber()));
        addressField.setText(String.valueOf(selectedcustomer.getAddress()));
        countryCombo.setValue(String.valueOf(selectedcustomer.getCountry()));
        provincesCombo.setValue(String.valueOf(selectedcustomer.getDivision()));
        postalField.setText(String.valueOf(selectedcustomer.getPostalcode()));
        hiddenID.setText(String.valueOf(DataAccess.getDivisionID().get(0)));

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
     * Get Data for Division Combo based on Country Combo box selection. Load Data into Division Combo box.
     * @param actionEvent on Click Country ComboBox.
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
            provincesCombo.setItems(divisionList);
        }
        catch (SQLException throwables){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error!");
            alert.setHeaderText("Error updating customer!");
            alert.setContentText("Form contains blank fields");
            alert.showAndWait();
        }
    }

    /**
     * Get the Divison ID for hidden field. It was required to modify customer data properly into the Database.
     * @param actionEvent on Click Division Combo Box.
     */
    public void comboSelection2(ActionEvent actionEvent) {

        ObservableList<Integer> divisionID = FXCollections.observableArrayList();

        try {
            String sql = "select Division_ID, Division from first_level_divisions where Division = '" + provincesCombo.getValue() + "'";

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
            alert.setHeaderText("Error updating customer!");
            alert.setContentText("Form contains blank fields");
            alert.showAndWait();
        }
    }

    /**
     * Save modified customer data to the database.
     * @param actionEvent on Click Save Button.
     */
    public void onClickSave(ActionEvent actionEvent) {
        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText().toString();
        String address = addressField.getText().toString();
        String postal = postalField.getText().toString();
        String phone = phoneField.getText().toString();
        String country = countryCombo.getValue().toString();
        String provinces = provincesCombo.getValue().toString();
        String division_ID = hiddenID.getText();

        try{
            /**
             * Empty fields check and show alert if some or all fields are blank.
             */
            if (name.isBlank() || address.isBlank() || postal.isBlank() || phone.isBlank()
                    || country.isBlank() || provinces.isBlank() || division_ID.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error!");
                alert.setHeaderText("Error updating customer!");
                alert.setContentText("Form contains blank fields");
                alert.showAndWait();
            }
            else {
                /**
                 * If fields are not Empty, Add modified data to the database.
                 */
                String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
                PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

                ps.setString(1, name);
                ps.setString(2, address);
                ps.setString(3, postal);
                ps.setString(4, phone);
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(6, "test");
                ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(8, "test");
                ps.setInt(9, Integer.parseInt(division_ID));
                ps.setInt(10, id);

                ps.executeUpdate();

                /**
                 * Show message after a customer was updated to the database.
                 */
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(null);
                alert.setHeaderText(null);
                alert.setContentText("Customer information was updated in the database!");
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
            alert.setHeaderText("Error updating customer!");
            alert.setContentText("Form contains blank fields");
            alert.showAndWait();

        }
    }
}
