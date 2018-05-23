package chitchat.client;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This is the Client launcher
 * The JavaFX basic Application method
 * @author milan
 */
public class MainLauncher extends Application {
    private Group root = new Group(); 
    private Stage mainStage;
    private Logger logger =  Logger.getLogger(getClass().getName());
    private ChatWorker chatWorker;
    private Thread chatWorkerThread;

    /**
     * setter for the worker reference
     * @param chatWorkerThread - the thread reference
     */
    public void setChatWorkerThread(Thread chatWorkerThread) {
        this.chatWorkerThread = chatWorkerThread;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        
        mainStage = stage;
        mainStage.setTitle("ChitChat Client");
        mainStage.setScene(new Scene(makeContent()));
        mainStage.show();
        mainStage.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                try{
                chatWorker.kill();
                Platform.exit();
                } catch(Exception e) {
                    logger.info(chatWorker.getUsername() + " is logging out");
                }
            }
            
        });
        
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * Note: calling main actually breaks the app
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * main stage getter
     * @return main stage object 
     */
    public Stage getMainStage(){
        return mainStage;
    }
    //method that gets the root node for the scene
    private Parent makeContent(){
        goToLogin();
        return this.root;
    }
    
    void userLogging(String s, ChatWorker chatWorker) {
        logger.info("User "+s+" has just logged in");
        this.chatWorker = chatWorker;
        goToChat();
    }
    
    // switch to login scene
    void goToLogin(){
        try {
            mainStage.hide();
            //setup FXML loader and mainStage
            LoginFXMLController login = (LoginFXMLController) replaceSceneContent("/fxml/LoginView.fxml");
            login.setApp(this);
            mainStage.setHeight(login.HEIGHT);
            mainStage.setWidth(login.WIDTH);
            mainStage.setResizable(false);
            mainStage.show();
        } catch(Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
    }
    // switch to the Chat scene
    private void goToChat(){
        try {
            mainStage.hide();
            //setup the FXML loader
            ChatViewController chat = (ChatViewController) replaceSceneContent("/fxml/ChatView.fxml");
            chat.setApp(this);
            chat.visit(this.chatWorker);
            chatWorker.setChatController(chat);
            mainStage.setHeight(chat.HEIGHT);
            mainStage.setWidth(chat.WIDTH);
            chat.setConstraints();
            mainStage.setResizable(false);
            mainStage.show();
        } catch(Exception e) {
            logger.log(Level.SEVERE, null, e);
        }
    }
    // method that removes all the children notes from Root node and replaces them
    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        //complicated FXMLLoader setup because the simple one doesn't seem to work
        InputStream in = MainLauncher.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainLauncher.class.getResource(fxml));
        GridPane page;
        
        try {
            //loadup the stage content
            page = (GridPane) loader.load(in);
        } finally {
            in.close();
        }
        //add it to the stage
        root.getChildren().clear();
        root.getChildren().addAll(page);
        
        return (Initializable) loader.getController();
    }
}
