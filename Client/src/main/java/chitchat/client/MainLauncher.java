package chitchat.client;

import java.io.IOException;
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


public class MainLauncher extends Application {
    private Group root = new Group();
    private Stage mainStage;
    private Logger logger =  Logger.getLogger(getClass().getName());
    private ChatWorker chatWorker;
    private Thread chatWorkerThread;

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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    private Parent makeContent(){
        goToLogin();
        return this.root;
    }
    
    void userLogging(String s, ChatWorker chatWorker) {
        logger.info("User "+s+" has just logged in");
        this.chatWorker = chatWorker;
        goToChat();
    }
    
    private void goToLogin(){
        try {
            mainStage.hide();
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
    
    private void goToChat(){
        try {
            mainStage.hide();
            
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
    
    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        //GridPane page = loader.load(getClass().getResource(fxml));
        
        InputStream in = MainLauncher.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainLauncher.class.getResource(fxml));
        GridPane page;
        
        try {
            page = (GridPane) loader.load(in);
        } finally {
            in.close();
        }
        
        root.getChildren().clear();
        root.getChildren().addAll(page);
        
        return (Initializable) loader.getController();
    }
}
