/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.client;

import java.net.URL;
import java.util.ResourceBundle;
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
    private MainLauncher app;
    final int WIDTH = 540;
    final int HEIGHT = 600;
    
    
    void setApp(MainLauncher ml){
        this.app = ml;
    }
    
    void setConstraints() {
        chatGrid.getColumnConstraints().add(new ColumnConstraints(100));
        chatGrid.getRowConstraints().add(new RowConstraints(430));
    }
    
    @FXML private void sendPressed(ActionEvent e){
        messagesArea.appendText(messageBox.getText()+"\n");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        messagesArea.setEditable(false);
    }    
    
}
