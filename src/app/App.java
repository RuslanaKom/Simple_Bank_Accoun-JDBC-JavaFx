package app;

import java.sql.SQLException;

import org.junit.platform.commons.util.StringUtils;

import controllers.AccountController;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			AccountController accController = new AccountController();

			Button buttonLogin = new Button("Login");
			TextField textLogin = new TextField();
			PasswordField textPassword = new PasswordField();

			Label labelLogin = new Label("Login:");
			Label labelPasssword = new Label("Password:");

			GridPane gridPane1 = new GridPane();
			gridPane1.setVgap(5);
			gridPane1.setHgap(5);
			gridPane1.setAlignment(Pos.TOP_CENTER);

			gridPane1.add(labelLogin, 1, 2);
			gridPane1.add(textLogin, 2, 2);
			gridPane1.add(labelPasssword, 1, 3);
			gridPane1.add(textPassword, 2, 3);
			gridPane1.add(buttonLogin, 2, 4);

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Login message");
			alert.setHeaderText("Login failed!");
			alert.setContentText("Your login or password is incorrect.");

			EventHandler<ActionEvent> LoginButtonEventHandler = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					if (textLogin != null) {
						if (accController.checkLoginAndPassword(textLogin.getText(), textPassword.getText())) {
							createSecondStage(accController,primaryStage).show();
							textLogin.clear();
							textPassword.clear();
							primaryStage.close();
						} else {
							alert.showAndWait();
						}
						;
					}
				}
			};

			buttonLogin.setOnAction(LoginButtonEventHandler);

			Scene scene = new Scene(gridPane1, 280, 350);
			primaryStage.setScene(scene);
			primaryStage.setTitle("BANK");
			primaryStage.show();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Stage createSecondStage(AccountController accController, Stage primaryStage) {
		
		GridPane gridPane2 = new GridPane();
		gridPane2.setVgap(5);
		gridPane2.setHgap(5);
		gridPane2.setAlignment(Pos.TOP_CENTER);

		Stage stage = new Stage();
		stage.setTitle("You are logged in as "+ accController.getCurrentUser().getFirstName()+" "+ accController.getCurrentUser().getLastName());
		
		ObservableList<Integer> accountNumbers =
				FXCollections.observableArrayList(accController.findUserAccounts());
		System.out.println(accountNumbers);
		final ComboBox comboBox = new ComboBox(accountNumbers);
		
		Label labelAccounts = new Label("Accounts:");
		Label labelBalance = new Label("Balance:");
		Label labelAccInfo = new Label("Account information:");
		
		Text accInfo = new Text();
		Text balance = new Text();
		
		gridPane2.add(labelAccounts, 1, 2);
		gridPane2.add(comboBox, 2, 2);
		gridPane2.add(labelAccInfo, 1, 3);
		gridPane2.add(accInfo, 2, 3);
		gridPane2.add(labelBalance, 1, 4);
		gridPane2.add(balance, 2, 4);
		
		EventHandler<ActionEvent> AccountSelectionEventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if(comboBox.getValue()!=null){
				accController.findUserAccount((int)comboBox.getValue());
				balance.setText(accController.getCurrentAccount().getBalance().toString()+ " " + accController.getCurrentAccount().getCurrency());
				accInfo.setText(accController.getCurrentAccount().getType().equals("S")?"Savings Account":"Checking Account");
				}
			}
		};

	
		comboBox.setOnAction(AccountSelectionEventHandler);
		
		TextField textMoneyTake = new TextField();
		TextField textMoneyPut = new TextField();
		Button buttonTake = new Button("Withdraw");
		Button buttonPut = new Button("Deposit");
		
		gridPane2.add(textMoneyTake, 1, 6);
		gridPane2.add(textMoneyPut, 1, 7);
		gridPane2.add(buttonTake, 2, 6);
		gridPane2.add(buttonPut, 2, 7);
		
		ChangeListener<String> changeListener1 = new ChangeListener <String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("[\\d*,.]")) {
		        	textMoneyTake.setText(newValue.replaceAll("[^\\d,^.]", ""));
		        }
		    }
		};
		
		ChangeListener<String> changeListener2 = new ChangeListener <String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		    	 if (!newValue.matches("[\\d*,.]")) {
		        	textMoneyPut.setText(newValue.replaceAll("[^\\d,^.]", ""));
		        }
		    }
		};
		
		textMoneyTake.textProperty().addListener(changeListener1);
		textMoneyPut.textProperty().addListener(changeListener2);
		
		EventHandler<ActionEvent> BalanceChangeEventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Double withdrawAmout=StringUtils.isBlank(textMoneyTake.getText())?0D:Double.parseDouble(textMoneyTake.getText());
				Double depositAmout=StringUtils.isBlank(textMoneyPut.getText())?0D:Double.parseDouble(textMoneyPut.getText());
				try {
					accController.changeBalance(depositAmout, withdrawAmout);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				textMoneyTake.clear();
				textMoneyPut.clear();
				AccountSelectionEventHandler.handle(e);
			}
		};
		
		buttonTake.setOnAction(BalanceChangeEventHandler);
		buttonPut.setOnAction(BalanceChangeEventHandler);
		
		Button logout = new Button("Logout");
		gridPane2.add(logout, 8, 8);
		
		EventHandler<ActionEvent> LogoutEventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				stage.close();
				primaryStage.show();
				
			}
		};

		logout.setOnAction(LogoutEventHandler);
		
		stage.setScene(new Scene(gridPane2, 450, 450));
		
		
		return stage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
