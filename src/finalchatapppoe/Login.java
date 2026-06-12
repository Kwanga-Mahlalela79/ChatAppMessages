package finalchatapppoe;

public class Login {

    /**
     * Login class applies these conditions:
     * - Both username and password must be entered correctly
     * - If username and password do not match the registered user, login fails
     * - If username and password match the registered user, login is successful
     */

    private String username;
    private String password;

    // Constructor
    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Validate username: exactly 5 chars, contains '_', no spaces
    private boolean isValidUsername(String username) {
        return username != null &&
               username.contains("_") &&
               username.length() == 5 &&
               !username.contains(" ");
    }

    // Validate password: 8+ chars, uppercase, lowercase, digit, special char
    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
        return password != null && password.matches(regex);
    }

    // Check login credentials against a registered user
    public boolean matchesRegisteredUser(Register registeredUser) {
        return registeredUser != null &&
               this.username.equals(registeredUser.getUsername()) &&
               this.password.equals(registeredUser.getPassword());
    }

    // Login user and return result message
    public String loginUser(Register registeredUser) {
        if (!isValidUsername(username)) {
            return "Username is incorrect. Please enter the correct details used on the register page.";
        }

        if (!isValidPassword(password)) {
            return "Password is incorrect. Please enter the password used on the register page.";
        }

        if (registeredUser == null) {
            return "No registered user found. Please register first.";
        }

        if (!matchesRegisteredUser(registeredUser)) {
            return "Username and password do not match any registered user.";
        }

        return "User has logged in successfully. WELCOME TO THE CHATAPP MESSENGER";
    }
}
