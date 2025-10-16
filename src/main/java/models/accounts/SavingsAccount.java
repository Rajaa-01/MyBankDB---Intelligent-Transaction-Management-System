package models.accounts;

public final class SavingsAccount extends BankAccount {
    private double interest;

    public SavingsAccount(Long id, String number, double balance, Long clientId, double interest) {
        super(id, number, balance, clientId, "SAVINGS");
        this.interest = interest;
    }

    public SavingsAccount(String number, double balance, Long clientId, double interest) {
        this(null, number, balance, clientId, interest);
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    @Override
    public String toString() {
        return "SavingsAccount{" + super.toString() + ", interest=" + interest + "%}";
    }
}