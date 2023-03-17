package model;

/**
 * Month Model
 * @author Justonna Naing
 */
public class Months {
    private String months;
    private int total;

    /**
     * constructor for the Month
     * @param months month name
     * @param total total appointment count
     */
    public Months (String months, int total){
        this.months = months;
        this.total = total;
    }

    /**
     * getter for the month name
     * @return months
     */
    public String getMonths() {
        return months;
    }

    /**
     * setter for the month name
     * @param months months to set
     */
    public void setMonths(String months) {
        this.months = months;
    }

    /**
     * getter for the total
     * @return total
     */
    public int getTotal() {
        return total;
    }

    /**
     * setter for the total
     * @param total total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }
}
