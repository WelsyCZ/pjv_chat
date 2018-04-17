package cz.cvut.fel.pjv;

/**
 * Client class - every user is an instance of Client class
 * communicates with server through a TCP connection
 * works with a ClientGUI class
 */

public class Client
{
    public String name; //(nick) name of the client
    private ClientGUI gui;
    private Server server;
    /* attributes I can't specify yet:
    ADDRESS - address of the client
    SOCKET - the socket that the connection is handled through
    */

    /**
     * Constructor
     * @param name is the name of the client
     */
    public Client(String name)

    /**
     * Initializes the connection to the server
     * This will be called right after or even within the constructor
     * @return true on success, false otherwise
     */
    private boolean initConnection()

    /**
     * Terminates the connection to the server
     * Called on exit
     * @return true on success, false otherwise
     */
    private boolean endConnection()

    /**
     * Creates a Message object from given String
     * adds author and time
     * gets called by hitting the "Send" button in the app
     * @param content is the text of the message
     * @return
     */
    private Message completeMessage(String content)

    /**
     * Sends the message to the server
     * This function should handle the network part
     * @param message is the Message object to be sent
     * @return true on success, false otherwise
     */
    private boolean sendMessage(Message message)

    /**
     * Keeps 'listening' for any information from the server,
     * mostly new messages
     * Should run on it's own thread, endlessly, until connection is terminated
     */
    private void listen()

    /**
     * Calls to a GUI (or a terminal) to handle displaying the message in the chat window
     * @param message is the Message object to be displayed
     * @return true on success, false otherwise
     */
    private boolean displayMessage(Message message)



}
