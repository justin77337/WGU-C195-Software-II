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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.*;
import helper.DataAccess;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Reports Screen Controller Class
 * @author Justonna Naing
 **/
public class reportscreen implements Initializable {
    public ComboBox contactCombo;
    public TableView Table1;
    public TableColumn t1_IDCol;
    public TableColumn t1_titleCol;
    public TableColumn t1_typeCol;
    public TableColumn t1_descriptionCol;
    public TableColumn t1_startDateTimeCol;
    public TableColumn t1_endDateTimeCol;
    public TableColumn t1_customerIDCol;
    public TableView Table2;
    public TableColumn t2_typeCol;
    public TableColumn t2_totalCol;
    public TableView Table3;
    public TableColumn t3_monthsCol;
    public TableColumn t3_totalCol;
    public TableView Table4;
    public TableColumn t4_countryNameCol;
    public TableColumn t4_totalCol;

    /**
     * Initialize Report Screen Controller.
     * Populate data in the tables.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am in Report Screen!");

        ObservableList<Contact> contactList = DataAccess.getAllContacts();
        ObservableList<String> contactName = FXCollections.observableArrayList();

        /**
         * Populate contact names in contact Combo box.
         */
        contactList.forEach(Contact -> contactName.add(Contact.getName()));
        Collections.sort(contactName); //sorting the list before setting the items to the combo box.
        contactCombo.setItems(contactName);

        /**
         * Appointments by Types and total counts
         */
        ObservableList<Type> appointmentsByType = DataAccess.getAppointmentsByType();
        Table2.setItems(appointmentsByType);

        t2_typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        t2_totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));


        /**
         * Appointments by Months and total counts
         */
        ObservableList<Months> appointmentsByMonth = DataAccess.getAppointmentsByMonth();
        Table3.setItems(appointmentsByMonth);

        t3_monthsCol.setCellValueFactory(new PropertyValueFactory<>("months"));
        t3_totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));


        /**
         * Appointments by Country and total counts
         * This is the additional report.
         */
        ObservableList<Countries> appointmentsByCountry = DataAccess.getAppointmentsByCountry();
        Table4.setItems(appointmentsByCountry);

        t4_countryNameCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        t4_totalCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    }

    /**
     * Populate Table1 Data based on Contact Name selection from contact combo box.
     * @param actionEvent on click Contact Combo Box
     */
    public void onContactSelectionCombo(ActionEvent actionEvent) {

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try{
            String sql = "select Appointment_ID,Title,Description,Location, a.Contact_ID,Type,Start,End,Customer_ID,User_ID, c.Contact_Name from appointments a\n" +
                    "inner join contacts c on a.Contact_ID = c.Contact_ID WHERE c.Contact_Name = '" + contactCombo.getValue() + "'";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next()){
                int id = queryResult.getInt("Appointment_ID");
                String title = queryResult.getString("Title");
                String description = queryResult.getString("Description");
                String location = queryResult.getString("Location");
                int contact = queryResult.getInt("Contact_ID");
                String type = queryResult.getString("Type");
                LocalDateTime start =  queryResult.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = queryResult.getTimestamp("End").toLocalDateTime();
                int customerID = queryResult.getInt("Customer_ID");
                int userID = queryResult.getInt("User_ID");

                Appointment appointment = new Appointment(id, title, description, location, contact, type, start, end, customerID, userID);
                appointmentList.add(appointment);
            }

            Table1.setItems(appointmentList);

            t1_IDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            t1_titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            t1_typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            t1_descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            t1_startDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            t1_endDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            t1_customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

            Table1.getSortOrder().addAll(t1_IDCol); //sort the table by ID column.
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

    }

    /**
     * Go back to Main Screen.
     * @param actionEvent on Click Back Button
     * @throws IOException
     */
    public void onClickBack(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/mainscreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,250,400);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();
    }
}
