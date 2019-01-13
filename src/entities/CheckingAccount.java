package entities;

public class CheckingAccount extends Account {

	public CheckingAccount(int id, int userId, String type, String currency, Double balance, Double withdrawLimit,
			Double depositLimit) {
		super(id, userId, type, currency, balance, withdrawLimit, depositLimit);
	}
	
	public CheckingAccount() {
	}

	@Override
	public void putMoney(Double money) {
		this.balance+=money;
	}

	@Override
	public void takeMoney(Double money) {
		if (this.balance>=money & money<=withdrawLimit) {
			this.balance-=money;
		}
	}
	
	
}
