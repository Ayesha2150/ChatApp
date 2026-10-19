/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {

    private String messageID;
    private String recipientCell;
    private String messageContent;
    private String messageHash;
    private int messageNumber;

    // This list holds all messages sent during the runtime
    private static final List<Message> sentMessages = new ArrayList<>();

    public Message() {
        this.messageID = generateMessageID();
    }

    public Message(String recipientCell, String messageContent, int messageNumber) {
        this.messageID = generateMessageID();
        this.recipientCell = recipientCell;
        this.messageContent = messageContent;
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random random = new Random();
        long id = 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
        return String.valueOf(id);
    }

    public Boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{1,10}");
    }

    public String checkRecipientCell() {
        if (recipientCell != null && recipientCell.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code.";
    }

    public String checkMessageLength() {
        if (messageContent == null) {
            return "Please enter a message.";
        }
        if (messageContent.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = messageContent.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    public String createMessageHash() {
        if (messageID == null || messageContent == null || messageContent.trim().isEmpty()) {
            return "";
        }
        String firstTwoDigits = messageID.substring(0, 2);
        String[] words = messageContent.trim().split("\\s+");
        
        // FIXED: Removes punctuation (like ?, !, .) from the start/end of the words
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        
        // Format: 00:0:HITHANKS
        return firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this); // Adds to the list
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                if (storeMessage()) {
                    return "Message successfully stored.";
                }
                return "Message could not be stored.";
            default:
                return "Invalid choice. Message was not sent.";
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }

        StringBuilder output = new StringBuilder();
        for (Message message : sentMessages) {
            output.append("Message ID: ").append(message.messageID).append("\n")
                  .append("Message Hash: ").append(message.messageHash).append("\n")
                  .append("Recipient: ").append(message.recipientCell).append("\n")
                  .append("Message: ").append(message.messageContent).append("\n")
                  .append("-----------------------------\n");
        }
        return output.toString();
    }

    public int returnTotalMessagess() {
        return sentMessages.size();
    }

    public boolean storeMessage() {
        Path path = Paths.get("messages.json");
        try {
            String jsonObject = "{"
                    + "\"messageID\":\"" + escapeJson(messageID) + "\","
                    + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                    + "\"recipientCell\":\"" + escapeJson(recipientCell) + "\","
                    + "\"messageContent\":\"" + escapeJson(messageContent) + "\","
                    + "\"messageNumber\":" + messageNumber
                    + "}";

            String existingJson = "[]";

            if (Files.exists(path)) {
                existingJson = new String(Files.readAllBytes(path), StandardCharsets.UTF_8).trim();
            }

            if (existingJson.isEmpty() || existingJson.equals("[]")) {
                existingJson = "[" + jsonObject + "]";
            } else if (existingJson.endsWith("]")) {
                String inside = existingJson.substring(0, existingJson.length() - 1).trim();
                if (inside.isEmpty()) {
                    existingJson = "[" + jsonObject + "]";
                } else {
                    existingJson = inside + "," + jsonObject + "]";
                }
            } else {
                return false;
            }

            Files.write(path, existingJson.getBytes(StandardCharsets.UTF_8));
            return true;

        } catch (IOException exception) {
            System.out.println("Error storing message: " + exception.getMessage());
            return false;
        }
    }

    private String escapeJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Getters
    public String getMessageID() { return messageID; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageContent() { return messageContent; }
    public String getMessageHash() { return messageHash; }
    public int getMessageNumber() { return messageNumber; }

    // Setters
    public void setRecipientCell(String recipientCell) { this.recipientCell = recipientCell; }
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
        this.messageHash = createMessageHash();
    }
    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash();
    }
}