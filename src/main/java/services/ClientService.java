package services;

import models.accounts.BankAccount;
import models.clients.ClientRecord;
import repository.Accountrepository;
import repository.Clientrepository;
import util.Validation;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ClientService {

    private final Clientrepository clientDAO;
    private final Accountrepository accountDAO;

    public ClientService() {
        this.clientDAO = new Clientrepository();
        this.accountDAO = new Accountrepository();
    }

    // ========== CREATE ==========
    public Optional<ClientRecord> addClient(String name, String email) {
        if (!Validation.isValidString(name)) {
            System.err.println("Erreur : Le nom du client ne peut pas etre vide");
            return Optional.empty();
        }

        if (!Validation.isValidEmail(email)) {
            System.err.println("Erreur : L'email est invalide");
            return Optional.empty();
        }

        try {
            var client = new ClientRecord(name.trim(), email.trim());
            return clientDAO.create(client);
        } catch (SQLException e) {
            System.err.println("Echec de l'ajout du client : " + e.getMessage());
            return Optional.empty();
        }
    }

    // ========== UPDATE ==========
    public boolean updateClient(Long id, String name, String email) {
        if (!Validation.isValidId(id)) {
            System.err.println("Erreur : ID invalide");
            return false;
        }

        if (!Validation.isValidString(name)) {
            System.err.println("Erreur : Le nom du client ne peut pas etre vide");
            return false;
        }

        if (!Validation.isValidEmail(email)) {
            System.err.println("Erreur : L'email est invalide");
            return false;
        }

        Optional<ClientRecord> existingClient = clientDAO.findById(id);
        if (existingClient.isEmpty()) {
            System.err.println("Erreur : Client introuvable avec l'ID : " + id);
            return false;
        }

        var updatedClient = new ClientRecord(id, name.trim(), email.trim());
        clientDAO.update(updatedClient);
        return true;
    }

    // ========== DELETE ==========
    public boolean deleteClient(Long id) {
        if (!Validation.isValidId(id)) {
            System.err.println("Erreur : ID invalide");
            return false;
        }

        Optional<ClientRecord> client = clientDAO.findById(id);
        if (client.isEmpty()) {
            System.err.println("Erreur : Client introuvable avec l'ID : " + id);
            return false;
        }

        // Verifier si le client a des comptes
        List<BankAccount> accounts = accountDAO.findByClientId(id);
        if (!accounts.isEmpty()) {
            System.err.println("Erreur : Impossible de supprimer le client. Il possede " +
                    accounts.size() + " compte(s)");
            return false;
        }

        return clientDAO.delete(id);  // Retourne le résultat du DAO
    }

    // ========== READ BY ID ==========
    public Optional<ClientRecord> findClientById(Long id) {
        if (!Validation.isValidId(id)) {
            System.err.println("Erreur : ID invalide");
            return Optional.empty();
        }
        return clientDAO.findById(id);
    }

    // ========== READ BY NAME ==========
    public List<ClientRecord> findClientsByName(String name) {
        if (!Validation.isValidString(name)) {
            System.err.println("Erreur : Le nom ne peut pas etre vide");
            return List.of();
        }
        return clientDAO.findByName(name.trim());
    }

    // ========== READ ALL ==========
    public List<ClientRecord> getAllClients() {
        return clientDAO.findAll();
    }

    // ========== RAPPORT METHODS  ==========
    public int getAccountCount(Long clientId) {
        if (!Validation.isValidId(clientId)) {
            System.err.println("Erreur : ID invalide");
            return 0;
        }

        return accountDAO.findByClientId(clientId).size();
    }

    public double getTotalBalance(Long clientId) {
        if (!Validation.isValidId(clientId)) {
            System.err.println("Erreur : ID invalide");
            return 0.0;
        }

        var accounts = accountDAO.findByClientId(clientId);

        return accounts.stream()
                .mapToDouble(BankAccount::getBalance)
                .sum();
    }

    public Optional<BankAccount> getMaxBalanceAccount(Long clientId) {
        if (!Validation.isValidId(clientId)) {
            System.err.println("Erreur : ID invalide");
            return Optional.empty();
        }

        var accounts = accountDAO.findByClientId(clientId);

        return accounts.stream()
                .max(Comparator.comparingDouble(BankAccount::getBalance));
    }

    public Optional<BankAccount> getMinBalanceAccount(Long clientId) {
        if (!Validation.isValidId(clientId)) {
            System.err.println("Erreur : ID invalide");
            return Optional.empty();
        }

        var accounts = accountDAO.findByClientId(clientId);

        return accounts.stream()
                .min(Comparator.comparingDouble(BankAccount::getBalance));
    }

    public void displayClientReport(Long clientId) {
        Optional<ClientRecord> clientOpt = findClientById(clientId);

        if (clientOpt.isEmpty()) {
            System.err.println("Client introuvable");
            return;
        }

        var client = clientOpt.get();
        var accounts = accountDAO.findByClientId(clientId);

        System.out.println("\n========== RAPPORT CLIENT ==========");
        System.out.println("ID : " + client.id());
        System.out.println("Nom : " + client.name());
        System.out.println("Email : " + client.email());
        System.out.println("Nombre de comptes : " + accounts.size());

        if (!accounts.isEmpty()) {
            double totalBalance = getTotalBalance(clientId);
            System.out.println("Solde total : " + String.format("%.2f", totalBalance) + " MAD");

            getMaxBalanceAccount(clientId).ifPresent(account ->
                    System.out.println("Compte avec solde max : " + account.getNumber() +
                            " (" + String.format("%.2f", account.getBalance()) + " MAD)")
            );

            getMinBalanceAccount(clientId).ifPresent(account ->
                    System.out.println("Compte avec solde min : " + account.getNumber() +
                            " (" + String.format("%.2f", account.getBalance()) + " MAD)")
            );
        } else {
            System.out.println("Ce client n'a aucun compte");
        }
        System.out.println("====================================\n");
    }
}