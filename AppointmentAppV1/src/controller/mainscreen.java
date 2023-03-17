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
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Main Screen Controller Class
 * @author Justonna Naing
 */
public class mainscreen implements Initializable {

    /**
     * Initialize the controller
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am in Mainscreen!");

    }

    /**
     * Load the Appointment Screen
     * @param actionEvent Appointment Button Action
     * @throws IOException
     */
    public void onClickAppointments(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/appointmentscreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,400);
        stage.setTitle("Appointments Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Load the Customer Screen
     * @param actionEvent Customer Button Action
     * @throws IOException
     */
    public void onClickCustomers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/customerscreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,400);
        stage.setTitle("Customers Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Load the Reports Screen
     * @param actionEvent Customer Button Action
     * @throws IOException
     */
    public void onClickReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/reportscreen.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root,1000,600);
        stage.setTitle("Reports Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Close Database Connection and Exit the Application
     * @param actionEvent logout Button Action
     */
    public void onClickLogout(ActionEvent actionEvent) {
        JDBC.closeConnection();
        System.exit(0);
    }
}
