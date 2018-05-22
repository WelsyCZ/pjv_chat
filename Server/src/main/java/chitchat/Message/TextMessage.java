package chitchat.Message;

import java.io.Serializable;

public class TextMessage implements Message, Serializable
{
    private final String content;
    private final String username;
    private final MessageType type;

    public TextMessage(String content, String username){
        this.content = content;
        this.username = username;
        this.type = MessageType.TEXT;
    }
    
    public String getContent()
    {
        return content;
    }

    public String getUsername()
    {
        return username;
    }
    
    @Override
    public MessageType getType() {
        return type;
    }
    
    
}
