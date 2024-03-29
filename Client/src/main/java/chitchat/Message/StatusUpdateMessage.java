/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.Message;

import chitchat.User.User;
import java.io.Serializable;

/**
 * StatusUpdateMessage is only sent by server to clients
 * refreshes the lists of online users
 * @author welsemil
 */
public class StatusUpdateMessage implements Message, Serializable {

    private String username;
    private MessageType type;
    private final User[] users;
    /**
     * constructor
     * @param users all connected instances of User in an array
     */
    public StatusUpdateMessage(User[] users) {
        this.users = users;
        this.type = MessageType.STATUS;
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
     * Description getter
     * @return short description
     */
    public String getContent() {
        return "StatusUpdate";
    }
    /**
     * Users getter
     * @return User[] array, all connected users
     */
    public User[] getUsers(){
        return users;
    }
    
}
