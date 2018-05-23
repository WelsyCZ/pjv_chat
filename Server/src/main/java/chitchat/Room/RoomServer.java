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

public class RoomServer
{
    private static final int CAPACITY = 10;
    private static final int PORT = 44444;
    private static final int TIMEOUT = 5000;
    private boolean running = true;
    private ServerSocket serverSocket;
    private Thread[] clientThreads;
    HashMap<String, User> users;
    //private ArrayList<ObjectOutputStream> outputs;
    HashMap<String, ObjectOutputStream> outputs;

    private static Logger logger = Logger.getLogger(RoomServer.class.getName());

    RoomServer()
    {
        logger.setLevel(Level.INFO);
        //outputs = new ArrayList<ObjectOutputStream>(CAPACITY);
        outputs = new HashMap<String, ObjectOutputStream>(CAPACITY);
        users = new HashMap<String, User>(CAPACITY);
    }

    public static void main(String[] args)
    {
        RoomServer srv = new RoomServer();
        srv.runServer();
    }

    void addOutput(String username, ObjectOutputStream output){
        outputs.put(username, output);
    }
    
    void broadcastMessage(Message msg) throws IOException {
        if(msg.getType() == MessageType.DISCONNECT){
            users.remove(msg.getUsername());
            outputs.remove(msg.getUsername());
            logger.info(msg.getUsername()+ " has disconnected.");
        } else{
            logger.info("Attempting to broadcast msg: "+msg.getContent());
            for(final ObjectOutputStream output : outputs.values()) {
                output.flush();
                System.out.println("broadcast size: "+((StatusUpdateMessage) msg).getUsers().values().size());
                output.writeObject(msg);
                
            }
        }
    }
    
    public boolean addUser(String username){
        if(users.containsKey(username)) 
            return false;
        User user = new User(username, Status.ONLINE);
        users.put(username, user);
        return true;
    }
    
    public boolean isUp()
    {
        return running;
    }

    public void kill()
    {
        this.running = false;
    }
    
    public void sendStatusMessage(){
        try{
            Message msg = new StatusUpdateMessage(users);
            //System.out.println("sendStatusMessage - size: "+((StatusUpdateMessage)msg).getUsers().values().size());
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
