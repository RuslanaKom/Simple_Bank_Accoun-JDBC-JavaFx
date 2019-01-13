package controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Account;
import entities.User;
import repositories.Repository;

public class AccountController {

	private Repository repo = new Repository();
	private User currentUser;
	private Account currentAccount;

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}

	public boolean checkLoginAndPassword(String login, String password) {
		repo.connect();
		User user = repo.findUserByLogin(login);
		repo.disconnect();
		if (user != null && user.getPassword().equals(password)) {
			currentUser = user;
			return true;
		}
		return false;
	}

	public List<Integer> findUserAccounts() {
		List<Integer> accounts = new ArrayList();
		if (currentUser != null) {
			repo.connect();
			accounts = repo.findUserAccountsNumbers(currentUser);
			repo.disconnect();
		}
		return accounts;
	}

	public Account findUserAccount(int accountNumber) {
		repo.connect();
		currentAccount = repo.findAccountByNumber(accountNumber);
		repo.disconnect();
		return currentAccount;
	}

	public void changeBalance(Double deposit, Double withdrawal) throws SQLException {
		currentAccount.putMoney(deposit);
		currentAccount.takeMoney(withdrawal);
		repo.connect();
		repo.updateAccountBallance(currentAccount);
		repo.disconnect();
	}
}
