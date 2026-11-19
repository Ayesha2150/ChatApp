/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        // Load any previously saved messages from the JSON file into the arrays
        Message.readJsonToArray();

        System.out.println("=== REGISTRATION ===");
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        if (login.checkUserName(username)) {
            System.out.println("Username successfully captured.");
        } else {
            System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
        }

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        if (login.checkPasswordComplexity(password)) {
            System.out.println("Password successfully captured.");
        } else {
            System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
        }

        System.out.print("Enter your South African cell phone number: ");
        String cellPhoneNumber = scanner.nextLine();

        if (login.checkCellPhoneNumber(cellPhoneNumber)) {
            System.out.println("Cell phone number successfully captured.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        login.setUserDetails(username, password, cellPhoneNumber, firstName, lastName);
        System.out.println(login.registerUser());

        // --- LOGIN SECTION ---
        System.out.println("\n=== LOGIN ===");
        System.out.print("Enter your username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();

        boolean loginSuccessful = login.loginUser(loginUsername, loginPassword);
        System.out.println(login.returnLoginStatus(loginSuccessful));

        // --- QUICKCHAT MENU ---
        if (loginSuccessful) {
            System.out.println("\nWelcome to QuickChat.");
            boolean running = true;

            while (running) {
                System.out.println("\n--- Menu ---");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Stored Messages");
                System.out.println("4) Quit");
                System.out.print("Choose an option: ");
                
                String menuChoice = scanner.nextLine();

                switch (menuChoice) {
                    case "1":
                        sendMessages(scanner); 
                        break;
                    case "2":
                        System.out.println("Coming Soon.");
                        break;
                    case "3":
                        storedMessagesMenu(scanner); 
                        break;
                    case "4":
                        System.out.println("Exiting QuickChat. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose 1, 2, 3, or 4.");
                }
            }
        } else {
            System.out.println("Login failed. You cannot access QuickChat.");
        }
        
        scanner.close();
    } 

    private static void sendMessages(Scanner scanner) {
        System.out.print("How many messages do you wish to enter? ");
        int numMessages;
        
        try {
            numMessages = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Returning to menu.");
            return;
        }

        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
            
            String recipient = "";
            boolean validNumber = false;

            while (!validNumber) {
                System.out.print("Enter recipient cell number (e.g., +27718693002): ");
                recipient = scanner.nextLine();

                Message tempMsg = new Message(recipient, "test", i);
                String recipientCheck = tempMsg.checkRecipientCell();
                System.out.println(recipientCheck); 

                if (recipientCheck.equals("Cell phone number successfully captured.")) {
                    validNumber = true; 
                }
            }

            // FIXED: Loop until the message is 250 characters or less
            String content = "";
            boolean validLength = false;
            while (!validLength) {
                System.out.print("Enter message (max 250 characters): ");
                content = scanner.nextLine();

                Message tempMsg = new Message(recipient, content, i);
                String lengthCheck = tempMsg.checkMessageLength();
                System.out.println(lengthCheck);

                if (lengthCheck.equals("Message ready to send.")) {
                    validLength = true;
                }
            }

            Message msg = new Message(recipient, content, i); 

            System.out.println("1. Send Message");
            System.out.println("2. Disregard Message");
            System.out.println("3. Store Message to send later");
            System.out.print("Enter your choice: ");
            
            int action = 0;
            try {
                action = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Message discarded.");
                continue;
            }

            if (action == 2) {
                System.out.println("Press 0 to delete the message.");
                System.out.print("Enter 0 to confirm deletion: ");
                String deleteConfirm = scanner.nextLine();
                
                if (deleteConfirm.equals("0")) {
                    System.out.println(msg.SentMessage(2));
                    System.out.println("Message deleted.");
                } else {
                    System.out.println("Deletion cancelled. Message discarded.");
                }
            } else {
                String result = msg.SentMessage(action);
                System.out.println(result);

                if (action == 1) {
                    System.out.println("\n--- Message Details ---");
                    System.out.println("Message ID: " + msg.getMessageID());
                    System.out.println("Message Hash: " + msg.getMessageHash());
                    System.out.println("Recipient: " + msg.getRecipientCell());
                    System.out.println("Message: " + msg.getMessageContent());
                }
            }
        }

        System.out.println("\nTotal messages successfully sent: " + Message.sentCount);
    } 

    // --- PART 3: SUB-MENU FOR STORED MESSAGES ---
    private static void storedMessagesMenu(Scanner scanner) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) Display sender and recipient of all stored messages.");
            System.out.println("b) Display the longest stored message.");
            System.out.println("c) Search for a message ID.");
            System.out.println("d) Search messages for a particular recipient.");
            System.out.println("e) Delete a message using the message hash.");
            System.out.println("f) Display full report.");
            System.out.println("g) Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "a":
                    System.out.println("Displaying stored messages...");
                    for(int i=0; i < Message.storedCount; i++) {
                        if(Message.storedMessages[i] != null) {
                             System.out.println("Message: " + Message.storedMessages[i]);
                        }
                    }
                    break;
                case "b":
                    System.out.println(Message.displayLongestMessage());
                    break;
                case "c":
                    System.out.print("Enter Message ID: ");
                    String id = scanner.nextLine();
                    System.out.println(Message.searchByMessageID(id));
                    break;
                case "d":
                    System.out.print("Enter Recipient: ");
                    String rec = scanner.nextLine();
                    System.out.println(Message.searchByRecipient(rec));
                    break;
                case "e":
                    System.out.print("Enter Message Hash: ");
                    String hash = scanner.nextLine();
                    System.out.println(Message.deleteMessageByHash(hash));
                    break;
                case "f":
                    System.out.println(Message.displayReport());
                    break;
                case "g":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}