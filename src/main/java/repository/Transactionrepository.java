package repository;

import enums.TranType;
import models.transactions.Transaction;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Transactionrepository {

    // ========== CREATE ==========
    public Optional<Transaction> create(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transaction (date, amount, type, location, accountId) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setTimestamp(1, Timestamp.valueOf(transaction.date()));
            stmt.setDouble(2, transaction.amount());
            stmt.setString(3, transaction.type().name());
            stmt.setString(4, transaction.location());
            stmt.setLong(5, transaction.accountId());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Long generatedId = rs.getLong(1);
                return Optional.of(new Transaction(
                        generatedId,
                        transaction.date(),
                        transaction.amount(),
                        transaction.type(),
                        transaction.location(),
                        transaction.accountId()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erreur de creation de la transaction");
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        }

        return Optional.empty();
    }

    // ========== UPDATE ==========
    public boolean update(Transaction transaction) {
        String sql = "UPDATE transaction SET date = ?, amount = ?, type = ?, location = ?, accountId = ? WHERE id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.valueOf(transaction.date()));
            stmt.setDouble(2, transaction.amount());
            stmt.setString(3, transaction.type().name());
            stmt.setString(4, transaction.location());
            stmt.setLong(5, transaction.accountId());
            stmt.setLong(6, transaction.id());

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
        String sql = "DELETE FROM transaction WHERE id = ?";
        PreparedStatement stmt = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setLong(1, id);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            System.err.println("Erreur de la suppression de la transaction");
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
    public Optional<Transaction> findById(Long id) {
        String sql = "SELECT id, date, amount, type, location, accountId FROM transaction WHERE id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                Transaction transaction = mapResultSetToTransaction(rs);
                return Optional.of(transaction);
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

    // ========== READ BY ACCOUNT ID ==========
    public List<Transaction> findByAccountId(Long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, date, amount, type, location, accountId FROM transaction WHERE accountId = ? ORDER BY date DESC";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setLong(1, accountId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = mapResultSetToTransaction(rs);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recherche par compte");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture des ressources: " + e.getMessage());
            }
        }
        return transactions;
    }

    // ========== READ BY CLIENT ID (avec jointure) ==========
    public List<Transaction> findByClientId(Long clientId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.id, t.date, t.amount, t.type, t.location, t.accountId " +
                "FROM transaction t " +
                "JOIN account a ON t.accountId = a.id " +
                "WHERE a.clientId = ? " +
                "ORDER BY t.date DESC";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            stmt.setLong(1, clientId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = mapResultSetToTransaction(rs);
                transactions.add(transaction);
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
        return transactions;
    }

    // ========== READ ALL ==========
    public List<Transaction> findAll() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT id, date, amount, type, location, accountId FROM transaction ORDER BY date DESC";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = DBConnection.getConnection().prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = mapResultSetToTransaction(rs);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.err.println("Erreur de recuperation des transactions");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.err.println("Erreur de fermeture des ressources: " + e.getMessage());
            }
        }
        return transactions;
    }

    // ========== HELPER METHOD ==========
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getLong("id"),
                rs.getTimestamp("date").toLocalDateTime(),
                rs.getDouble("amount"),
                TranType.valueOf(rs.getString("type")),
                rs.getString("location"),
                rs.getLong("accountId")
        );
    }
}