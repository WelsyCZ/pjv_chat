package chitchat.Room;

import chitchat.Message.Message;
import chitchat.Message.MessageType;
import chitchat.Message.StatusUpdateMessage;
import chitchat.User.Status;
import chitchat.User.User;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server class for one chatroom
 * @author welsemil
 */
public class RoomServer
{
    private static final int CAPACITY = 10;
    private static final int PORT = 44444;
    private static final int TIMEOUT = 5000;
    private boolean running = true;
    private ServerSocket serverSocket;
    private Thread[] clientThreads;
    HashMap<String, User> users; //dictionairy to get user objects from strings
    HashMap<String, ObjectOutputStream> outputs; //dictionary to get specific output streams if necessary

    private static Logger logger = Logger.getLogger(RoomServer.class.getName());

    /* NOT PUBLIC
     * Very basic constructor, only initialize collections and logger
     */
    RoomServer()
    {
        logger.setLevel(Level.INFO);
        outputs = new HashMap<String, ObjectOutputStream>(CAPACITY);
        users = new HashMap<String, User>(CAPACITY);
    }
    /**
     * This is the Server Launcher, main function calls the package private constructor and starts the server
     * @param args 
     */
    public static void main(String[] args)
    {
        RoomServer srv = new RoomServer();
        srv.runServer();
    }
    
    //add a String username : output stream pair to our dictionary
    void addOutput(String username, ObjectOutputStream output){
        outputs.put(username, output);
    }
    // Method to send a message to all connected clients
    // also parses disconnects
    void broadcastMessage(Message msg) throws IOException {
        if(msg.getType() == MessageType.DISCONNECT){
            users.remove(msg.getUsername());
            outputs.remove(msg.getUsername());
            logger.info(msg.getUsername()+ " has disconnected.");
            sendStatusMessage(); //update users of the disconnected client
        } else if(msg.getType() == MessageType.FILE){
            logger.info("Sending file");
            ObjectOutputStream authorStream = outputs.get(msg.getUsername());
            for (final ObjectOutputStream output : outputs.values()) {
                if(output == authorStream) continue;
                output.flush();
                output.writeObject(msg);
            }
        } else {
            logger.info("Attempting to broadcast msg: "+msg.getContent());
            
            for(final ObjectOutputStream output : outputs.values()) {
                output.flush();
                output.writeObject(msg);
                
            }
        }
    }
    /**
     * Adds a user if user with such username doesnt yet exist
     * @param username the desired username
     * @return true on success, false otherwise
     */
    public boolean addUser(String username){
        if(users.containsKey(username)) 
            return false;
        User user = new User(username, Status.ONLINE);
        users.put(username, user);
        return true;
    }
    
    /**
     * Method to change a status of a user (bugged)
     * @param username who's status we're changing
     * @param status the new status
     * - the changes go through fine, status gets updated, the right thing is put into the stream
     * but the status does not change on the client side
     */
    public void changeStatus(String username, Status status){
        users.get(username).setStatus(status);
        System.out.println("Changed status of "+username+" to "+users.get(username).getStatus());
        sendStatusMessage(); //update clients about the change
    }
    /**
     * Used to check on whether the server is running
     * @return true if running, false if not
     */
    public boolean isUp()
    {
        return running;
    }

    /**
     * Stop the server
     * this will stop the main loop, resulting in all the threads joining and exitting safely
     */
    public void kill()
    {
        this.running = false;
    }
    
    /**
     * Updating method, sends out the current list of connected users with their statuses
     */
    public void sendStatusMessage(){
        try{
            ArrayList<User> al = new ArrayList<User>(users.values());
            User[] usrs = new User[this.CAPACITY];
            al.toArray(usrs);
            Message msg = new StatusUpdateMessage(usrs);
            broadcastMessage(msg);
        } catch(IOException e) {
           logger.warning("failed to send status message");
           e.printStackTrace();
        }
    }

    private void runServer()
    {
        int clientsConnected = 0;
        try
        {
            serverSocket = new ServerSocket(PORT);
            logger.info("RoomServer is up and running.");
            clientThreads = new Thread[CAPACITY];
            serverSocket.setSoTimeout(TIMEOUT);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            while (this.running)
            {
                try
                {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Client connected");
                    if (clientsConnected < CAPACITY)
                    {
                        clientThreads[clientsConnected] = new ClientHandler(clientSocket, this);
                        clientThreads[clientsConnected++].start();
                        if(clientsConnected == CAPACITY) logger.warning("Room capacity reached!");
                    } else
                    {
                        logger.warning("Client failed to connect - room is full");
                    }
                } catch (SocketTimeoutException e)
                {
                    logger.fine("Waiting for a client timeout...");
                }

            }
            serverSocket.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        for (Thread t : clientThreads)
        {
            try
            {
                if (t != null)
                    t.join();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        logger.info("Room shut down successfully.");
    }
}
