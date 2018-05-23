package chitchat.User;

import java.io.Serializable;

public class User implements Serializable
{
    private String name;
    private Status status;

    public User(String name, Status status)
    {
        this.name = name;
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
}
