package models.accounts;

public final class CheckingAccount extends BankAccount {
    private double overdraft;

    public CheckingAccount(Long id, String number, double balance, Long clientId, double overdraft) {
        super(id, number, balance, clientId, "CHECKING");
        this.overdraft = overdraft;}

    public CheckingAccount(String number, double balance, Long clientId, double overdraft) {
        this(null, number, balance, clientId, overdraft);
    }

    public double getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(double overdraft) {
        this.overdraft = overdraft;
    }

    @Override
    public String toString() {
        return "CheckingAccount{" + super.toString() + ", overdraft=" + overdraft + "}";
    }
}