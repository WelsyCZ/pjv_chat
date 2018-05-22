package chitchat.Message;

public interface Message
{
    public MessageType getType();
    public String getUsername();
    public String getContent();
}
