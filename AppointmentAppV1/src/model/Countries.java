package model;

/**
 * Country Model
 * @author Justonna Naing
 */
public class Countries {
    private int id;
    private String country;

    /**
     * Constructor for the Country
     * @param id country ID
     * @param country Country Name.
     */
    public Countries (int id, String country){
        this.id = id;
        this.country = country;
    }

    /**
     * Getter for the country name.
     * @return country name.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter for the country name.
     * @param country country name to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Getter for the country ID.
     * @return Country ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the country ID.
     * @param id country ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Override to String (country name) instead of showing build in ID.
     * @return country name.
     */
    @Override
    public String toString() {
        return country;
    }

}
