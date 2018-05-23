package chitchat.client;

import chitchat.Message.FileMessage;
import chitchat.Message.Message;
import chitchat.Message.StatusMessage;
import chitchat.Message.StatusUpdateMessage;
import chitchat.Message.TextMessage;
import chitchat.User.Status;
import chitchat.User.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import java.nio.file.Files;
import java.nio.file.Path;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;

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
            } else if(msgText.equals("/help") || msgText.equals("/HELP")){
                displayHelpMessage();
                messageBox.clear();
                return;
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
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                Path path = file.toPath();
                byte[] data = Files.readAllBytes(path);
                Message msg = new FileMessage(chatWorker.getUsername(), data, file.getName());
                chatWorker.sendMessage(msg);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex){
                Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OutOfMemoryError ex){
                Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, "File too big", ex);
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Too large file");
                alert.setContentText("The file you have chosen is too large!");
                alert.showAndWait();
            }
            
            
        }
    }
    
    void displayHelpMessage(){
        String help = "Help message - avaible commands:\n"
                    + "/afk or /AFK - sets your status to AWAY\n"
                    + "/back or /BACK - sets your status to ONLINE\n"
                    + "/help or /HELP - displays this message\n";
        messagesArea.appendText(help);
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
        messageBox.setPromptText("/help");
        onlineArea.setEditable(false);
        onlineArea.setFocusTraversable(false);
        messageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                    sendPressed(null);
                ke.consume();
            }
        });
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
    @FXML private void logout(ActionEvent e){
        try{
             
            //show the login screen, false flag indicates no error
            chatWorker.loginController.logoutScene(false); 
            //stop the worker thread
            chatWorker.kill();
        } catch (Exception ex) {
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
    
    void saveFile(FileMessage msg){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Where would you like to save the file?");
        File selectedDirectory = chooser.showDialog(app.getMainStage());
        File newFile = new File(selectedDirectory.getPath() + File.separator + msg.getFilename());
        
        try {
            if(!newFile.exists())
                newFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(msg.getData());
            fos.flush();
            fos.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(newFile.getPath());
            alert.setContentText("Your file has been saved!");
            alert.show();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, "file not found??", ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatViewController.class.getName()).log(Level.SEVERE, "IOexceptiono", ex);
        }
        
        
    }
    
}
