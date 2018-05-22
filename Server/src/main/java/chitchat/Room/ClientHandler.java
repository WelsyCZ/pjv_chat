package chitchat.Room;

import chitchat.Message.ConfirmMessage;
import chitchat.Message.ConnectMessage;
import chitchat.Message.Message;
import chitchat.Message.TextMessage;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Logger;

public class ClientHandler extends Thread
{
    private Socket clientSocket;
    private RoomServer roomServer;
    private OutputStream os;
    private InputStream is;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Logger logger = Logger.getLogger(ClientHandler.class.getName());

    ClientHandler(Socket socket, RoomServer roomServer){
        this.clientSocket = socket;
        this.roomServer = roomServer;
    }
    
    public boolean authenticate() throws IOException, ClassNotFoundException {
        ConnectMessage first = (ConnectMessage) input.readObject(); 
        return roomServer.addUser(first.getUsername());
    }

    
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
            if(authenticate()){
                conf = new ConfirmMessage("SERVER", true);
            } else {
                conf = new ConfirmMessage("SERVER", false);
            }
            output.writeObject(conf);
            if(conf.getUsernameAvailable()) {
                Message receivedMsg;
                while (roomServer.isUp() && clientSocket.isConnected())
                {
                    try{
                        receivedMsg = (Message) input.readObject();
                        System.out.println( ((TextMessage) receivedMsg).getContent());
                        //roomServer.broadcastMessage(receivedMsg);
                    } catch (IOException e) {
                        logger.warning("error while broadcasting message");
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
