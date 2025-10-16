package models.accounts;

public sealed abstract class BankAccount permits CheckingAccount, SavingsAccount {
    private Long id;
    private String number;
    private double balance;
    private Long clientId;
    private String type;

    public BankAccount(Long id, String number, double balance, Long clientId, String type) {
        this.id = id;
        this.number = number;
        this.balance = balance;
        this.clientId = clientId;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Account{id=" + id + ", number='" + number + "', balance=" + balance +
                ", clientId=" + clientId + ", type='" + type + "'}";
    }
}