package util;

public class Validation {

    private Validation() {
        throw new UnsupportedOperationException("Impossible d'instancier une classe utilitaire!");
    }
    // ========== EMAIL VALIDATION ==========
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    // ========== ID VALIDATION ==========
    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }
    // ========== STRING VALIDATION ==========
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }
    // ========== AMOUNT VALIDATION ==========
    public static boolean isValidAmount(double amount) {
        return amount > 0;
    }
    // ========== BALANCE VALIDATION ==========
    public static boolean isValidBalance(double balance) {
        return balance >= 0;
    }
    // ========== ACCOUNT NUMBER VALIDATION ==========
    public static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        // Format: CPT-XXXXX (CPT- suivi de 5 chiffres)
        return accountNumber.matches("^CPT-\\d{5}$");
    }
    // ========== PERCENTAGE VALIDATION ==========
    public static boolean isValidPercentage(double percentage) {
        return percentage >= 0 && percentage <= 100;
    }
    // ========== YEAR VALIDATION ==========
    public static boolean isValidYear(int year) {
        return year >= 1900 && year <= 2100;
    }
    // ========== MONTH VALIDATION ==========
    public static boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }
    // ========== DAY VALIDATION ==========
    public static boolean isValidDay(int day) {
        return day >= 1 && day <= 31;
    }
    // ========== MENU CHOICE VALIDATION ==========
    public static boolean isValidMenuChoice(int choice, int min, int max) {
        return choice >= min && choice <= max;
    }
}