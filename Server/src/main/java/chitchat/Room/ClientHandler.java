package chitchat.Room;

import chitchat.Message.ConfirmMessage;
import chitchat.Message.ConnectMessage;
import chitchat.Message.Message;
import chitchat.Message.MessageType;
import chitchat.Message.StatusMessage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;
/**
 * Handler for clients
 * every handler runs in its own thread
 * @author milan
 */
public class ClientHandler extends Thread
{
    private Socket clientSocket;
    private RoomServer roomServer;
    private OutputStream os;
    private InputStream is;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private String username;
    // constructor only callable by the server
    ClientHandler(Socket socket, RoomServer roomServer){
        this.clientSocket = socket;
        this.roomServer = roomServer;
    }
    /**
     * this method reads the first message and passes the usernaem authentication request to the server
     * @return true if username is available, false if not
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public boolean authenticate() throws IOException, ClassNotFoundException {
        ConnectMessage first = (ConnectMessage) input.readObject(); 
        username = first.getUsername();
        return roomServer.addUser(username);
    }

    
    /**
     * The Thread method
     * listents and retransmits almost all the messages
     */
    @Override
    public void run()
    {
        try
        {
            os = clientSocket.getOutputStream();
            output = new ObjectOutputStream(os);
            is = clientSocket.getInputStream();
            input = new ObjectInputStream(is);
            
            ConfirmMessage conf;
            if(authenticate()){ //accept/deny the request
                conf = new ConfirmMessage("SERVER", true);
            } else {
                conf = new ConfirmMessage("SERVER", false);
            }
            
            output.writeObject(conf);
            if(conf.getUsernameAvailable()) {
                roomServer.addOutput(username, output); //add new output stream to write to
                Message receivedMsg;
                roomServer.sendStatusMessage(); //update clients - new client
                while (roomServer.isUp() && clientSocket.isConnected()) // MAIN LOOP - Read message, broadcast message
                {
                    try{
                        receivedMsg = (Message) input.readObject();
                        if(receivedMsg.getType() == MessageType.STATUS){ //changeStatus messages do not get retransmitted
                            roomServer.changeStatus(receivedMsg.getUsername(), ((StatusMessage)receivedMsg).getStatus());
                            continue;
                        }
                        roomServer.broadcastMessage(receivedMsg); //send the message to all clients
                    } catch (IOException e) {
                        logger.info("Error in transmission - probably due to a disconnect");
                        roomServer.logServerStatus();
                        break;
                    }
                }
            }
        } catch (SocketException se)
        {
            logger.warning("Client Socket Exception - disconnect");
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally {
            try
            {
                clientSocket.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
