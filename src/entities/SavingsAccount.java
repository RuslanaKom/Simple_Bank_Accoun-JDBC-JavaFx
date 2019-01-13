package entities;

public class SavingsAccount extends Account {

	public SavingsAccount(int id, int userId, String type, String currency, Double balance, Double withdrawLimit,
			Double depositLimit) {
		super(id, userId, type, currency, balance, withdrawLimit, depositLimit);
	}

	public SavingsAccount() {
	}

	@Override
	public void putMoney(Double money) {
		if (money >= depositLimit) {
			balance += money;
		}
	}

	@Override
	public void takeMoney(Double money) {
		if (money<=calculatePossibleWithdrawal()) {
			balance-=money;
		}
	}
	
	private Double calculatePossibleWithdrawal() {
		return balance*withdrawLimit/100;
	}

}
