package chitchat.client;

import chitchat.Message.FileMessage;
import chitchat.Message.Message;
import chitchat.Message.StatusMessage;
import chitchat.Message.StatusUpdateMessage;
import chitchat.Message.TextMessage;
import chitchat.User.Status;
import chitchat.User.User;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;

/**
 * Chat FXML Controller class
 * this class joins the ChatView.fxml together with the Application
 * @author welsemil
 */
public class ChatViewController implements Initializable {

    
    @FXML private TextArea messagesArea; 
    @FXML private TextArea messageBox;   
    @FXML private GridPane chatGrid;
    @FXML private TextArea onlineArea;
    @FXML private Label uname;
    private ChatWorker chatWorker;
    private MainLauncher app;
    private boolean ready = false;
    // window parameters
    final int WIDTH = 560; 
    final int HEIGHT = 600;
    private User[] users;
    Logger logger = Logger.getLogger(getClass().getName());
    
    // MainLauncher (the app) visitor
    void setApp(MainLauncher ml){
        this.app = ml;
    }
    // set constraints for columns and rows in the GridPane
    void setConstraints() {
        chatGrid.getColumnConstraints().add(new ColumnConstraints(120));
        chatGrid.getRowConstraints().add(new RowConstraints(430));
    }
    
    //EventHandler for the "Send" button
    //checks for key phrases to change user status (bugged)
    @FXML private void sendPressed(ActionEvent e){
        
        try {
            String usname = chatWorker.getUsername();
            String msgText = messageBox.getText();
            if(msgText.equals("")) return; // Dont do anything if messagebox is empty
            Message msg;
            if(msgText.equals("/AFK") || msgText.equals("/afk")) {
                msg = new StatusMessage(chatWorker.getUsername(), Status.AWAY);
            } else if(msgText.equals("/back") || msgText.equals("/BACK")) {
                msg = new StatusMessage(chatWorker.getUsername(), Status.ONLINE);
            } else {
                msg = new TextMessage(msgText, usname);
            }
            chatWorker.sendMessage(msg);
            messageBox.clear();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to send message", ex);
        }
        
    }
    // Send file button handler, triggers the FileChooser
    // Filesending not implemented
    @FXML private void sendFilePressed(ActionEvent e){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(app.getMainStage());
        if(file != null) {
            Message msg = new FileMessage(chatWorker.getUsername(), true);
            try {
                chatWorker.sendMessage(msg);
                chatWorker.wait();
            } catch (IOException ex) {
                logger.warning("failed sending a file send request");
            } catch (InterruptedException ex) {
                logger.log(Level.WARNING, "interrupt", ex);
            }
        }
    }
    
    /**
     * Initializes the controller class.
     * called after all elements from the .fxml file have been properly loaded
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        messagesArea.setEditable(false);
        messagesArea.setFocusTraversable(false); // dont focus with tab
        messagesArea.setWrapText(true);
        messageBox.setWrapText(true);
        onlineArea.setEditable(false);
        onlineArea.setFocusTraversable(false);
        ready = true;
        
    }    
    
    // ChatWorker visitor - to get the chatWorker reference
    void visit(ChatWorker chatWorker){
        this.chatWorker = chatWorker;
    }
    
    // setter for your username label topleft of the chat window
    void setUnameLabel(String s){
        uname.setText(s);
    }
    // the main message parser, executes operations for the GUI chat window
    // shows messages, online users
    void addToChat(Message msg){
        switch(msg.getType()){
            case TEXT:
                msg = (TextMessage)msg;
                String line = msg.getUsername() + ": " + msg.getContent() + "\n";
                messagesArea.appendText(line);
                break;
            case STATUS:
                System.out.println("status");
                StatusUpdateMessage sumsg = (StatusUpdateMessage) msg;
                users = null;
                users = sumsg.getUsers().clone();
                listOnline(users);
                break;
            default:
                logger.fine("nothing to addtochat");
        }
    }
    // to return to the login screen
    private void logout(){
        try{
            chatWorker.kill(); //stop the worker thread
            //show the login screen, false flag indicates no error
            chatWorker.loginController.logoutScene(false); 
        } catch (Exception e) {
            logger.warning("error");
        }
    }
    //enters the online users in the textarea on top left
    private void listOnline(User[] users){
        while(!this.ready) continue; //wait until the class is loaded up
        onlineArea.clear(); 
        String line;
        for(User user: users){
            if(user == null) continue;
            // <username> - <userStatus>
            line = user.getName() + " - " + user.getStatus() + "\n";
            onlineArea.appendText(line);
        }
    }
    
}
