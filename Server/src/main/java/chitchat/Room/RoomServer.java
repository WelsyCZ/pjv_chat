package chitchat.Room;

import chitchat.Message.Message;
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
    private HashMap<String, User> users;
    private ArrayList<ObjectOutputStream> outputs;

    private static Logger logger = Logger.getLogger(RoomServer.class.getName());

    RoomServer()
    {
        logger.setLevel(Level.INFO);
        outputs = new ArrayList<ObjectOutputStream>(CAPACITY);
        users = new HashMap<String, User>(CAPACITY);
    }

    public static void main(String[] args)
    {
        RoomServer srv = new RoomServer();
        srv.runServer();
    }

    void broadcastMessage(Message msg) throws IOException {
        for(final ObjectOutputStream output : outputs) {
            output.writeObject(msg);
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
