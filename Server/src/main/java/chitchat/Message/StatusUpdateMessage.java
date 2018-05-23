/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.Message;

import chitchat.User.User;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author milan
 */
public class StatusUpdateMessage implements Message, Serializable {

    private String username;
    private MessageType type;
    private final HashMap<String, User> users;
    
    public StatusUpdateMessage(HashMap<String, User> users) {
        this.users = users;
        this.type = MessageType.STATUS;
    }

    public MessageType getType() {
        return type;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return "StatusUpdate";
    }
    
    public HashMap<String, User> getUsers(){
        return users;
    }
    
}
