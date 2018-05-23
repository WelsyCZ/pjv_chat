/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.Message;

import chitchat.User.Status;
import java.io.Serializable;

/**
 *  Intended for changing the status of a user
 * @author welsemil
 */
public class StatusMessage implements Message, Serializable{
    private MessageType type;
    private Status status;
    private String username;

    /**
     * Constructor
     * @param username who's status will be changed
     * @param status the new status value from Status enum
     */
    public StatusMessage(String username, Status status) {
        this.status = status;
        this.username = username;
        this.type = MessageType.STATUS;
    }
    /**
     * Status getter
     * @return new status
     */
    public Status getStatus(){
        return status;
    }
    /**
     * type getter
     * @return message type
     */
    public MessageType getType() {
        return type;
    }

    /**
     * username getter
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * description getter
     * @return short description
     */
    public String getContent() {
        return "status msg";
    }
    
}
