package chitchat.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * This class joins the Login part of the app with the FXML elements
 * @author welsemil
 */
public class LoginFXMLController implements Initializable 
{
    @FXML private TextField username;
    //window parameters
    final int WIDTH = 250;
    final int HEIGHT = 180;
    private MainLauncher app;
    private ChatWorker chatWorker;
    
    //app reference setter
    void setApp(MainLauncher ml){
        this.app = ml;
    }
    //app reference getter
    MainLauncher getApp(){
        return app;
    }
    
    // the connect button, also known as "Sign in" button
    @FXML protected void connectButtonAction(ActionEvent e) throws IOException{
        String uname = username.getText(); //get the username from the textfield
        // setup our worker on an adress and a port, with our username
        this.chatWorker = new ChatWorker(uname, "localhost", 44444);
        chatWorker.setLoginController(this);
        Thread chatWorkerThread = new Thread(chatWorker);
        app.setChatWorkerThread(chatWorkerThread); //setup and launch the worker thread
        chatWorkerThread.start();
        app.userLogging(uname, chatWorker); //log the user in
        
       
       
    }
    /**
     * Called by JavaFX when all the FXML elements were loaded
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setPromptText("Username");
        username.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                    try{
                        connectButtonAction(null);
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                ke.consume();
            }
        });
    }    
    
    /**
     * This is the actual logout method
     * @param duplicate - duplicate username exception flag, true if this was invoked by a duplicate username
     */
    public void logoutScene(boolean duplicate) {
        // setup a task to be performed as soon as possible
        Platform.runLater(() -> {
            // load up the login scene
            FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Parent window = null;
            try {
                window = (Pane) fmxlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // let the user know to use a different username
            // if he used a duplicate last time
            if(duplicate){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Duplicate username");
                alert.setContentText("A user has already logged in with that name, please choose a different one");
                alert.showAndWait();
            }
            // setup the stage, scene
            Stage stage = app.getMainStage();
            javafx.scene.Scene scene = new Scene(window);
            stage.setMaxWidth(250);
            stage.setMaxHeight(180);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.centerOnScreen();
            
        });
    }
    
    
    
}
