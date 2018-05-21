package chitchat.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
    
    void setApp(MainLauncher ml){
        this.app = ml;
    }
    
    @FXML protected void connectButtonAction(ActionEvent e) throws IOException{
       actiontarget.setText("Connecting...");
       app.userLogging(username.getText());
       //Parent mainChatWindow = FXMLLoader.load(getClass().getResource("/fxml/ChatView.fxml"));
       
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setPromptText("Username");
    }    
    
   
    
    
    
}
