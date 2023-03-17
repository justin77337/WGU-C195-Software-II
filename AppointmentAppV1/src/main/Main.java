package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Appointment Scheduling Desktop Application.
 * Login Screen is in English or French based on user's computer language Setting. (Default Language is English if user chose other than English or French).
 * @author Justonna Naing
 * Student ID: 001467971
 * */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ResourceBundle resourceBundle = null;
        ResourceBundle rb = resourceBundle.getBundle("helper/language", Locale.getDefault());

        Parent root = FXMLLoader.load(getClass().getResource("/view/loginscreen.fxml"));
        stage.setTitle(rb.getString("screentitle"));
        stage.setScene(new Scene(root, 500, 350));
        stage.show();
    }


    /**
     * Entry point of the application and launch the application
     * @param args
     */
    public static void main(String[] args){
        /**
         * Testing Language changes!
         */
        // Locale.setDefault(new Locale("en"));

        /**
         * Only allow language to be English or French!!
         */
        if(Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("fr")){
            Locale.getDefault();
        }

        /**
         * If system language is selected other than English or Frensh --> Set Default to "English" !!
         */
        else{
            Locale.setDefault(new Locale("en"));
        }

        /**
         * Open Database Connection.
         */
        JDBC.openConnection();

        launch(args);

        /**
         * Close Database Connection.
         */
        JDBC.closeConnection();
    }
}
