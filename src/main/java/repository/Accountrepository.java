package repository;

import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import models.accounts.BankAccount;
import models.accounts.CheckingAccount;
import models.accounts.SavingsAccount;

public class Accountrepository {

    // ========== CREATE ==========
    public Optional<BankAccount> create(BankAccount account) throws SQLException {
        String sql = "INSERT INTO account (number, balance, clientId, type, overdraft, interest) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, account.getNumber());
            stmt.setDouble(2, account.getBalance());
            stmt.setLong(3, account.getClientId());
            stmt.setString(4, account.getType());

            if (account instanceof CheckingAccount) {
                stmt.setDouble(5, ((CheckingAccount) account).getOverdraft());
                stmt.setNull(6, Types.DECIMAL);
            } else if (account instanceof SavingsAccount) {
                stmt.setNull(5, Types.DECIMAL);
                stmt.setDouble(6, ((SavingsAccount) account).getInterest());
            }

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Long generatedId = rs.getLong(1);
                account.setId(generatedId);
                return Optional.of(account);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de creation du compte");
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return Optional.empty();
    }

    // ========== UPDATE ==========
    public boolean update(BankAccount account) {
        String sql = "UPDATE account SET number = ?, balance = ?, clientId = ?, type = ?, overdraft = ?, interest = ? WHERE id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setString(1, account.getNumber());
            stmt.setDouble(2, account.getBalance());
            stmt.setLong(3, account.getClientId());
            stmt.setString(4, account.getType());

            if (account instanceof CheckingAccount) {
                stmt.setDouble(5, ((CheckingAccount) account).getOverdraft());
                stmt.setNull(6, Types.DECIMAL);
            } else if (account instanceof SavingsAccount) {
                stmt.setNull(5, Types.DECIMAL);
                stmt.setDouble(6, ((SavingsAccount) account).getInterest());
            }

            stmt.setLong(7, account.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erreur de la mise a jour");
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture des ressources: " + e.getMessage());
            }
        }
    }

    // ========== DELETE ==========
    public boolean delete(Long id) {
        String sql = "DELETE FROM account WHERE id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setLong(1, id);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Erreur de la suppression du compte");
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture des ressources: " + e.getMessage());
            }
        }
    }

    // ========== READ BY ID ==========
    public Optional<BankAccount> findById(Long id) {
        String sql = "SELECT id, number, balance, clientId, type, overdraft, interest FROM account WHERE id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                BankAccount account = mapResultSetToAccount(rs);
                return Optional.of(account);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recherche");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture des ressources: " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    // ========== READ BY CLIENT ID ==========
    public List<BankAccount> findByClientId(Long clientId) {
        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT id, number, balance, clientId, type, overdraft, interest FROM account WHERE clientId = ? ORDER BY id";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setLong(1, clientId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                BankAccount account = mapResultSetToAccount(rs);
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recherche par client");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture des ressources: " + e.getMessage());
            }
        }
        return accounts;
    }

    // ========== READ ALL ==========
    public List<BankAccount> findAll() {
        List<BankAccount> accounts = new ArrayList<>();
        String sql = "SELECT id, number, balance, clientId, type, overdraft, interest FROM account ORDER BY id";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                BankAccount account = mapResultSetToAccount(rs);
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recuperation des comptes");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture des ressources: " + e.getMessage());
            }
        }
        return accounts;
    }

    // ========== READ BY NUMBER ==========
    public Optional<BankAccount> findByNumber(String number) {
        String sql = "SELECT id, number, balance, clientId, type, overdraft, interest FROM account WHERE number = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setString(1, number);
            rs = stmt.executeQuery();

            if (rs.next()) {
                BankAccount account = mapResultSetToAccount(rs);
                return Optional.of(account);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recherche par numero");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture des ressources: " + e.getMessage());
            }
        }
        return Optional.empty();
    }

    // ========== HELPER METHOD ==========
    private BankAccount mapResultSetToAccount(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String number = rs.getString("number");
        double balance = rs.getDouble("balance");
        Long clientId = rs.getLong("clientId");
        String type = rs.getString("type");

        if ("CHECKING".equals(type)) {
            double overdraft = rs.getDouble("overdraft");
            return new CheckingAccount(id, number, balance, clientId, overdraft);
        } else if ("SAVINGS".equals(type)) {
            double interest = rs.getDouble("interest");
            return new SavingsAccount(id, number, balance, clientId, interest);
        }

        throw new SQLException("Type de compte inconnu : " + type);
    }

    // ========== GET LAST ACCOUNT NUMBER ==========
    public Optional<String> getLastAccountNumber() {
        String sql = "SELECT number FROM account ORDER BY id DESC LIMIT 1";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(rs.getString("number"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recuperation du dernier numero");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture des ressources: " + e.getMessage());
            }
        }
        return Optional.empty();
    }
}