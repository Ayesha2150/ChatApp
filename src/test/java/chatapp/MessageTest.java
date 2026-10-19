/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageIDIsTenDigits() {
        Message message = new Message();
        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    @Test
    public void testValidRecipientNumber() {
        Message message = new Message(
                "+27718693002", "Hi tonight", 1
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testInvalidRecipientNumber() {
        Message message = new Message(
                "0812345678", "Hi tonight", 1
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testValidMessageLength() {
        Message message = new Message(
                "+27718693002", "Hi tonight", 1
        );

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength()
        );
    }

    @Test
    public void testMessageExceeds250Characters() {
        String longMessage = "A".repeat(251);

        Message message = new Message(
                "+27718693002", longMessage, 1
        );

        assertEquals(
                "Message exceeds 250 characters by 1; please reduce the size.",
                message.checkMessageLength()
        );
    }

    @Test
    public void testMessageHashFormat() {
        Message message = new Message(
                "+27718693002", "Hi Mike, can you join us for dinner tonight?", 1
        );

        String hash = message.createMessageHash();

        // The hash should be XX:1:HITONIGHT (where XX is the first two digits of the auto-generated ID)
        assertTrue(hash.matches("\\d{2}:1:HITONIGHT"));
    }

    // ==========================================================
    // NEW TESTS ADDED TO COVER THE REST OF THE BRIEF REQUIREMENTS
    // ==========================================================

    @Test
    public void testMessageHashLoop() {
        // The brief asks to test the remainder of the hashes in a loop
        String[][] testData = {
            {"+27718693002", "Hi Mike, can you join us for dinner tonight?", "1", "HITONIGHT"},
            {"08575975889", "Hi Keegan, did you receive the payment?", "2", "HIPAYMENT"}
        };

        for (String[] data : testData) {
            Message msg = new Message(data[0], data[1], Integer.parseInt(data[2]));
            String hash = msg.createMessageHash();
            assertTrue(hash.endsWith(data[3]), "Hash should end with " + data[3]);
        }
    }

    @Test
    public void testSentMessageSend() {
        Message msg = new Message("+27718693002", "Hi tonight", 1);
        assertEquals("Message successfully sent.", msg.SentMessage(1));
    }

    @Test
    public void testSentMessageDisregard() {
        Message msg = new Message("+27718693002", "Hi tonight", 1);
        assertEquals("Press 0 to delete the message.", msg.SentMessage(2));
    }

    @Test
    public void testSentMessageStore() {
        Message msg = new Message("+27718693002", "Hi tonight", 1);
        assertEquals("Message successfully stored.", msg.SentMessage(3));
    }
}