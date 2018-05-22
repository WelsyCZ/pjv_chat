/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.Message;

import java.io.Serializable;

/**
 *
 * @author milan
 */
public class DisconnectMessage implements Message, Serializable{
     private String username;
    private MessageType type;

    public DisconnectMessage(String username) {
        this.username = username;
        this.type = MessageType.DISCONNECT;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getContent(){
        return "ConnectMessage";
    }
}
