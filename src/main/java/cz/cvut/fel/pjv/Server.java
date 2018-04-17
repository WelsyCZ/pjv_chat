package cz.cvut.fel.pjv;
import java.util.List;
import java.util.Set;

/**
 * Singleton Server class
 * Handles all the clients and messages
 */


public class Server
{
    private static Server instance; // the only instance of the class
    private List<Client> clients;   // the list containing all connected clients

    /**
     * Constructor, very basic
     * private - class is a singleton
     */
    private Server()
    {}

    /**
     * automatic creation of the only class instance
     */
    static
    {
        instance = new Server();
    }

    /**
     * Used to get the instance
     * use instead of constructor
     * @return the only Server class instance
     */
    public static Server getInstance()
    {
        return instance;
    }

    /**
     * Listen to any incoming new connections or messages
     * should run on a separate thread, indefinitely, until shut down
     */
    private void listen()

    /**
     * Function to handle any special requirements about the message
     * (e.g. could be checked for bad words)
     * Just to make it more complex, otherwise this would be a fancy echo server
     * Gets called from the listen() function
     * @param message is the just received Message object
     * @return the (perhaps changed?) Message object we just received
     */
    private Message checkMessage(Message message)

    /**
     * Broadcasts (sends) the message to all connected clients
     * Should be called right after the checkMessage() function
     * @param message is the Message object to be sent
     * @return the number of successful deliveries of the message (max is number of connected clients)
     */
    private int broadcastMessage(Message message)

    /**
     * Handshake to confirm ending the connection with the client
     * Termination is usually initiated from the client side
     * @param client to end the connection with
     * @return true on success, false otherwise
     */
    private boolean endConnection(Client client)

    /**
     * Get ready to shut down the server
     * Disconnect all clients
     * @return true on success, false otherwise
     */
    private boolean shutdown()
}
