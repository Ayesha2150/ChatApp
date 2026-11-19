/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @BeforeEach
    public void setUp() {
        Message.sentMessages = new String[100];
        Message.disregardedMessages = new String[100];
        Message.storedMessages = new String[100];
        Message.messageHashes = new String[100];
        Message.messageIDs = new String[100];
        Message.sentCount = 0;
        Message.disregardedCount = 0;
        Message.storedCount = 0;
    }

    @Test
    public void testMessageIDIsTenDigits() {
        Message message = new Message();
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    @Test
    public void testValidRecipientNumber() {
        Message message = new Message("+27718693002", "Hi tonight", 1);
        assertEquals("Cell phone number successfully captured.", message.checkRecipientCell());
    }

    @Test
    public void testInvalidRecipientNumber() {
        Message message = new Message("0812345678", "Hi tonight", 1);
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code.", message.checkRecipientCell());
    }

    @Test
    public void testMessageLength() {
        Message message = new Message("+27718693002", "Hi tonight", 1);
        assertEquals("Message ready to send.", message.checkMessageLength());
    }

    @Test
    public void testMessageExceeds250Characters() {
        String longMessage = "A".repeat(251);
        Message message = new Message("+27718693002", longMessage, 1);
        assertEquals("Message exceeds 250 characters by 1; please reduce the size.", message.checkMessageLength());
    }

    @Test
    public void testMessageHashFormat() {
        Message message = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?", 1);
        String hash = message.createMessageHash();
        assertTrue(hash.matches("\\d{2}:1:HITONIGHT"));
    }

    @Test
    public void testMessageHashLoop() {
        String[][] testData = {
            {"+27718693002", "Hi Mike, can you join us for dinner tonight?", "1", "HITONIGHT"},
            {"08575975889", "Hi Keegan, did you receive the payment?", "2", "HIPAYMENT"}
        };
        for (String[] data : testData) {
            Message msg = new Message(data[0], data[1], Integer.parseInt(data[2]));
            assertTrue(msg.createMessageHash().endsWith(data[3]));
        }
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        Message msg1 = new Message("+27834557896", "Did you get the cake?", 1);
        msg1.SentMessage(1);
        
        Message msg4 = new Message("0838884567", "It is dinner time !", 2);
        msg4.SentMessage(1);

        String[] expectedMessages = {"Did you get the cake?", "It is dinner time !"};
        String[] actualMessages = {Message.sentMessages[0], Message.sentMessages[1]};
        
        assertArrayEquals(expectedMessages, actualMessages);
    }

    @Test
    public void testDisplayLongestMessage() {
        Message.storedMessages[0] = "Did you get the cake?";
        Message.storedMessages[1] = "Where are you? You are late! I have asked you to be on time.";
        Message.storedMessages[2] = "Yohoooo, I am at your gate.";
        Message.storedCount = 3;

        String result = Message.displayLongestMessage();
        assertEquals("Longest Message: Where are you? You are late! I have asked you to be on time.", result);
    }

    @Test
    public void testSearchForMessageID() {
        Message msg = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", 1);
        msg.SentMessage(3);
        
        Message.messageIDs[0] = "0838884567"; 
        
        String result = Message.searchByMessageID("0838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
    }

    @Test
    public void testSearchByRecipient() {
        Message msg1 = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", 1);
        msg1.SentMessage(3);
        
        Message msg2 = new Message("+27838884567", "Ok, I am leaving without you.", 2);
        msg2.SentMessage(3);

        String result = Message.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late!"));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        Message msg = new Message("+27838884567", "Where are you? You are late! I have asked you to be on time.", 1);
        msg.SentMessage(3);
        
        String hashToDelete = msg.getMessageHash();
        
        String result = Message.deleteMessageByHash(hashToDelete);
        assertTrue(result.contains("successfully deleted"));
        assertEquals(0, Message.storedCount);
    }

    @Test
    public void testDisplayReport() {
        Message msg = new Message("+27838884567", "It is dinner time !", 1);
        msg.SentMessage(3);

        String report = Message.displayReport();
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Message: It is dinner time !"));
    }
}