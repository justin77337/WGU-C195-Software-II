package model;

/**
 * Customer Model
 * @author Justonna Naing
 */
public class Customer {
    private int id;
    private String name;
    private String phonenumber;
    private String address;
    private String country;
    private String division;
    private String postalcode;

    /**
     * coustructor for the customer
     * @param id customer ID
     * @param name customer Name
     * @param phonenumber customer Phone number
     * @param address customer Address
     * @param country customer Country
     * @param division customer Division
     * @param postalcode customer Postal Code
     */
    public Customer(int id, String name, String phonenumber, String address, String country, String division, String postalcode){
        this.id = id;
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
        this.country = country;
        this.division = division;
        this.postalcode = postalcode;
    }

    /**
     * Getter for the customer ID
     * @return the id
     */
    public int getId(){
        return id;
    }

    /**
     * Setter for the customer ID
     * @param id the id to set
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Getter for the customer Name
     * @return the customer name
     */
    public String getName(){
        return name;
    }

    /**
     * Setter for the customer Name
     * @param name the customer name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Getter for the customer phone number
     * @return the phone number
     */
    public String getPhonenumber(){
        return phonenumber;
    }

    /**
     * setter for the customer phone number
     * @param phonenumber the phone number to set
     */
    public void setPhonenumber(String phonenumber){
        this.phonenumber = phonenumber;
    }

    /**
     * Getter for the customer address
     * @return the address
     */
    public String getAddress(){
        return address;
    }

    /**
     * setter for the customer address
     * @param address the address to set
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * getter for the customer country
     * @return the country
     */
    public String getCountry(){
        return country;
    }

    /**
     * setter for the customer country
     * @param country the country to set
     */
    public void setCountry(String country){
        this.country = country;
    }

    /**
     * getter for the customer division
     * @return the Division
     */
    public String getDivision(){
        return division;
    }

    /**
     * setter for the customer division
     * @param division the division to set
     */
    public void setDivision(String division){
        this.division = division;
    }

    /**
     * getter for the customer postal code
     * @return the postalcode
     */
    public String getPostalcode(){
        return  postalcode;
    }

    /**
     * setter for the customer postal code
     * @param postalcode the postalcode to set
     */
    public void setPostalcode(String postalcode){
        this.postalcode = postalcode;
    }
}
