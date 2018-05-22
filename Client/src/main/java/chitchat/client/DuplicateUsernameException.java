/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat.client;

/**
 *
 * @author milan
 */
public class DuplicateUsernameException extends Exception{

    public DuplicateUsernameException() {
    }

    public DuplicateUsernameException(String message) {
        super(message);
    }
    
}
