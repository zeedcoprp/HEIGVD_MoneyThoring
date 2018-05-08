package gui.controller;

import bll.logic.BankAccountLogic;
import bll.logic.ClientLogic;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller_detailBankAccount implements Initializable, IController {
	
	
	@FXML private Label name;
	@FXML private Label nameBankAccount;
	@FXML private Label typeBankAccount;
	@FXML private Label dateLastTransaction;
	@FXML private Label amountBankAccount;
	@FXML private LineChart<?, ?> lineChart;
	@FXML private CategoryAxis axeX;
	@FXML private NumberAxis axeY;
	@FXML private JFXButton returnButton;
	@FXML private JFXNodesList nodelist;
	@FXML private AnchorPane paneform;
	@FXML private CheckBox chkDefaultAccount;
	
	private JFXButton preferenceButton;
	private JFXButton modifyButton;
	private JFXButton removeButton;
	
	private Controller_bankAccount cba;
	private BankAccountLogic bal;
	
	/**
	 * Constructor of our controller
	 *
	 * @param cba TODO
	 * @param bal TODO
	 */
	public Controller_detailBankAccount(Controller_bankAccount cba, BankAccountLogic bal) {
		
		this.bal = bal;
		this.cba = cba;
	}
	
	private void returnFrame() {
		
		cba.modifyItem(bal);
	}
	
	private void generateNodeList() {
		
		preferenceButton = new JFXButton();
		preferenceButton.setButtonType(JFXButton.ButtonType.RAISED);
		modifyButton = new JFXButton("M");
		modifyButton.setButtonType(JFXButton.ButtonType.RAISED);
		modifyButton.getStyleClass().addAll("preference-button", "preference-button-sub");
		removeButton = new JFXButton("X");
		removeButton.setButtonType(JFXButton.ButtonType.RAISED);
		removeButton.getStyleClass().addAll("preference-button", "preference-button-sub");
		
		nodelist.addAnimatedNode(preferenceButton);
		nodelist.addAnimatedNode(modifyButton);
		nodelist.addAnimatedNode(removeButton);
		nodelist.setSpacing(5d);
		
		ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/gui/Image/preference.png")));
		image.setFitWidth(25);
		image.setFitHeight(25);
		preferenceButton.setGraphic(image);
	}
	
	private void removeBankAccount() {
		
		bal.supp();
		cba.deleteItem(bal);
	}
	
	private void modifyBankAccount() {
		
		/* we load the form fxml*/
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/view/formBankAccount.fxml"));
		
		/*Create a instance of the controller of bank account form*/
		Controller_formBankAccount cba = new Controller_formBankAccount(this, bal);
		
		/*Sets the controller associated with the root object*/
		loader.setController(cba);
		paneform.setVisible(true);
		paneform.setMouseTransparent(false);
		
		try {
			AnchorPane pane = loader.load();
			paneform.getChildren().add(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override public void createItem(Object result) {
		
		paneform.getChildren().clear();
		paneform.setMouseTransparent(true);
		paneform.setVisible(false);
	}
	
	@Override public void deleteItem(Object toDelete) {
	
	}
	
	@Override public void modifyItem(Object toUpdated) {
		
		BankAccountLogic bal = (BankAccountLogic) toUpdated;
		this.name.setText(bal.getName());
		this.nameBankAccount.setText(bal.getBankName());
		this.typeBankAccount.setText(bal.getType());
		this.amountBankAccount.setText(String.valueOf(bal.getAmount()));
		chkDefaultAccount.setSelected(bal.isDefault());
	}
	
	/**
	 * Called to initialize a controller after its root element has been completely processed.
	 *
	 * @param location The location used to resolve relative paths for the root object, or null if the location is not
	 * 		known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	@Override public void initialize(URL location, ResourceBundle resources) {
		
		paneform.setVisible(false);
		paneform.setMouseTransparent(true);
		
		ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/gui/Image/return.png")));
		image.setFitWidth(48);
		image.setFitHeight(36);
		returnButton.setGraphic(image);
		
		generateNodeList();
		/*Set the name of the account*/
		name.setText(bal.getName());
		
		/*Set the name of the bank account*/
		nameBankAccount.setText(bal.getBankName());
		
		/*Set the type of the account*/
		typeBankAccount.setText(bal.getType());
		
		//if the list of transaction is not empty, we get the last transaction date
		if (!bal.getTransactions().isEmpty()) {
			//dateLastTransaction
					//.setText(bal.getTransactions().get(bal.getTransactions().size() - 1).getDate().toString());
		}
		
		/*Change the color if the amount is bigger or lesser than 0*/
		if (bal.getAmount() >= 0) {
			amountBankAccount.setTextFill(Color.GREEN);
		} else {
			amountBankAccount.setTextFill(Color.RED);
		}
		
		/*Set the amount of the bank account*/
		amountBankAccount.setText(String.valueOf(bal.getAmount()) + " CHF");
		chkDefaultAccount.setSelected(bal.isDefault());
		chkDefaultAccount.setMouseTransparent(true);
		
		/*TODO mettre les données dans le graphique*/
		/*TODO max min des valeur de l'axe Y*/
		XYChart.Series series = new XYChart.Series();
		series.setName("Evolution du solde");
		Random random = new Random();
		for (int i = 0; i < 31; ++i) {
			int value = 15000 + random.nextInt(5000);
			series.getData().add(new XYChart.Data(String.valueOf(i + 1), value));
		}
		
		lineChart.getData().addAll(series);
		
		modifyButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override public void handle(ActionEvent event) {
				
				modifyBankAccount();
			}
		});
		
		removeButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override public void handle(ActionEvent event) {
				
				removeBankAccount();
			}
		});
		
		returnButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override public void handle(ActionEvent event) {
				
				returnFrame();
			}
		});
		
	}
}
