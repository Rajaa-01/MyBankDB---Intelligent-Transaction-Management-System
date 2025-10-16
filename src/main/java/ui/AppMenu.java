package ui;

import enums.TranType;
import models.accounts.BankAccount;
import models.clients.ClientRecord;
import models.transactions.Transaction;
import services.AccountService;
import services.ClientService;
import services.ReportService;
import services.TransactionService;
import util.Input;
import util.Validation;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class AppMenu {

    private final ClientService clientService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ReportService reportService;

    public AppMenu() {
        this.clientService = new ClientService();
        this.accountService = new AccountService();
        this.transactionService = new TransactionService();
        this.reportService = new ReportService();
    }

    public void start() {
        System.out.println("\n  BIENVENUE DANS BANKGUARD ANALYZER");

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = Input.readInt("Votre choix : ");

            switch (choice) {
                case 1 -> manageClients();
                case 2 -> manageAccounts();
                case 3 -> manageTransactions();
                case 4 -> displayReportsMenu();
                case 0 -> {
                    System.out.println("\nMerci d'avoir utilise BankGuard Analyzer. Au revoir !");
                    running = false;
                }
                default -> System.out.println("Choix invalide. Veuillez reessayer.");
            }
        }
    }

    // ========== MAIN MENU ==========
    private void displayMainMenu() {
        System.out.println("\n========== MENU PRINCIPAL ==========");
        System.out.println("1. Gestion des clients");
        System.out.println("2. Gestion des comptes");
        System.out.println("3. Gestion des transactions");
        System.out.println("4. Rapports et analyses");
        System.out.println("0. Quitter");
        System.out.println("====================================");
    }

    // ========== CLIENT MANAGEMENT ==========
    private void manageClients() {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== GESTION DES CLIENTS ==========");
            System.out.println("1. Ajouter un client");
            System.out.println("2. Modifier un client");
            System.out.println("3. Supprimer un client");
            System.out.println("4. Rechercher un client par ID");
            System.out.println("5. Rechercher un client par nom");
            System.out.println("6. Lister tous les clients");
            System.out.println("7. Afficher le rapport d'un client");
            System.out.println("0. Retour");
            System.out.println("=========================================");

            int choice = Input.readInt("Votre choix : ");

            switch (choice) {
                case 1 -> addClient();
                case 2 -> updateClient();
                case 3 -> deleteClient();
                case 4 -> findClientById();
                case 5 -> findClientByName();
                case 6 -> listAllClients();
                case 7 -> displayClientReport();
                case 0 -> back = true;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void addClient() {
        System.out.println("\n--- Ajouter un client ---");
        String name = Input.readString("Nom : ");
        String email = Input.readString("Email : ");

        Optional<ClientRecord> createdClient = clientService.addClient(name, email);
        if (createdClient.isPresent()) {
            var client = createdClient.get();
            System.out.println("Client ajoute avec succes !");
            System.out.println("ID : " + client.id() + " | Nom : " + client.name() + " | Email : " + client.email());
        }
    }
    private void updateClient() {
        System.out.println("\n--- Modifier un client ---");
        long id = Input.readLong("ID du client : ");
        String name = Input.readString("Nouveau nom : ");
        String email = Input.readString("Nouvel email : ");

        clientService.updateClient(id, name, email);
    }
    private void deleteClient() {
        System.out.println("\n--- Supprimer un client ---");
        long id = Input.readLong("ID du client : ");

        if (clientService.deleteClient(id)) {
            System.out.println("Client supprime avec succes !");
        }
    }

    private void findClientById() {
        System.out.println("\n--- Rechercher un client par ID ---");
        long id = Input.readLong("ID du client : ");

        Optional<ClientRecord> client = clientService.findClientById(id);
        if (client.isPresent()) {
            displayClient(client.get());
        } else {
            System.out.println("Client introuvable.");
        }
    }

    private void findClientByName() {
        System.out.println("\n--- Rechercher un client par nom ---");
        String name = Input.readString("Nom : ");

        List<ClientRecord> clients = clientService.findClientsByName(name);
        if (clients.isEmpty()) {
            System.out.println("Aucun client trouve.");
        } else {
            System.out.println("\nClients trouves :");
            clients.forEach(this::displayClient);
        }
    }

    private void listAllClients() {
        System.out.println("\n--- Liste de tous les clients ---");
        List<ClientRecord> clients = clientService.getAllClients();

        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistre.");
        } else {
            clients.forEach(this::displayClient);
        }
    }

    private void displayClientReport() {
        System.out.println("\n--- Rapport client ---");
        long id = Input.readLong("ID du client : ");
        clientService.displayClientReport(id);
    }

    private void displayClient(ClientRecord client) {
        System.out.println("  [" + client.id() + "] " + client.name() + " - " + client.email());
    }

    // ========== ACCOUNT MANAGEMENT ==========
    private void manageAccounts() {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== GESTION DES COMPTES ==========");
            System.out.println("1. Creer un compte courant");
            System.out.println("2. Creer un compte epargne");
            System.out.println("3. Modifier le solde");
            System.out.println("4. Modifier le decouvert autorise");
            System.out.println("5. Modifier le taux d'interet");
            System.out.println("6. Supprimer un compte");
            System.out.println("7. Rechercher un compte par ID");
            System.out.println("8. Rechercher un compte par numero");
            System.out.println("9. Lister les comptes d'un client");
            System.out.println("10. Afficher le rapport d'un compte");
            System.out.println("0. Retour");
            System.out.println("=========================================");

            int choice = Input.readInt("Votre choix : ");

            switch (choice) {
                case 1 -> createCheckingAccount();
                case 2 -> createSavingsAccount();
                case 3 -> updateBalance();
                case 4 -> updateOverdraft();
                case 5 -> updateInterest();
                case 6 -> deleteAccount();
                case 7 -> findAccountById();
                case 8 -> findAccountByNumber();
                case 9 -> listAccountsByClient();
                case 10 -> displayAccountReport();
                case 0 -> back = true;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void createCheckingAccount() {
        System.out.println("\n--- Creer un compte courant ---");
        double balance = Input.readDouble("Solde initial : ");
        long clientId = Input.readLong("ID du client : ");
        double overdraft = Input.readDouble("Decouvert autorise : ");

        if (accountService.createCheckingAccount(balance, clientId, overdraft)) {
            System.out.println("Compte courant cree avec succes !");
        }
    }

    private void createSavingsAccount() {
        System.out.println("\n--- Creer un compte epargne ---");
        double balance = Input.readDouble("Solde initial : ");
        long clientId = Input.readLong("ID du client : ");
        double interest = Input.readDouble("Taux d'interet (%) : ");

        if (accountService.createSavingsAccount(balance, clientId, interest)) {
            System.out.println("Compte epargne cree avec succes !");
        }
    }

    private void updateBalance() {
        System.out.println("\n--- Modifier le solde ---");
        String number = Input.readString("Numero du compte : ");
        double newBalance = Input.readDouble("Nouveau solde : ");

        if (accountService.updateBalance(number, newBalance)) {
            System.out.println("Solde modifie avec succes !");
        }
    }

    private void updateOverdraft() {
        System.out.println("\n--- Modifier le decouvert autorise ---");
        String number = Input.readString("Numero du compte : ");
        double newOverdraft = Input.readDouble("Nouveau decouvert : ");

        if (accountService.updateOverdraft(number, newOverdraft)) {
            System.out.println("Decouvert modifie avec succes !");
        }
    }

    private void updateInterest() {
        System.out.println("\n--- Modifier le taux d'interet ---");
        String number = Input.readString("Numero du compte : ");
        double newInterest = Input.readDouble("Nouveau taux d'interet (%) : ");

        if (accountService.updateInterest(number, newInterest)) {
            System.out.println("Taux d'interet modifie avec succes !");
        }
    }

    private void deleteAccount() {
        System.out.println("\n--- Supprimer un compte ---");
        String number = Input.readString("Numero du compte : ");

        if (accountService.deleteAccount(number)) {
            System.out.println("Compte supprime avec succes !");
        }
    }

    private void findAccountById() {
        System.out.println("\n--- Rechercher un compte par ID ---");
        long id = Input.readLong("ID du compte : ");

        Optional<BankAccount> account = accountService.findAccountById(id);
        if (account.isPresent()) {
            displayAccount(account.get());
        } else {
            System.out.println("Compte introuvable.");
        }
    }

    private void findAccountByNumber() {
        System.out.println("\n--- Rechercher un compte par numero ---");
        String number = Input.readString("Numero du compte : ").toUpperCase();

        Optional<BankAccount> account = accountService.findAccountByNumber(number);
        if (account.isPresent()) {
            displayAccount(account.get());
        } else {
            System.out.println("Compte introuvable.");
        }
    }

    private void listAccountsByClient() {
        System.out.println("\n--- Lister les comptes d'un client ---");
        long clientId = Input.readLong("ID du client : ");

        List<BankAccount> accounts = accountService.findAccountsByClient(clientId);
        if (accounts.isEmpty()) {
            System.out.println("Aucun compte trouve.");
        } else {
            System.out.println("\nComptes trouves :");
            accounts.forEach(this::displayAccount);
        }
    }

    private void displayAccountReport() {
        System.out.println("\n--- Rapport compte ---");
        String number = Input.readString("Numero du compte : ");
        Optional<BankAccount> find = accountService.findAccountByNumber(number);
        if(find.isPresent()){
            long id = find.get().getId();
            accountService.displayAccountReport(id);
        }
    }

    private void displayAccount(BankAccount account) {
        System.out.println("  [" + account.getId() + "] " + account.getNumber() +
                " - Type: " + account.getType() +
                " - Solde: " + String.format("%.2f", account.getBalance()) + " MAD");
    }

    // ========== TRANSACTION MANAGEMENT ==========
    private void manageTransactions() {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== GESTION DES TRANSACTIONS ==========");
            System.out.println("1. Enregistrer une transaction");
            System.out.println("2. Consulter l'historique d'un compte");
            System.out.println("3. Consulter les transactions d'un client");
            System.out.println("4. Afficher le rapport d'un compte");
            System.out.println("0. Retour");
            System.out.println("==============================================");

            int choice = Input.readInt("Votre choix : ");

            switch (choice) {
                case 1 -> createTransaction();
                case 2 -> viewAccountTransactions();
                case 3 -> viewClientTransactions();
                case 4 -> displayTransactionReport();
                case 0 -> back = true;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void createTransaction() {
        System.out.println("\n--- Enregistrer une transaction ---");

        System.out.println("Type de transaction :");
        System.out.println("1. Versement");
        System.out.println("2. Retrait");
        System.out.println("3. Virement");
        int typeChoice = Input.readInt("Choix : ");

        TranType type = switch (typeChoice) {
            case 1 -> TranType.DEPOSIT;
            case 2 -> TranType.WITHDRAWAL;
            case 3 -> TranType.TRANSFER;
            default -> {
                System.out.println("Type invalide.");
                yield null;
            }
        };

        if (type == null) return;

        double amount = Input.readDouble("Montant : ");
        String location = Input.readString("Lieu : ");
        String number = Input.readString("Numero du compte : ");

        if (transactionService.createTransaction(LocalDateTime.now(), amount, type, location, number)) {
            System.out.println("Transaction enregistree avec succes !");
        }
    }

    private void viewAccountTransactions() {
        System.out.println("\n--- Historique des transactions d'un compte ---");
        String number = Input.readString("Numero du compte : ");

        Optional<BankAccount> account = accountService.findAccountByNumber(number);
        long accountId = account.get().getId();
        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountId);
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction trouvee.");
        } else {
            System.out.println("\nTransactions :");
            transactions.forEach(this::displayTransaction);
        }
    }

    private void viewClientTransactions() {
        System.out.println("\n--- Transactions d'un client ---");
        long clientId = Input.readLong("ID du client : ");

        List<Transaction> transactions = transactionService.getTransactionsByClient(clientId);
        if (transactions.isEmpty()) {
            System.out.println("Aucune transaction trouvee.");
        } else {
            System.out.println("\nTransactions :");
            transactions.forEach(this::displayTransaction);
        }
    }

    private void displayTransactionReport() {
        System.out.println("\n--- Rapport des transactions ---");
        String number = Input.readString("Numero du compte : ");
        Optional<BankAccount> account = accountService.findAccountByNumber(number);
        long accountId = account.get().getId();
        transactionService.displayTransactionReport(accountId);
    }

    private void displayTransaction(Transaction transaction) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        System.out.println("  [" + transaction.id() + "] " +
                transaction.date().format(formatter) + " - " +
                transaction.type() + " - " +
                String.format("%.2f", transaction.amount()) + " MAD - " +
                transaction.location());
    }

    // ========== REPORTS AND ANALYSIS ==========
    private void displayReportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n========== RAPPORTS ET ANALYSES ==========");
            System.out.println("1. Top 5 clients par solde");
            System.out.println("2. Rapport mensuel");
            System.out.println("3. Transactions suspectes");
            System.out.println("4. Comptes inactifs");
            System.out.println("0. Retour");
            System.out.println("==========================================");

            int choice = Input.readInt("Votre choix : ");

            switch (choice) {
                case 1 -> reportService.displayTop5ClientsByBalance();
                case 2 -> displayMonthlyReport();
                case 3 -> displaySuspiciousTransactions();
                case 4 -> displayInactiveAccounts();
                case 0 -> back = true;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void displayMonthlyReport() {
        System.out.println("\n--- Rapport mensuel ---");
        int year = Input.readInt("Annee (ex: 2025) : ");
        int month = Input.readInt("Mois (1-12) : ");

        if (!Validation.isValidYear(year) || !Validation.isValidMonth(month)) {
            System.out.println("Date invalide.");
            return;
        }

        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            reportService.displayMonthlyReport(yearMonth);
        } catch (Exception e) {
            System.out.println("Erreur lors de la creation de la date.");
        }
    }

    private void displaySuspiciousTransactions() {
        System.out.println("\n--- Transactions suspectes ---");
        double threshold = Input.readDouble("Seuil de montant (ex: 10000) : ");
        String country = Input.readString("Pays habituel (ex: Morocco) : ");
        long maxMinutes = Input.readLong("Frequence maximale en minutes (ex: 1) : ");

        reportService.displaySuspiciousTransactions(threshold, country, maxMinutes);
    }

    private void displayInactiveAccounts() {
        System.out.println("\n--- Comptes inactifs ---");
        int days = Input.readInt("Nombre de jours d'inactivite (ex: 30) : ");
        reportService.displayInactiveAccounts(days);
    }


}