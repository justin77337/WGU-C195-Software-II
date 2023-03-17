package model;

import java.time.LocalDateTime;

/**
 * Appointment Model
 * @author Justonna Naing
 */
public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private int contact;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;

    /**
     * Constructor for the Appointment
     * @param id ID of the appointment
     * @param title title of the appointment
     * @param description description of the appointment
     * @param location location of the appointment
     * @param contact contact of the appointment
     * @param type type of the appointment
     * @param start startDate and Time of the appointment
     * @param end endDate and Time of the appointment
     * @param customerID customerID for the appointment
     * @param userID userID for the appointment
     */
    public Appointment(int id, String title, String description, String location, int contact, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID){
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
    }

    /**
     * Getter for the appointment ID.
     * @return ID of the appointment.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the appointment iD.
     * @param id ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the appointment title.
     * @return title of the appointment.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for the appointment title.
     * @param title title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for the appointment description
     * @return description of the appointment
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for the appointment description
     * @param description description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the appointment location
     * @return location for the appointment
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for the appointment location
     * @param location appointment location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for the appointment Contact
     * @return contact for the appointment
     */
    public int getContact() {
        return contact;
    }

    /**
     * Setter for the appointment contact
     * @param contact appointment contact to set
     */
    public void setContact(int contact) {
        this.contact = contact;
    }

    /**
     * Getter for the appointment Type.
     * @return Type for the appointment.
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for the appointment Type
     * @param type appointment type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for the appointment Start Date and Time.
     * @return Start Date and Time of the appointment
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Setter for the Appointment Start Date and Time
     * @param start appointment start date and time to set.
     */
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    /**
     * Getter for the appointment End Date and time.
     * @return end date and time of the appointment.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Setter for the appointment End Date and time
     * @param end appointment end date and time to set.
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Getter for the appointment Customer ID.
     * @return customer ID of the appointment.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Setter for the appointment Customer ID.
     * @param customerID appointment customer ID to set.
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Getter for the appointment User ID.
     * @return user ID of the appointment.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Setter for the appointment User ID.
     * @param userID appointment User ID to set.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

}
