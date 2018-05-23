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
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * Essentially a background listener for receiving messages
 * runs in its own thread
 * used for certain utilities
 * @author welsemil
 */
public class ChatWorker implements Runnable {
    
    private Socket socket;
    private final String username;
    private final String hostname;
    private final int port;
    private ChatViewController chatController;
    LoginFXMLController loginController; //package access because of logout
    private ObjectOutputStream output;
    private OutputStream os;
    private InputStream is;
    private ObjectInputStream input;
    private boolean noKill = true;
    Logger logger = Logger.getLogger(getClass().getName());

    /**
     * Constructor
     * @param username Master's name
     * @param hostname the name/adress on which we are listening
     * @param port the port on which we are listening
     */
    public ChatWorker(String username, String hostname, int port) {
        this.username = username;
        this.hostname = hostname;
        this.port = port;
    }
    //for getting the instance of ChatController (FXML)
    void setChatController(ChatViewController chatController) {
        this.chatController = chatController;
        chatController.visit(this);
        chatController.setUnameLabel("You: "+username);
    }
    //for getting the logincontroller instance (FXML)
    void setLoginController(LoginFXMLController loginController){
        this.loginController = loginController;
    }
    
    /**
     * shuts down the thread and disconnects
     * @throws IOException 
     */
    public void kill() throws IOException{
        noKill = false;
        Message msg = new DisconnectMessage(username); //goodbye message
        output.writeObject(msg); //send the message
        input.close();
        Platform.exit();
    }
    
    /**
     * The thread method, this method runs in a separate thread
     * maintains connection with server, receives messages,
     * calls appropriate methods
     */
    @Override
    public void run() {
        try{
            // we use ObjectStreams for communication as we use Serializable Messages
            socket = new Socket(hostname, port);
            os = socket.getOutputStream();
            output = new ObjectOutputStream(os);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
            
        } catch (IOException e) {
            logger.log(Level.SEVERE, "error connecting to server", e);
        }
    
        try{
            connect(); // handshake with the server
            logger.log(Level.INFO, "Socket in and ready");
            // the main listening loop
            while(noKill && socket.isConnected()) {
                //read a message from the input stream
                //we're using the Message interface, polymorphism is cool
                Message rcvMsg = (Message) input.readObject();
                while(chatController == null) continue; // wait for the loadup
                // special behavior for file messages, sadly not implemented
                if(rcvMsg.getType() == MessageType.FILE){
                   fileTransfer((FileMessage) rcvMsg);
                } else {
                    // call controller method to exectude required actions
                    chatController.addToChat(rcvMsg);
                }
            }
        } catch (SocketTimeoutException se) {
            logger.fine("socket timeout");
        } catch (IOException ioe) {
            logger.info("Disconnected.");
        } catch (DuplicateUsernameException due) { 
            logger.log(Level.WARNING, "Login attempt failed, username already exists!", due);
            loginController.logoutScene(true);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "ClassNotFound", e);
        }
        try{
            // close all the streams
        input.close();
        output.close();
        os.close();
        is.close();
        socket.close();
        } catch(Exception e){
            logger.log(Level.WARNING, "closing issue", e);
        }
    
    }
    // The handshake method
    // gives server the username, server responds whether its avaible
    // uses CONNECT and CONFIRM messages
    private void connect() throws IOException, ClassNotFoundException, DuplicateUsernameException {
        ConnectMessage msg = new ConnectMessage(username);
        sendMessage(msg);
        ConfirmMessage rcvMsg = (ConfirmMessage) input.readObject();
        if(!rcvMsg.getUsernameAvailable())
            throw new DuplicateUsernameException();
        
    }
    /**
     * API for sending a message
     * @param msg any message object to be sent
     * @throws IOException 
     */
    public void sendMessage(Message msg) throws IOException {
        output.writeObject(msg);
    }
    // to handle the fileTransfer, not implemented
    private void fileTransfer(FileMessage msg){
        Runnable rnb = new Runnable(){
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Incoming file transfer");
                alert.setHeaderText("User "+msg.getUsername()+" has sent a file" );
                alert.setContentText("Do you want to save it?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK){
                    chatController.saveFile(msg);
                }
            }
            
        };
        Platform.runLater(rnb);
    }
    
    
    /**
     * username getter
     * @return username
     */
    public String getUsername() {
        return username;
    }
    
    
    
}
