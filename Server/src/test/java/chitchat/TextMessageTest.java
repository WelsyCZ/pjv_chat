/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chitchat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import chitchat.Message.TextMessage;
import chitchat.Message.MessageType;
/**
 *
 * @author milan
 */
public class TextMessageTest {
    
    TextMessage txt;
    
    public TextMessageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        txt = new TextMessage("content", "username");
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetContent(){
        assertEquals("content", txt.getContent());
    }
    
    @Test
    public void testGetUsername(){
        assertEquals("username", txt.getUsername());
    }
    
    @Test
    public void testGetType(){
        assertEquals(MessageType.TEXT, txt.getType());
    }
}
