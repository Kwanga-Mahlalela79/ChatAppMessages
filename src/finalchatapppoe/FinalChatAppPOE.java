package finalchatapppoe;

import java.util.Scanner;
import java.util.Random;
import java.util.ArrayList;

public class FinalChatAppPOE {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Register storedUser = null;
        ArrayList<Message> sentMessages = new ArrayList<>();

        System.out.print("How many messages would you like to send? ");
        int maxMessages = sc.nextInt();
        sc.nextLine();

        while (true) {
            System.out.println("\n= MENU =");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Messenger");
            System.out.println("4. Message history with message hash & ID");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\n= Registration =");
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    System.out.print("Enter cellphone (+...): ");
                    String cellphone = sc.nextLine();

                    storedUser = new Register(username, password, cellphone);
                    System.out.println(storedUser.registerUser());
                    break;

                case 2:
                    System.out.println("\n= Login =");
                    System.out.print("Enter username: ");
                    String loginUsername = sc.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = sc.nextLine();

                    Login login = new Login(loginUsername, loginPassword);
                    System.out.println(login.loginUser(storedUser));
                    break;

                case 3:
                    System.out.println("\n= Messenger =");
                    System.out.print("Enter Recipient number: ");
                    String recipient = sc.nextLine();
                    System.out.print("Enter Message: ");
                    String messageText = sc.nextLine();

                    Message msg = new Message(recipient, messageText);

                    if (!msg.checkRecipientCell()) {
                        System.out.println("Error: Invalid recipient number. Must start with + and be 10–13 digits.");
                        break;
                    }

                    if (!msg.checkMessageLength()) {
                        System.out.println("Error: message exceeds 250 characters.");
                        break;
                    }

                    System.out.println("\nChoose option:");
                    System.out.println("1. Send");
                    System.out.println("2. Delete");
                    System.out.println("3. Store for later");

                    int msgChoice = sc.nextInt();
                    sc.nextLine();

                    switch (msgChoice) {
                        case 1:
                            msg.displayMessage();
                            sentMessages.add(msg);
                            break;
                        case 2:
                            System.out.println("Message deleted.");
                            break;
                        case 3:
                            System.out.println("Message stored.");
                            break;
                        default:
                            System.out.println("Invalid option.");
                    }
                    break;

                case 4:
                    System.out.println("\n= Message History =");
                    if (sentMessages.isEmpty()) {
                        System.out.println("No messages sent yet.");
                    } else {
                        System.out.println("Total messages sent: " + sentMessages.size());
                        System.out.println("----------");
                        for (int i = 0; i < sentMessages.size(); i++) {
                            System.out.println("\nMessage #" + (i + 1));
                            sentMessages.get(i).displayMessage();
                            System.out.println("----------");
                        }
                    }
                    break;

                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Public static so tests can access
    public static class Message {
        private String messageID;
        private static int messageCount = 0;
        private String recipient;
        private String messageText;
        private String messageHash;

        public Message(String recipient, String messageText) {
            this.messageID = generateMessageID();
            this.recipient = recipient;
            this.messageText = messageText;
            messageCount++;
            this.messageHash = createMessageHash();
        }

        private String generateMessageID() {
            Random rand = new Random();
            long num = 1000000000L + (long) (rand.nextDouble() * 9000000000L);
            return String.valueOf(num);
        }

        public boolean checkRecipientCell() {
            return recipient.matches("^\\+\\d{10,13}$");
        }

        public boolean checkMessageLength() {
            return messageText.length() <= 250;
        }

        private String createMessageHash() {
            String[] words = messageText.split(" ");
            String firstWord = words[0];
            String lastWord = words[words.length - 1];
            return (messageID.substring(0, 2) + ":" + messageCount + ":" + firstWord + lastWord).toUpperCase();
        }

        public void displayMessage() {
            System.out.println("\nMESSAGE SENT");
            System.out.println("ID: " + messageID);
            System.out.println("Recipient: " + recipient);
            System.out.println("Message: " + messageText);
            System.out.println("Hash: " + messageHash);
        }

        public String getMessageID() { return messageID; }
        public String getMessageText() { return messageText; }
        public String getRecipient() { return recipient; }
        public String getMessageHash() { return messageHash; }
    }
}
