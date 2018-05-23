/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.Message;

import java.io.Serializable;

/**
 * ConfirmMessage is used to communicate to Client whether his chosen username is available
 * @author welsemil
 */
public class ConfirmMessage implements Message, Serializable{
    private String username;
    private final MessageType type;
    private final boolean usernameAvailable;

    /**
     * Constructor
     * @param username - the user who is sending the message (usually server)
     * @param usernameAvailable - true if available, false otherwise
     */
    public ConfirmMessage(String username, boolean usernameAvailable) {
        this.username = username;
        this.usernameAvailable = usernameAvailable;
        this.type = MessageType.CONFIRM;
        
    }

    /**
     * 
     * @return the message type, value from the MessageType enum
     */
    @Override
    public MessageType getType() {
        return type;
    }
    /**
     * 
     * @return true if your chosen username is available
     */
    public boolean getUsernameAvailable(){
        return usernameAvailable;
    }
    /**
     * 
     * @return username of the message composer (usually server)
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @return String description of the message
     */
    public String getContent(){
        return "ConfirmMessage";
    }
}
