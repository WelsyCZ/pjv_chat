package chitchat.Message;

import java.io.Serializable;
/**
 * Regular text message that is displayed in the application
 * @author milan
 */
public class TextMessage implements Message, Serializable
{
    private final String content;
    private final String username;
    private final MessageType type;

    /**
     * constructor
     * @param content (String) text message to be sent
     * @param username author of the message
     */
    public TextMessage(String content, String username){
        this.content = content;
        this.username = username;
        this.type = MessageType.TEXT;
    }
    
    /**
     * type getter
     * @return message type
     */
    public MessageType getType() {
        return type;
    }
    /**
     * Getter for the actual String - text message
     * @return String content of the message
     */
    public String getContent()
    {
        return content;
    }
    /**
     * username getter  
     * @return username
     */
    public String getUsername()
    {
        return username;
    }
}
