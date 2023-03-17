package model;

/**
 * Type model
 * @author Justonna Naing
 */
public class Type {
    private String type;
    private int total;

    /**
     * constructor for the Type
     * @param type type
     * @param total total
     */
    public Type (String type, int total){
        this.type = type;
        this.total = total;
    }

    /**
     * getter for the type
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * setter for the type
     * @param type type to set
     */
    public void setType(String type) {
        this.type = type;
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
