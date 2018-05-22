/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.client;

import chitchat.Message.*;
import chitchat.User.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

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
    private boolean noKill = true;
    private HashMap<String, User> users;
    Logger logger = Logger.getLogger(getClass().getName());

    public ChatWorker(String username, String hostname, int port) {
        this.username = username;
        this.hostname = hostname;
        this.port = port;
        this.users = new HashMap<String, User>();
    }

    void setChatController(ChatViewController chatController) {
        this.chatController = chatController;
        chatController.visit(this);
    }
    
    void setLoginController(LoginFXMLController loginController){
        this.loginController = loginController;
    }
    
    public void kill() throws IOException{
        noKill = false;
        Message msg = new DisconnectMessage(username);
        output.writeObject(msg);
        input.close();
        Platform.exit();
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
            Message rcvMsg;
            logger.log(Level.INFO, "Socket in and ready");
            while(noKill && socket.isConnected()) {
                rcvMsg = (Message) input.readObject();
                chatController.addToChat(rcvMsg);
            }
        } catch (SocketTimeoutException se) {
            logger.fine("socket timeout");
        } catch (IOException ioe) {
            logger.info("Disconnected.");
            //logger.log(Level.WARNING, "IOE", ioe);
        } catch (DuplicateUsernameException due) { 
            logger.log(Level.WARNING, "Login attempt failed, username already exists!", due);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "ClassNotFound", e);
        }
        try{
        input.close();
        output.close();
        os.close();
        is.close();
        socket.close();
        } catch(Exception e){
            logger.log(Level.WARNING, "closing issue", e);
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
