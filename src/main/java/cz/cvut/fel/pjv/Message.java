package cz.cvut.fel.pjv;

import java.util.Date;

/**
 * Message class represents the actual message
 * This object will travel between the server - client - gui
 */
public class Message
{
    private String content; // the text content
    private Date time;      // time when message was sent
    private Client author;  // who sent the message

    /**
     * Constructor, sets the content and author
     * gets and sets the current time
     * @param content is the text (String) content of the message
     * @param author is the author of the message (who sent the message)
     */
    public Message(String content, Client author)
    {}

    public String getContent()
    {
        return content;
    }

    public Client getAuthor()
    {
        return author;
    }
}
