/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.client;

import chitchat.Message.ConfirmMessage;
import chitchat.Message.ConnectMessage;
import chitchat.Message.Message;
import chitchat.Message.StatusUpdateMessage;
import chitchat.Message.TextMessage;
import chitchat.User.User;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author milan
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
    final int WIDTH = 540;
    final int HEIGHT = 600;
    private ArrayList<User> users = new ArrayList<>();
    Logger logger = Logger.getLogger(getClass().getName());
    
    
    void setApp(MainLauncher ml){
        this.app = ml;
    }
    
    void setConstraints() {
        chatGrid.getColumnConstraints().add(new ColumnConstraints(100));
        chatGrid.getRowConstraints().add(new RowConstraints(430));
    }
    
    @FXML private void sendPressed(ActionEvent e){
        
        try {
            String uname = chatWorker.getUsername();
            String msgText = messageBox.getText();
            if(msgText.equals("")) return;
            if(uname == null) System.out.println("uname null");
            if(msgText == null) System.out.println("msgText null");
            //TextMessage msg = new TextMessage(messageBox.getText(), chatWorker.getUsername());
            TextMessage msg = new TextMessage(msgText, uname);
            chatWorker.sendMessage(msg);
            messageBox.clear();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Failed to send message", ex);
        }
        
    }
    
    @FXML private void sendFilePressed(ActionEvent e){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(app.getMainStage());
        if(file != null) {
            
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        messagesArea.setEditable(false);
        messagesArea.setFocusTraversable(false);
        messagesArea.setWrapText(true);
        messageBox.setWrapText(true);
        onlineArea.setEditable(false);
        onlineArea.setFocusTraversable(false);
        uname.setText("You: ");
        ready = true;
        
    }    
    
    public void visit(ChatWorker chatWorker){
        this.chatWorker = chatWorker;
    }
    
    public void addToChat(Message msg){
        switch(msg.getType()){
            case TEXT:
                msg = (TextMessage)msg;
                String line = msg.getUsername() + ": " + msg.getContent() + "\n";
                messagesArea.appendText(line);
                break;
            case CONFIRM:
                System.out.println("confirm");
                break;
            case CONNECT:
                System.out.println("connect");
                break;
            case STATUS:
                System.out.println("status");
                StatusUpdateMessage sumsg = (StatusUpdateMessage) msg;
                users.clear();
                users.addAll(sumsg.getUsers().values());
                System.out.println("users size "+users.size());
                listOnline(users);
                break;
            default:
                System.out.println("something effed up");
        }
    }
    
    private void listOnline(ArrayList<User> users){
        while(!this.ready) continue;
        System.out.println("inside listOnline");
        onlineArea.clear();
        String line;
        for(User user: users){
            System.out.println("loop");
            line = user.getName() + " - " + user.getStatus() + "\n";
            onlineArea.appendText(line);
        }
    }
    
}
