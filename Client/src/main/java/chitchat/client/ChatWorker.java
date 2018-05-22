/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.client;

import chitchat.Message.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author milan
 */
public class ChatWorker implements Runnable {
    
    private Socket socket;
    private final String username;
    private final String hostname;
    private final int port;
    private ChatViewController chatController;
    private LoginFXMLController loginController;
    private ObjectOutputStream output;
    private OutputStream os;
    private InputStream is;
    private ObjectInputStream input;
    Logger logger = Logger.getLogger(getClass().getName());

    public ChatWorker(String username, String hostname, int port) {
        this.username = username;
        this.hostname = hostname;
        this.port = port;
    }

    void setChatController(ChatViewController chatController) {
        this.chatController = chatController;
        chatController.visit(this);
    }
    
    void setLoginController(LoginFXMLController loginController){
        this.loginController = loginController;
    }
    
    
    
    @Override
    public void run() {
        try{
            socket = new Socket(hostname, port);
            os = socket.getOutputStream();
            output = new ObjectOutputStream(os);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "error connecting to server", e);
        }
    
        try{
            connect();
            logger.log(Level.INFO, "Socket in and ready");
            while(socket.isConnected()) {
                continue;
            }
        } catch (IOException ioe) {
            logger.log(Level.WARNING, "IOE", ioe);
        } catch (DuplicateUsernameException due) { 
            logger.log(Level.WARNING, "Login attempt failed, username already exists!", due);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "ClassNotFound", e);
        }
    
    
    }
    
    
    private void connect() throws IOException, ClassNotFoundException, DuplicateUsernameException {
        ConnectMessage msg = new ConnectMessage(username);
        sendMessage(msg);
        ConfirmMessage rcvMsg = (ConfirmMessage) input.readObject();
        if(!rcvMsg.getUsernameAvailable())
            throw new DuplicateUsernameException();
        
    }
    
    public void sendMessage(Message msg) throws IOException {
        switch(msg.getType()){
            case TEXT:
                msg = (TextMessage) msg;
                break;
            case CONFIRM:
                msg = (ConfirmMessage) msg;
                break;
            case CONNECT:
                msg = (ConnectMessage) msg;
                break;
            case STATUS:
                System.out.println("status not implemented yet");
                break;
            default:
                System.out.println("something effed up");
        }
        output.writeObject(msg);
    }

    public String getUsername() {
        return username;
    }
    
    
    
}
