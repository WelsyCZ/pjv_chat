/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.Message;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author milan
 */
public class FileMessage implements Message, Serializable {

    private MessageType type;
    private String username;
    private boolean agree;

    public FileMessage(String username, boolean agree){
        this.username = username;
        this.agree = agree;
        type = MessageType.FILE;
    }
    
    public boolean getAgree(){
        return agree;
    }
    
    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getContent() {
        return "file send request";
    }
    
}
