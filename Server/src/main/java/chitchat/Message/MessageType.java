package chitchat.Message;

/**
 * Enum for types of messages
 * TEXT - Text Message type - String content
 * STATUS - Status Message type - Status (int) content
 * CONNECT - Connect Message type - no content, only for login
 * CONFIRM - confirming availability of a username
 * DISCONNECT - the goodbye message sent before disconnecting
 * FILE - intended as a warning that a file stream is incoming
 * @author milan
 */
public enum MessageType
{
    TEXT, STATUS, CONNECT, CONFIRM, DISCONNECT, FILE
}
