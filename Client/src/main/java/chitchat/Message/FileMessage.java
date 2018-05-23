/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.Message;

import java.io.Serializable;

/**
 * File transfer class
 * @author welsemil
 */
public class FileMessage implements Message, Serializable {

    private MessageType type;
    private String username;
    private byte[] data;
    private String filename;
    
    /**
     * constructor
     * @param username senders username
     * @param data transferred file as bytes
     * @param filename 
     */
    public FileMessage(String username, byte[] data, String filename){
        this.username = username;
        this.data = data;
        this.filename = filename;
        type = MessageType.FILE;
    }
    /**
     * filename getter
     * @return String Filename
     */
    public String getFilename() {
        return filename;
    }
    /**
     * file as bytes getter
     * @return byte[] file
     */
    public byte[] getData() {
        return data;
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
