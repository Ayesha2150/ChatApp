/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

import java.util.Scanner;

public class Main {

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

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

        // --- PART 2: QUICKCHAT MENU ---
        if (loginSuccessful) {
            System.out.println("\nWelcome to QuickChat.");
            boolean running = true;

            while (running) {
                System.out.println("\n--- Menu ---");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
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
                        System.out.println("Exiting QuickChat. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please choose 1, 2, or 3.");
                }
            }
        } else {
            System.out.println("Login failed. You cannot access QuickChat.");
        }
        
        scanner.close();
    } 

    // --- HELPER METHOD FOR SENDING MESSAGES ---
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

            // Keep asking until a valid number is entered
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

            System.out.print("Enter message (max 250 characters): ");
            String content = scanner.nextLine();

            Message msg = new Message(recipient, content, i); 

            String lengthCheck = msg.checkMessageLength();
            System.out.println(lengthCheck);

            if (lengthCheck.equals("Message ready to send.")) {
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

                // Handle the choice
                if (action == 2) {
                    // DISREGARD LOGIC: Prompt to press 0 to delete
                    System.out.println("Press 0 to delete the message.");
                    System.out.print("Enter 0 to confirm deletion: ");
                    String deleteConfirm = scanner.nextLine();
                    
                    if (deleteConfirm.equals("0")) {
                        System.out.println("Message deleted.");
                    } else {
                        System.out.println("Deletion cancelled. Message discarded.");
                    }
                    
                } else {
                    // SEND OR STORE LOGIC
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
            } else {
                 System.out.println("Message cannot be sent because it exceeds the character limit.");
            }
        }

        System.out.println("\nTotal messages successfully sent: " + new Message().returnTotalMessagess());
    } 
}