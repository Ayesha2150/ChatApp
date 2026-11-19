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
import java.util.Random;

public class Message {

    private String messageID;
    private String recipientCell;
    private String messageContent;
    private String messageHash;
    private int messageNumber;
    private String flag; 

    // --- PART 3: PARALLEL ARRAYS ---
    public static String[] sentMessages = new String[100];
    public static String[] disregardedMessages = new String[100];
    public static String[] storedMessages = new String[100];
    public static String[] messageHashes = new String[100];
    public static String[] messageIDs = new String[100];
    
    public static int sentCount = 0;
    public static int disregardedCount = 0;
    public static int storedCount = 0;

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

    // FIXED: String-based generation for the ID based on feedback
    private String generateMessageID() {
        StringBuilder id = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            id.append(random.nextInt(10));
        }
        return id.toString();
    }

    public Boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
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
        
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        
        return firstTwoDigits + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1:
                flag = "Sent";
                sentMessages[sentCount] = this.messageContent;
                messageHashes[sentCount] = this.messageHash;
                messageIDs[sentCount] = this.messageID;
                sentCount++;
                return "Message successfully sent.";
            case 2:
                flag = "Disregard";
                disregardedMessages[disregardedCount] = this.messageContent;
                disregardedCount++;
                return "Press 0 to delete the message.";
            case 3:
                flag = "Stored";
                if (storeMessage()) {
                    storedMessages[storedCount] = this.messageContent;
                    messageHashes[storedCount] = this.messageHash; 
                    messageIDs[storedCount] = this.messageID;
                    storedCount++;
                    return "Message successfully stored.";
                }
                return "Message could not be stored.";
            default:
                return "Invalid choice. Message was not sent.";
        }
    }

    public boolean storeMessage() {
        Path path = Paths.get("messages.json");
        try {
            String jsonObject = "{"
                    + "\"messageID\":\"" + escapeJson(messageID) + "\","
                    + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                    + "\"recipientCell\":\"" + escapeJson(recipientCell) + "\","
                    + "\"messageContent\":\"" + escapeJson(messageContent) + "\","
                    + "\"messageNumber\":" + messageNumber + ","
                    + "\"flag\":\"" + escapeJson(flag) + "\""
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

    public static void readJsonToArray() {
        Path path = Paths.get("messages.json");
        if (!Files.exists(path)) return;

        try {
            String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8).trim();
            if (json.equals("[]") || json.isEmpty()) return;

            String[] objects = json.split("\\},\\s*\\{");
            storedCount = 0; 
            for (String obj : objects) {
                if (obj.contains("\"messageContent\"")) {
                    int start = obj.indexOf("\"messageContent\":\"") + 18;
                    int end = obj.indexOf("\"", start);
                    if (start > 17 && end > start) {
                        String content = obj.substring(start, end);
                        storedMessages[storedCount] = content;
                        storedCount++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON: " + e.getMessage());
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

    public static String displayLongestMessage() {
        String longest = "";
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i] != null && storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        return longest.isEmpty() ? "No stored messages." : "Longest Message: " + longest;
    }

    public static String searchByMessageID(String id) {
        for (int i = 0; i < storedCount; i++) {
            if (messageIDs[i] != null && messageIDs[i].equals(id)) {
                return "Message ID: " + messageIDs[i] + "\nMessage: " + storedMessages[i];
            }
        }
        return "Message ID not found.";
    }

    public static String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < storedCount; i++) {
             if (storedMessages[i] != null) {
                 sb.append("Message: ").append(storedMessages[i]).append("\n");
             }
        }
        return sb.length() > 0 ? sb.toString() : "No messages found for recipient.";
    }

    public static String deleteMessageByHash(String hash) {
        for (int i = 0; i < storedCount; i++) {
            if (messageHashes[i] != null && messageHashes[i].equals(hash)) {
                String deletedMsg = storedMessages[i];
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                    messageHashes[j] = messageHashes[j + 1];
                    messageIDs[j] = messageIDs[j + 1];
                }
                storedMessages[storedCount - 1] = null;
                messageHashes[storedCount - 1] = null;
                messageIDs[storedCount - 1] = null;
                storedCount--;
                return "Message: \"" + deletedMsg + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    public static String displayReport() {
        StringBuilder report = new StringBuilder("--- STORED MESSAGES REPORT ---\n");
        for (int i = 0; i < storedCount; i++) {
            report.append("Message Hash: ").append(messageHashes[i]).append("\n")
                  .append("Message: ").append(storedMessages[i]).append("\n")
                  .append("-----------------------------\n");
        }
        return report.toString();
    }

    public String getMessageID() { return messageID; }
    public String getRecipientCell() { return recipientCell; }
    public String getMessageContent() { return messageContent; }
    public String getMessageHash() { return messageHash; }
    public int getMessageNumber() { return messageNumber; }

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