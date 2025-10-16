package services;

import enums.TranType;
import models.accounts.BankAccount;
import models.transactions.Transaction;
import repository.Accountrepository;
import repository.Transactionrepository;
import util.Validation;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {

    private final Transactionrepository transactionDAO;
    private final Accountrepository accountDAO;

    public TransactionService() {
        this.transactionDAO = new Transactionrepository();
        this.accountDAO = new Accountrepository();
    }

    // ========== CREATE TRANSACTION ==========
    public boolean createTransaction(LocalDateTime date, double amount, TranType type, String location, String number) {
        if (date == null || date.isAfter(LocalDateTime.now())) {
            System.err.println("Erreur : Date invalide");
            return false;
        }

        if (!Validation.isValidAmount(amount)) {
            System.err.println("Erreur : Le montant doit etre positif");
            return false;
        }

        if (type == null) {
            System.err.println("Erreur : Type de transaction invalide");
            return false;
        }

        if (!Validation.isValidString(location)) {
            System.err.println("Erreur : Lieu invalide");
            return false;
        }

        if (!Validation.isValidAccountNumber(number)) {
            System.err.println("Erreur : Numero du compte invalide");
            return false;
        }

        Optional<BankAccount> account = accountDAO.findByNumber(number);
        if (account.isEmpty()) {
            System.err.println("Erreur : Compte introuvable avec le numero : " + number);
            return false;
        }

        try {
            long accountId = account.get().getId();
            var transaction = new Transaction(date, amount, type, location, accountId);
            transactionDAO.create(transaction);
            return true;
        } catch (SQLException e) {
            System.err.println("Echec de la creation de la transaction : " + e.getMessage());
            return false;
        }
    }

    // ========== LIST TRANSACTIONS ==========

    public List<Transaction> getTransactionsByAccount(Long accountId) {
        if (!Validation.isValidId(accountId)) {
            System.err.println("Erreur : ID compte invalide");
            return List.of();
        }

        return transactionDAO.findByAccountId(accountId).stream()
                .sorted(Comparator.comparing(Transaction::date).reversed())
                .toList();
    }

    public List<Transaction> getTransactionsByClient(Long clientId) {
        if (!Validation.isValidId(clientId)) {
            System.err.println("Erreur : ID client invalide");
            return List.of();
        }

        return transactionDAO.findByClientId(clientId).stream()
                .sorted(Comparator.comparing(Transaction::date).reversed())
                .toList();
    }

    public List<Transaction> getAllTransactions() {
        return transactionDAO.findAll().stream()
                .sorted(Comparator.comparing(Transaction::date).reversed())
                .toList();
    }

    // ========== FILTER TRANSACTIONS ==========

    public List<Transaction> filterByAmount(List<Transaction> transactions, double minAmount, double maxAmount) {
        return transactions.stream()
                .filter(t -> t.amount() >= minAmount && t.amount() <= maxAmount)
                .toList();
    }

    public List<Transaction> filterByType(List<Transaction> transactions, TranType type) {
        if (type == null) {
            return transactions;
        }

        return transactions.stream()
                .filter(t -> t.type() == type)
                .toList();
    }

    public List<Transaction> filterByDateRange(List<Transaction> transactions, LocalDateTime startDate, LocalDateTime endDate) {
        return transactions.stream()
                .filter(t -> !t.date().isBefore(startDate) && !t.date().isAfter(endDate))
                .toList();
    }

    public List<Transaction> filterByLocation(List<Transaction> transactions, String location) {
        if (!Validation.isValidString(location)) {
            return transactions;
        }

        return transactions.stream()
                .filter(t -> t.location().toLowerCase().contains(location.toLowerCase()))
                .toList();
    }

    // ========== GROUP TRANSACTIONS ==========

    public Map<TranType, List<Transaction>> groupByType(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::type));
    }

    public Map<YearMonth, List<Transaction>> groupByMonth(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(t -> YearMonth.from(t.date())));
    }

    public Map<LocalDate, List<Transaction>> groupByDay(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(t -> t.date().toLocalDate()));
    }

    public Map<String, List<Transaction>> groupByLocation(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::location));
    }

    // ========== CALCULATE STATISTICS ==========

    public double getTotalAmount(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(Transaction::amount)
                .sum();
    }

    public OptionalDouble getAverageAmount(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(Transaction::amount)
                .average();
    }

    public double getTotalByAccount(Long accountId) {
        if (!Validation.isValidId(accountId)) {
            return 0.0;
        }

        return transactionDAO.findByAccountId(accountId).stream()
                .mapToDouble(Transaction::amount)
                .sum();
    }

    public double getTotalByClient(Long clientId) {
        if (!Validation.isValidId(clientId)) {
            return 0.0;
        }

        return transactionDAO.findByClientId(clientId).stream()
                .mapToDouble(Transaction::amount)
                .sum();
    }

    public OptionalDouble getAverageByAccount(Long accountId) {
        if (!Validation.isValidId(accountId)) {
            return OptionalDouble.empty();
        }

        return transactionDAO.findByAccountId(accountId).stream()
                .mapToDouble(Transaction::amount)
                .average();
    }

    public OptionalDouble getAverageByClient(Long clientId) {
        if (!Validation.isValidId(clientId)) {
            return OptionalDouble.empty();
        }

        return transactionDAO.findByClientId(clientId).stream()
                .mapToDouble(Transaction::amount)
                .average();
    }

    // ========== DETECT SUSPICIOUS TRANSACTIONS ==========

    public List<Transaction> detectHighAmountTransactions(List<Transaction> transactions, double threshold) {
        return transactions.stream()
                .filter(t -> t.amount() > threshold)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .toList();
    }

    public List<Transaction> detectUnusualLocation(List<Transaction> transactions, String usualCountry) {
        if (!Validation.isValidString(usualCountry)) {
            return List.of();
        }

        return transactions.stream()
                .filter(t -> !t.location().toLowerCase().contains(usualCountry.toLowerCase()))
                .toList();
    }

    public List<Transaction> detectHighFrequency(List<Transaction> transactions, long maxMinutesBetween) {
        List<Transaction> suspicious = new ArrayList<>();

        var sortedTransactions = transactions.stream()
                .sorted(Comparator.comparing(Transaction::date))
                .toList();

        for (int i = 0; i < sortedTransactions.size() - 1; i++) {
            var current = sortedTransactions.get(i);
            var next = sortedTransactions.get(i + 1);

            long minutesBetween = ChronoUnit.MINUTES.between(current.date(), next.date());

            if (minutesBetween <= maxMinutesBetween) {
                if (!suspicious.contains(current)) {
                    suspicious.add(current);
                }
                if (!suspicious.contains(next)) {
                    suspicious.add(next);
                }
            }
        }

        return suspicious;
    }

    public List<Transaction> detectAllSuspicious(Long accountId, double amountThreshold, String usualCountry, long maxMinutesBetween) {
        if (!Validation.isValidId(accountId)) {
            return List.of();
        }

        var transactions = transactionDAO.findByAccountId(accountId);

        var highAmount = detectHighAmountTransactions(transactions, amountThreshold);
        var unusualLocation = detectUnusualLocation(transactions, usualCountry);
        var highFrequency = detectHighFrequency(transactions, maxMinutesBetween);

        return transactions.stream()
                .filter(t -> highAmount.contains(t) || unusualLocation.contains(t) || highFrequency.contains(t))
                .distinct()
                .sorted(Comparator.comparing(Transaction::date).reversed())
                .toList();
    }

    // ========== DISPLAY TRANSACTION REPORT ==========

    public void displayTransactionReport(Long accountId) {
        if (!Validation.isValidId(accountId)) {
            System.err.println("Erreur : ID compte invalide");
            return;
        }

        var transactions = transactionDAO.findByAccountId(accountId);

        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction trouvee pour ce compte");
            return;
        }

        System.out.println("\n========== RAPPORT TRANSACTIONS ==========");
        System.out.println("Compte ID : " + accountId);
        System.out.println("Nombre total de transactions : " + transactions.size());
        System.out.println("Montant total : " + String.format("%.2f", getTotalAmount(transactions)) + " MAD");

        getAverageAmount(transactions).ifPresent(avg ->
                System.out.println("Montant moyen : " + String.format("%.2f", avg) + " MAD")
        );

        System.out.println("\n--- Repartition par type ---");
        var byType = groupByType(transactions);
        byType.forEach((type, list) ->
                System.out.println(type + " : " + list.size() + " transaction(s) - Total : " +
                        String.format("%.2f", getTotalAmount(list)) + " MAD")
        );

        System.out.println("\n--- Transactions suspectes (>10000 MAD) ---");
        var suspicious = detectHighAmountTransactions(transactions, 10000.0);
        if (suspicious.isEmpty()) {
            System.out.println("Aucune transaction suspecte detectee");
        } else {
            suspicious.forEach(t ->
                    System.out.println("  - " + t.date() + " : " +
                            String.format("%.2f", t.amount()) + " MAD (" + t.type() + ")")
            );
        }

        System.out.println("==========================================\n");
    }
}