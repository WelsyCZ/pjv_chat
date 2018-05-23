/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.Message;

import java.io.Serializable;

/**
 * First message to be sent to the server, only contains username
 * @author welsemil
 */
public class ConnectMessage implements Message, Serializable {
    private String username;
    private MessageType type;
    /**
     * constructor
     * @param username is the name of the user joining the server
     */
    public ConnectMessage(String username) {
        this.username = username;
        this.type = MessageType.CONNECT;
    }

    /**
     * 
     * @return the message type, value from the MessageType enum
     */
    public MessageType getType() {
        return type;
    }
    /**
     * username getter
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    /**
     * 
     * @return short String description of the message
     */
    public String getContent(){
        return "ConnectMessage";
    }
    
    
}
