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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class LoginFXMLController implements Initializable 
{
    @FXML private Text actiontarget;
    @FXML private TextField username;
    
    @FXML protected void connectButtonAction(ActionEvent e) throws IOException{
       actiontarget.setText("Connecting...");
       
       Parent mainChatWindow = FXMLLoader.load(getClass().getResource("/fxml/ChatView.fxml"));
       
       
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Done");
    }    
}
