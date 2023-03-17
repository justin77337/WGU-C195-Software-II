package model;

/**
 * USER MODEL
 * @author  Justonna Naing
 */
public class User {
    private int id;
    private String name;

    /**
     * constructor for the user
     * @param id user id
     * @param name user name
     */
    public User (int id, String name){
        this.id = id;
        this.name = name;
    }

    /**
     * getter for the user id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * setter for the user id
     * @param id id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter for the user name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * setter for the user name
     * @param name name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}
