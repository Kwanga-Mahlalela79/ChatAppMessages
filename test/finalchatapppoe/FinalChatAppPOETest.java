package finalchatapppoe;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class FinalChatAppPOETest {

    private static Register registeredUser;
    private static ArrayList<FinalChatAppPOE.Message> sentMessages;

    @BeforeClass
    public static void setUpAll() {
        registeredUser = new Register("kyl_1", "Ch&&sec@ke99!", "+27821234567");

        sentMessages = new ArrayList<>();
        sentMessages.add(new FinalChatAppPOE.Message("+27834557896", "Did you get the cake?"));
        sentMessages.add(new FinalChatAppPOE.Message("+27838884567", "Where are you? You are late! I have asked you to be on time."));
        sentMessages.add(new FinalChatAppPOE.Message("+27834484567", "Yohoo, I am at your gate."));
        sentMessages.add(new FinalChatAppPOE.Message("+27838884567", "It is dinner time!"));
        sentMessages.add(new FinalChatAppPOE.Message("+27838884567", "Ok, I am leaving without you."));
    }

    // ---------------- Registration Tests ----------------
    @Test
    public void testRegisterValidUser() {
        Register user = new Register("kyl_1", "Ch&&sec@ke99!", "+27821234567");
        assertEquals("User has been registered successfully.", user.registerUser());
    }

    @Test
    public void testRegisterUsernameTooShort() {
        Register user = new Register("ky_1", "Ch&&sec@ke99!", "+27821234567");
        assertTrue(user.registerUser().contains("Username cannot be configured"));
    }

    @Test
    public void testRegisterUsernameMissingUnderscore() {
        Register user = new Register("kyle1", "Ch&&sec@ke99!", "+27821234567");
        assertTrue(user.registerUser().contains("Username cannot be configured"));
    }

    @Test
    public void testRegisterWeakPassword() {
        Register user = new Register("kyl_1", "password", "+27821234567");
        assertTrue(user.registerUser().contains("Password must be 8 or more"));
    }

    @Test
    public void testRegisterInvalidCellphone() {
        Register user = new Register("kyl_1", "Ch&&sec@ke99!", "27821234567");
        assertTrue(user.registerUser().contains("Cellphone number"));
    }

    // ---------------- Login Tests ----------------
    @Test
    public void testLoginSuccess() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!");
        assertTrue(login.loginUser(registeredUser).contains("logged in successfully"));
    }

    @Test
    public void testLoginWrongPassword() {
        Login login = new Login("kyl_1", "WrongPass1!");
        assertTrue(login.loginUser(registeredUser).contains("Username and password do not match"));
    }

    @Test
    public void testLoginNoRegisteredUser() {
        Login login = new Login("kyl_1", "Ch&&sec@ke99!");
        String result = login.loginUser(null);
        assertTrue(result.contains("No registered user"));
    }

    // ---------------- Message Tests ----------------
    @Test
    public void testSentMessagesCount() {
        assertEquals(5, sentMessages.size());
    }

    @Test
    public void testSentMessagesHaveIDs() {
        for (FinalChatAppPOE.Message msg : sentMessages) {
            assertNotNull(msg.getMessageID());
            assertEquals(10, msg.getMessageID().length());
        }
    }

    @Test
    public void testRecipientCellValid() {
        FinalChatAppPOE.Message msg = new FinalChatAppPOE.Message("+27821234567", "Hello world");
        assertTrue(msg.checkRecipientCell());
    }

    @Test
    public void testRecipientCellInvalid() {
        FinalChatAppPOE.Message msg = new FinalChatAppPOE.Message("27821234567", "Hello world");
        assertFalse(msg.checkRecipientCell());
    }

    @Test
    public void testMessageLengthValid() {
        FinalChatAppPOE.Message msg = new FinalChatAppPOE.Message("+27821234567", "Short message");
        assertTrue(msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthExceeded() {
        String longText = new String(new char[251]).replace("\0", "A");
        FinalChatAppPOE.Message msg = new FinalChatAppPOE.Message("+27821234567", longText);
        assertFalse(msg.checkMessageLength());
    }

    // ---------------- Longest Message ----------------
    @Test
    public void testFindLongestMessage() {
        FinalChatAppPOE.Message longest = findLongestMessage(sentMessages);
        assertNotNull(longest);
        for (FinalChatAppPOE.Message msg : sentMessages) {
            assertTrue(msg.getMessageText().length() <= longest.getMessageText().length());
        }
    }

    @Test
    public void testFindLongestMessageEmptyList() {
        assertNull(findLongestMessage(new ArrayList<>()));
    }

    // ---------------- Search by ID ----------------
    @Test
    public void testSearchByIDFound() {
        String targetID = sentMessages.get(0).getMessageID();
        FinalChatAppPOE.Message found = searchByID(sentMessages, targetID);
        assertNotNull(found);
        assertEquals(targetID, found.getMessageID());
    }

    @Test
    public void testSearchByIDNotFound() {
        assertNull(searchByID(sentMessages, "0000000000"));
    }

    // ---------------- Search by Recipient ----------------
    @Test
    public void testSearchByRecipientFound() {
        String targetRecipient = "+27838884567";
        ArrayList<FinalChatAppPOE.Message> results = searchByRecipient(sentMessages, targetRecipient);
        assertFalse(results.isEmpty());
        for (FinalChatAppPOE.Message msg : results) {
            assertEquals(targetRecipient, msg.getRecipient());
        }
    }

    @Test
    public void testSearchByRecipientNotFound() {
        ArrayList<FinalChatAppPOE.Message> results = searchByRecipient(sentMessages, "+10000000000");
        assertTrue(results.isEmpty());
    }

    // ---------------- Delete by Hash ----------------
    @Test
    public void testDeleteByHashSuccess() {
        ArrayList<FinalChatAppPOE.Message> copy = new ArrayList<>(sentMessages);
        String hashToDelete = copy.get(0).getMessageHash();
        boolean deleted = deleteByHash(copy, hashToDelete);
        assertTrue(deleted);
        assertEquals(4, copy.size());
    }

    @Test
    public void testDeleteByHashNotFound() {
        ArrayList<FinalChatAppPOE.Message> copy = new ArrayList<>(sentMessages);
        boolean deleted = deleteByHash(copy, "XX:99:FAKEHASH");
        assertFalse(deleted);
        assertEquals(5, copy.size());
    }

    // ---------------- Report ----------------
    @Test
    public void testDisplayReportNotEmpty() {
        String report = generateReport(sentMessages);
        assertNotNull(report);
        assertFalse(report.isEmpty());
    }

    @Test
    public void testDisplayReportContainsAllIDs() {
        String report = generateReport(sentMessages);
        for (FinalChatAppPOE.Message msg : sentMessages) {
            assertTrue(report.contains(msg.getMessageID()));
        }
    }

    @Test
    public void testDisplayReportContainsAllRecipients() {
        String report = generateReport(sentMessages);
        for (FinalChatAppPOE.Message msg : sentMessages) {
            assertTrue(report.contains(msg.getRecipient()));
        }
    }

    @Test
    public void testDisplayReportContainsAllHashes() {
        String report = generateReport(sentMessages);
        for (FinalChatAppPOE.Message msg : sentMessages) {
            assertTrue(report.contains(msg.getMessageHash()));
        }
    }

    @Test
    public void testDisplayReportEmpty() {
        String report = generateReport(new ArrayList<>());
        assertTrue(report.contains("No messages"));
    }

    // ---------------- Helper Methods ----------------
    private FinalChatAppPOE.Message findLongestMessage(ArrayList<FinalChatAppPOE.Message> messages) {
        if (messages.isEmpty()) return null;
        FinalChatAppPOE.Message longest = messages.get(0);
        for (FinalChatAppPOE.Message msg : messages) {
            if (msg.getMessageText().length() > longest.getMessageText().length()) {
                longest = msg;
            }
        }
        return longest;
    }

    private FinalChatAppPOE.Message searchByID(ArrayList<FinalChatAppPOE.Message> messages, String id) {
        for (FinalChatAppPOE.Message msg : messages) {
            if (msg.getMessageID().equals(id)) return msg;
        }
        return null;
    }

        private ArrayList<FinalChatAppPOE.Message> searchByRecipient(ArrayList<FinalChatAppPOE.Message> messages, String recipient) {
        ArrayList<FinalChatAppPOE.Message> results = new ArrayList<>();
        for (FinalChatAppPOE.Message msg : messages) {
            if (msg.getRecipient().equals(recipient)) {
                results.add(msg);
            }
        }
        return results;
    }

    private boolean deleteByHash(ArrayList<FinalChatAppPOE.Message> messages, String hash) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getMessageHash().equals(hash)) {
                messages.remove(i);
                return true;
            }
        }
        return false;
    }

    private String generateReport(ArrayList<FinalChatAppPOE.Message> messages) {
        if (messages.isEmpty()) {
            return "No messages to display.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("CHATAPP MESSAGE REPORT\n");
        sb.append(String.format("Total messages: %d%n%n", messages.size()));

        for (int i = 0; i < messages.size(); i++) {
            FinalChatAppPOE.Message msg = messages.get(i);
            sb.append(String.format("Message #%d%n", i + 1));
            sb.append(String.format("ID: %s%n", msg.getMessageID()));
            sb.append(String.format("Recipient: %s%n", msg.getRecipient()));
            sb.append(String.format("Message: %s%n", msg.getMessageText()));
            sb.append(String.format("Hash: %s%n%n", msg.getMessageHash()));
        }

        return sb.toString();
    }
}
