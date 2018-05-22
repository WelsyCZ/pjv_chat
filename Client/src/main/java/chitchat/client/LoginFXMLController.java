package chitchat.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class LoginFXMLController implements Initializable 
{
    @FXML private Text actiontarget;
    @FXML private GridPane loginGrid;
    @FXML private TextField username;
    final int WIDTH = 250;
    final int HEIGHT = 180;
    private MainLauncher app;
    private ChatWorker chatWorker;
    
    void setApp(MainLauncher ml){
        this.app = ml;
    }
    
    @FXML protected void connectButtonAction(ActionEvent e) throws IOException{
        String uname = username.getText();
        this.chatWorker = new ChatWorker(uname, "localhost", 44444);
        chatWorker.setLoginController(this);
        Thread chatWorkerThread = new Thread(chatWorker);
        app.setChatWorkerThread(chatWorkerThread);
        chatWorkerThread.start();
        actiontarget.setText("Connecting...");
        app.userLogging(uname, chatWorker);
        
       //Parent mainChatWindow = FXMLLoader.load(getClass().getResource("/fxml/ChatView.fxml"));
       
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setPromptText("Username");
    }    
    
   
    
    
    
}
