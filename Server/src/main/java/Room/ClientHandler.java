package Room;

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

    public void run()
    {
        try
        {
            os = clientSocket.getOutputStream();
            OutputStreamWriter out = new OutputStreamWriter(os);
            InputStream is = clientSocket.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            out.write("You: ");
            while (roomServer.isUp() && clientSocket.isConnected())
            {
                out.write("\nYou: ");
                line = in.readLine();
                //System.out.println("Client: "+line);

                out.flush();
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
