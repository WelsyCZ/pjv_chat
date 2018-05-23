/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.Message;

import java.io.Serializable;

/**
 * Last message to be sent to server - sent when disconnecting
 * @author welsemil
 */
public class DisconnectMessage implements Message, Serializable{
     private String username;
    private MessageType type;
    /**
     * constructor
     * @param username who is disconnecting
     */
    public DisconnectMessage(String username) {
        this.username = username;
        this.type = MessageType.DISCONNECT;
    }

    /**
     * 
     * @return type of the message from MessageType enum
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
     * 
     * @return short description
     */
    public String getContent(){
        return "ConnectMessage";
    }
}
