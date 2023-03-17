package controller;

import helper.DataAccess;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import helper.JDBC;
import model.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Login Screen Controller Class.
 * @author Justonna Naing
 */
public class loginscreen implements Initializable {


    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginButton;
    public Button exitButton;
    public Label timezoneLabel;
    public Label timeZoneText;
    public Label titleLabel;
    public Label passwordLabel;
    public Label usernameLabel;

    /**
     * Initialize the controller.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am in LoginScreen!");
        timeZoneText.setText(String.valueOf(ZoneId.systemDefault()));

        ResourceBundle rb = resourceBundle.getBundle("helper/language", Locale.getDefault());

        /**
         * Setting Text based on System default Language --> Either English or French
         */
        titleLabel.setText(rb.getString("title"));
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        loginButton.setText(rb.getString("login"));
        exitButton.setText(rb.getString("exit"));
        timezoneLabel.setText(rb.getString("timezone"));


    }

    /**
     * Close Database Connection
     * Exit the Program
     * @param actionEvent Exit button action
     */
    public void onClickExit(ActionEvent actionEvent) {
        JDBC.closeConnection();
        System.exit(0);
    }

    /**
     * Load Main Screen Controller after successfully login.
     * @param actionEvent Login Button Action
     * @throws IOException
     */
    public void onClickLogin(ActionEvent actionEvent) throws IOException {

        FileWriter file = new FileWriter("login_activity.txt", true);
        PrintWriter printfile = new PrintWriter(file);

        ResourceBundle resourceBundle = null;
        ResourceBundle rb = resourceBundle.getBundle("helper/language", Locale.getDefault());

        /**
         * input validation --> checking both usernameField and passwordFields are not empty
         */
        if (usernameField.getText().isBlank() == false && passwordField.getText().isBlank() == false) {

            JDBC.getConnection();

            /**
             * User account verification using sql command.
             */
            String verifyLogin = "select count(1) from users where User_Name = '" + usernameField.getText() + "' and Password = '"+ passwordField.getText() +"'";

            try{
                Statement statement = JDBC.connection.createStatement();
                ResultSet queryResult = statement.executeQuery(verifyLogin);

                while (queryResult.next()){
                    /**
                     * if the result query return 1 then there is an matching user name and password! go to Main Screen.
                     */
                    if (queryResult.getInt(1) == 1){

                        /**
                         * Log successful activity to the file.
                         */
                        printfile.println("Login Attempt:");
                        printfile.println("User '" + usernameField.getText() + "' successfully logged in at: " + Timestamp.valueOf(LocalDateTime.now()));


                        /*
                        * Appointment 15 minutes Reminder Session!!
                        */
                        try {
                            ObservableList<Appointment> appointmentList = DataAccess.getAllAppointments();

                            boolean hasAppointment = false;
                            int appointmentID = 0;
                            LocalDateTime appointmentTime = null;

                            if (appointmentList != null) {
                                for (Appointment appointment: DataAccess.getAllAppointments()) {
                                    if (appointment.getStart().isAfter(LocalDateTime.now().minusMinutes(1)) && appointment.getStart().isBefore(LocalDateTime.now().plusMinutes(15))) {
                                        appointmentID = appointment.getId();
                                        appointmentTime = appointment.getStart();
                                        hasAppointment = true;
                                    }
                                }
                            }
                            /*
                             * Reminder Alert if there is an appointments within 15 mintues!
                             */
                            if(hasAppointment)
                            {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Reminder!");
                                alert.setHeaderText(null);
                                alert.setContentText("You have an appointment in 15 minutes!\n" + "ID: " + appointmentID + "\n" + "Appointment start time: " + appointmentTime);
                                alert.showAndWait();
                            }
                            /*
                             * Show this message if there is no appointments within 15 minutes
                             */
                            else{
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle(null);
                                alert.setHeaderText(null);
                                alert.setContentText("There are no upcoming appointments!");
                                alert.showAndWait();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        /*
                         * After successfully logged in --> Go to the Main Screen!
                         */
                        Parent root = FXMLLoader.load(getClass().getResource("/view/mainscreen.fxml"));
                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root, 250, 400);
                        stage.setTitle("Main Screen");
                        stage.setScene(scene);
                        stage.show();
                    }
                    else {

                        /**
                         * Log unsuccessful activity to the file.
                         */
                        printfile.println("Login Attempt:");
                        printfile.println("User '" + usernameField.getText() + "' failed log in at: " + Timestamp.valueOf(LocalDateTime.now()));

                        /**
                         * if the result query return 0 then no matching username and password.
                         * Show below message!
                         */
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle(rb.getString("errortitle"));
                        alert.setHeaderText(rb.getString("invalidaccount"));
                        alert.setContentText(rb.getString("invaliddescription"));
                        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(rb.getString("okay"));
                        alert.showAndWait();
                    }
                }

                printfile.close();
            }
            catch (Exception e){
                e.printStackTrace();

            }

        }
        else{
            /**
             * Show Alerts if the username field or password field is Empty!
             */
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(rb.getString("errortitle"));
            alert.setHeaderText(rb.getString("errorlogin"));
            alert.setContentText(rb.getString("errordescription"));
            ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText(rb.getString("okay"));
            alert.showAndWait();
        }
    }

}
