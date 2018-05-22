/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.client;

import chitchat.Message.TextMessage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

/**
 * FXML Controller class
 *
 * @author milan
 */
public class ChatViewController implements Initializable {

    
    @FXML private TextArea messagesArea;
    @FXML private TextArea messageBox;
    @FXML private GridPane chatGrid;
    private ChatWorker chatWorker;
    private MainLauncher app;
    final int WIDTH = 540;
    final int HEIGHT = 600;
    Logger logger = Logger.getLogger(getClass().getName());
    
    
    void setApp(MainLauncher ml){
        this.app = ml;
    }
    
    void setConstraints() {
        chatGrid.getColumnConstraints().add(new ColumnConstraints(100));
        chatGrid.getRowConstraints().add(new RowConstraints(430));
    }
    
    @FXML private void sendPressed(ActionEvent e){
        //messagesArea.appendText("User: "+messageBox.getText()+"\n");
        
        try {
            String uname = chatWorker.getUsername();
            String msgText = messageBox.getText();
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        messagesArea.setEditable(false);
        messagesArea.setFocusTraversable(false);
    }    
    
    public void visit(ChatWorker chatWorker){
        this.chatWorker = chatWorker;
    }
}
