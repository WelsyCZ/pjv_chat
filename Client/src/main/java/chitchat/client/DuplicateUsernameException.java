package chitchat.client;

/**
 * Special exception, thrown when server responds,
 * that a username is already in use
 * @author welsemil
 */
public class DuplicateUsernameException extends Exception{

    public DuplicateUsernameException() {
    }

    public DuplicateUsernameException(String message) {
        super(message);
    }
    
}
