package model;

import java.time.LocalDateTime;

/**
 * Contact Model
 * @author  Justonna Naing
 */
public class Contact {
    private int id;
    private String name;
    private String email;

    /**
     * Constructor for the Contact.
     * @param id Contact ID
     * @param name Contact name.
     * @param email Contact email
     */
    public Contact(int id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Getter for the Contact ID.
     * @return Contact ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the Contact ID.
     * @param id Contact ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter for the Contact Name
     * @return Contact Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the contact Name.
     * @param name contact name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the contact email
     * @return contact email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter for the contact email
     * @param email contact email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
