package model;

/**
 * Division Model
 * @author Justonna Naing
 */
public class Divisions {
    private String country;
    private String division;

    /**
     * constructor for the division
     * @param country country
     * @param division division
     */
    public Divisions (String country, String division){
        this.country = country;
        this.division = division;
    }

    /**
     * getter for the country id
     * @return country
     */
    public String getId() {
        return country;
    }

    /**
     * setter for the country
     * @param country country to set
     */
    public void setId(String country) {
        this.country = country;
    }

    /**
     * getter for the division
     * @return division
     */
    public String getDivision() {
        return division;
    }

    /**
     * setter for the division
     * @param division division to set
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * override to tostring
     * @return division name
     */
    @Override
    public String toString() {
        return division;
    }
}
