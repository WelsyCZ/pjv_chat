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
    private File file;
    
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
        return file.getName();
    }
    
}
