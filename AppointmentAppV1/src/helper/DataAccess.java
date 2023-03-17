package helper;

import com.sun.source.tree.BreakTree;
import controller.customerscreen;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.*;

/**
 * Data Access Helper Class.
 * @author Justonna Naing
 **/
public class DataAccess {

    /**
     * Method to get all Customer information
     * @return customerList in ObservableList.
     */
    public static ObservableList<Customer> getAllCustomers(){

        ObservableList<Customer> customerList = FXCollections.observableArrayList();

        try{
            String sql = "select c.Customer_ID, c.Customer_Name, c.Phone, c.Address, e.Country, d.Division, c.Postal_Code from customers c \n" +
                    "inner join first_level_divisions d\n" +
                    "on c.Division_ID = d.Division_ID \n" +
                    "inner join countries e\n" +
                    "on d.Country_ID = e.Country_ID";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next()){
                int id = queryResult.getInt("Customer_ID");
                String name = queryResult.getString("Customer_Name");
                String phonenumber = queryResult.getString("Phone");
                String address = queryResult.getString("Address");
                String country = queryResult.getString("Country");
                String division = queryResult.getString("Division");
                String postalcode = queryResult.getString("Postal_Code");

                Customer customer = new Customer(id, name, phonenumber, address, country, division, postalcode);
                customerList.add(customer);
                }


            }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return customerList;
    }

    /**
     * Method to get All Countries Data.
     * @return countries list in ObservableList.
     */
    public static ObservableList<Countries> getAllCountries(){

        ObservableList<Countries> countriesList = FXCollections.observableArrayList();

        try{
            String sql = "select Country_ID, Country from countries";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next()){
                int id = queryResult.getInt("Country_ID");
                String country = queryResult.getString("Country");

                Countries newlist = new Countries (id, country);
                countriesList.add(newlist);
            }

        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return countriesList;
    }

    /**
     * Method to get all First Level Division Data.
     * @return first level division list in ObservableList.
     */
    public static ObservableList<Divisions> getAllDivisions(){

        ObservableList<Divisions> divisionList = FXCollections.observableArrayList();

        Customer selectedcustomer;
        selectedcustomer = customerscreen.customerModify();


        try{
            String sql = "select f.Division, c.Country from first_level_divisions f inner join countries c \n" +
                    "on f.Country_ID = c.Country_ID where Country = '" + selectedcustomer.getCountry() + "'";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next()){
                String country = queryResult.getString("Country");
                String division = queryResult.getString("Division");

                Divisions newlist = new Divisions (country, division);
                divisionList.add(newlist);
            }

        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return divisionList;
    }

    /**
     * Method to get Division IDs.
     * @return divisionID in ObservableList.
     */
    public static ObservableList<Integer> getDivisionID() {

        ObservableList<Integer> divisionID = FXCollections.observableArrayList();

        Customer selectedcustomer;
        selectedcustomer = customerscreen.customerModify();


        try {
            String sql = "select Division_ID, Division from first_level_divisions where Division = '" + selectedcustomer.getDivision() + "'";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();

            while (queryResult.next()) {
                int d_id = queryResult.getInt("Division_ID");
                divisionID.add(d_id);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return divisionID;
    }


    /**
     * Method to get All Appointments Data.
     * @return appointmentList in ObservableList.
     */
    public static ObservableList<Appointment> getAllAppointments(){

        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try{
            String sql = "select Appointment_ID,Title,Description,Location,Contact_ID,Type,Start,End,Customer_ID,User_ID from appointments;";

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

        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    /**
     * Method to get All Contact data.
     * @return contactList in ObservableList.
     */
    public static ObservableList<Contact> getAllContacts(){

        ObservableList<Contact> contactList = FXCollections.observableArrayList();

        try{
            String sql = "select * from contacts;";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next()){
                int id = queryResult.getInt("Contact_ID");
                String name = queryResult.getString("Contact_Name");
                String email = queryResult.getString("Email");

                Contact contact = new Contact(id, name, email);
                contactList.add(contact);
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return contactList;
    }

    /**
     * Method to get All Users Data.
     * @return userList in ObservableList.
     */
    public static ObservableList<User> getAllUsers(){

        ObservableList<User> userList = FXCollections.observableArrayList();

        try{
            String sql = "select User_ID, User_Name from users;";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();

            while(queryResult.next()){
                int id = queryResult.getInt("User_ID");
                String name = queryResult.getString("User_Name");


                User user = new User(id, name);
                userList.add(user);
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return userList;
    }

    /**
     * Method to get appointments by type.
     * @return appointment by type list in ObservableList.
     */
    public static ObservableList<Type> getAppointmentsByType(){

        ObservableList<Type> appointmentByTypeList = FXCollections.observableArrayList();

        try{
            String sql = "select Type as 'Appointment_Type' , count(*) as 'Total_Appointments' from appointments group by Type;";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next()){
                String type = queryResult.getString("Appointment_Type");
                int total = queryResult.getInt("Total_Appointments");

                Type appointments = new Type(type, total);
                appointmentByTypeList.add(appointments);
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return appointmentByTypeList;
    }

    /**
     * Method to get all the appointments by Month.
     * @return appointment by month list in ObservableList.
     */
    public static ObservableList<Months> getAppointmentsByMonth(){

        ObservableList<Months> appointmentByMonthist = FXCollections.observableArrayList();

        try{
            String sql = "select MONTHNAME(Start) as bymonth , count(*) as total from appointments group by MONTHNAME(Start);";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next()){
                String months = queryResult.getString("bymonth");
                int total = queryResult.getInt("total");

                Months appointments = new Months(months, total);
                appointmentByMonthist.add(appointments);
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return appointmentByMonthist;
    }


    /**
     * Method to get All the appointments by country
     * @return appointment by country list in ObservableList.
     */
    public static ObservableList<Countries> getAppointmentsByCountry(){

        ObservableList<Countries> appointmentByCountry = FXCollections.observableArrayList();

        try{
            String sql = "select cl.Country as Country, count(*) as total from appointments a inner join customers c on a.Customer_ID = c.Customer_ID\n" +
                    "inner join first_level_divisions d on c.Division_ID = d.Division_ID left join countries cl on d.Country_ID = cl.Country_ID group by cl.Country;";

            PreparedStatement statement = JDBC.getConnection().prepareStatement(sql);
            ResultSet queryResult = statement.executeQuery();


            while(queryResult.next()){
                String country = queryResult.getString("Country");
                int total = queryResult.getInt("total");

                Countries appointments = new Countries(total, country);
                appointmentByCountry.add(appointments);
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return appointmentByCountry;
    }

}
