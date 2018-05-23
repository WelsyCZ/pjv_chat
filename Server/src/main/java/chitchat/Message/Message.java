package chitchat.Message;

/**
 * The baseline for all messages
 * @author welsemil
 */
public interface Message
{  
/**
 * type getter
 * @return type
 */
    public MessageType getType();
    /**
     * username getter
     * @return username
     */
    public String getUsername();
    /**
     * Description getter
     * @return  short description
     */
    public String getContent();
}
