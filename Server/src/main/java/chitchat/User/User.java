package chitchat.User;

import java.io.Serializable;
/**
 * very generic User class, only contains username and status
 * @author welsemil
 */
public class User implements Serializable
{
    private String name;
    private Status status;
    /**
     * constructor
     * @param name the username
     * @param status the user's status
     */
    public User(String name, Status status)
    {
        this.name = name;
        this.status = status;
    }

    /** 
     * username getter
     * @return name (username)
     */
    public String getName()
    {
        return name;
    }
    /**
     * Status getter
     * @return status
     */
    public Status getStatus()
    {
        return status;
    }
    /**
     * Status Setter
     * @param status the new status
     */
    public void setStatus(Status status)
    {
        this.status = status;
    }
}
