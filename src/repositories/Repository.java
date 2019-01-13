package repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import entities.Account;
import entities.CheckingAccount;
import entities.SavingsAccount;
import entities.User;

public class Repository {

	private Connection con;
	
	public void connect() {
		try {
			if (con == null || con.isClosed()) {
				Class.forName("org.h2.Driver");
				con = DriverManager.getConnection(
						"jdbc:h2:file:./data/bank.db",
						"sa", "");
			}
			if (!con.isClosed()) {
				System.out.println("we are connected");
			}
		} catch (Exception e) {
			System.out.println("Could not connect to database " + e);
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			System.out.println("Could not disconnect from database " + e);
			e.printStackTrace();
		}
	}
	
	public User findUserByLogin(String login) {
		User user = null;
		try {
			PreparedStatement st = con.prepareStatement("select * from USERS where LOGIN= ?");
			st.setString(1, login);
			ResultSet rs = st.executeQuery();

			if (rs.next()) {
				return mapToUser(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	
	public List<Integer> findUserAccountsNumbers(User user){
		List <Integer> list = new ArrayList();
		try {
			PreparedStatement st = con.prepareStatement("select ACCOUNT_ID from ACCOUNTS  where USER_ID = ?");
			st.setInt(1, user.getId());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt("ACCOUNT_ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Account findAccountByNumber(int accountNumber) {
		Account account = null;
		try {
			PreparedStatement st = con.prepareStatement("select * from ACCOUNTS  where ACCOUNT_ID =?");
			st.setInt(1, accountNumber);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return mapToAccount(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	public void updateAccountBallance (Account account) throws SQLException {
		con.setAutoCommit(false);
		try {
		PreparedStatement st = con.prepareStatement("update ACCOUNTS set BALANCE=? where ACCOUNT_ID =?");
		st.setDouble(1, account.getBalance());
		st.setInt(2, account.getId());
		st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.commit();
	}
	
	private User mapToUser(ResultSet rs) throws SQLException {
		return new User(rs.getInt("USER_ID"), rs.getString("FIRSTNAME"), rs.getString("LASTNAME"),
				rs.getString("LOGIN"), rs.getString("PASSWORD"));
	}
	
	private Account mapToAccount(ResultSet rs) throws SQLException {
		Account acc=null;
		if (rs.getString("TYPE").equals("S")) {
			acc=new SavingsAccount();
		}
		else {
			acc=new CheckingAccount();
		}
		acc.setId(rs.getInt("ACCOUNT_ID"));
		acc.setUserId(rs.getInt("USER_ID"));
		acc.setType(rs.getString("TYPE"));
		acc.setCurrency(rs.getString("CURRENCY"));
		acc.setBalance(rs.getDouble("BALANCE"));
		acc.setWithdrawLimit(rs.getDouble("WITHDRAWLIMIT"));
		acc.setDepositLimit(rs.getDouble("DEPOSITLIMIT"));
		return acc;
	}
}
