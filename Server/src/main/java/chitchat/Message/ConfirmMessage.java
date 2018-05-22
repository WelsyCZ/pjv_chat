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
public class ConfirmMessage implements Message, Serializable{
    private String username;
    private final MessageType type;
    private final boolean usernameAvailable;

    public ConfirmMessage(String username, boolean usernameAvailable) {
        this.username = username;
        this.usernameAvailable = usernameAvailable;
        this.type = MessageType.CONFIRM;
        
    }

    @Override
    public MessageType getType() {
        return type;
    }
    
    public boolean getUsernameAvailable(){
        return usernameAvailable;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
