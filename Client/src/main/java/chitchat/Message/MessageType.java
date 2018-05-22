package chitchat.Message;

/**
 * TEXT - Text Message type - String content
 * STATUS - Status Message type - Status (int) content
 * CONNECT - Connect Message type - no content, only for login
 * @author milan
 */
public enum MessageType
{
    TEXT, STATUS, CONNECT, CONFIRM, DISCONNECT
}
