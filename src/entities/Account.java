package entities;

public abstract class Account {
	
	protected int id;
	protected int userId;
	protected String type;
	protected String currency;
	protected Double balance; 
	protected Double withdrawLimit;
	protected Double depositLimit;
	
	public Account() {
	}
	
	public Account(int id, int userId, String type, String currency, Double balance, Double withdrawLimit,
			Double depositLimit) {
		super();
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.currency = currency;
		this.balance = balance;
		/*stores definite sum for checkingAccount and percents for saving account*/
		this.withdrawLimit = withdrawLimit; 
		this.depositLimit = depositLimit;
	}

	public Double getBalance() {
		return balance;
	}
	
	public abstract void putMoney(Double money);
	
	public abstract void takeMoney(Double money);

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getWithdrawLimit() {
		return withdrawLimit;
	}

	public void setWithdrawLimit(Double withdrawLimit) {
		this.withdrawLimit = withdrawLimit;
	}

	public Double getDepositLimit() {
		return depositLimit;
	}

	public void setDepositLimit(Double depositLimit) {
		this.depositLimit = depositLimit;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	
}
