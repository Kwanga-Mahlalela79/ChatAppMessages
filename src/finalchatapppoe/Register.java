package finalchatapppoe;

public class Register {

    /**
     * Register class applies these conditions:
     * - Username must be 5 characters long, contain an underscore '_', and have no spaces
     * - Password must be 8+ characters: 1 uppercase, 1 lowercase, 1 number, 1 special character
     * - Cellphone must be a valid international number (e.g. +(CODE)XXXXXXXXX)
     */

    private String username;
    private String password;
    private String cellphone;

    // Constructor
    public Register(String username, String password, String cellphone) {
        this.username = username;
        this.password = password;
        this.cellphone = cellphone;
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

    // Validate cellphone: must start with + and be 10–15 digits
    private boolean isValidCellphone(String cellphone) {
        String regex = "^\\+[0-9]{10,15}$";
        return cellphone != null && cellphone.matches(regex);
    }

    // Register user and return result message
    public String registerUser() {
        if (!isValidUsername(username)) {
            return "Username cannot be configured. Please ensure it is exactly 5 characters long, " +
                   "contains an underscore, and has no spaces.";
        }

        if (!isValidPassword(password)) {
            return "Password must be 8 or more characters long and include at least 1 uppercase letter, " +
                   "1 lowercase letter, 1 number, and 1 special character.";
        }

        if (!isValidCellphone(cellphone)) {
            return "Cellphone number must be correctly formatted with the country code " +
                   "following after (e.g. +CodeXXXXXXXXX).";
        }

        return "User has been registered successfully.";
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
