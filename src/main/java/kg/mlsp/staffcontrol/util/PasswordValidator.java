package kg.mlsp.staffcontrol.util;

public class PasswordValidator {

    public static boolean isStrong(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/].*");

        return hasLower && hasUpper && hasDigit && hasSpecial;
    }
}