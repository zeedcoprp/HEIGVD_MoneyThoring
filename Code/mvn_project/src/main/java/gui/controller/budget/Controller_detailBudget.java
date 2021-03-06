package gui.controller.budget;

import bll.logic.*;
import bll.model.IOTransactionModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.effects.JFXDepthManager;
import gui.controller.IController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static gui.controller.budget.Controller_listBudget.totalAmount;

/**
 * Budget detail controller. Gather every transaction related to a supervised
 * category between
 * the budget dates, and handle them in order to display them efficiently
 *
 * @author Bryan Curchod
 * @author François Burgener
 * @version 1.0
 */
public class Controller_detailBudget implements Initializable, IController {
	
	@FXML
	private Label lblTitre;
	@FXML
	private Label lblSolde;
	@FXML
	private Label lblPlafond;
	@FXML
	private JFXProgressBar progessBar;
	@FXML
	private JFXNodesList nodeModifDelete;
	@FXML
	private AnchorPane graphPane;
	@FXML
	private ScrollPane scrollpane;
	@FXML
	private AnchorPane paneForm;
	@FXML
	private JFXButton btnRetour;
	@FXML
	private AnchorPane paneTop;
	@FXML
	private GridPane paneBottom;
	@FXML
	private PieChart pieChart;
	@FXML
	private VBox transactionList;
	@FXML
	private Label label_creator;
	@FXML
	private Label startDate;
	@FXML
	private Label endDate;
	
	JFXButton btnEdit;
	JFXButton btnDelete;
	JFXButton btnMenu;
	
	BudgetLogic budget;
	IController parent;
	
	double outgo;
	
	/**
	 * Transaction displayer, display a transaction information
	 */
	private class transactionDisplayer {
		
		private Label lblDate;
		private Label lblCaption;
		private Label lblPrix;
		private GridPane paneDisplay;
		
		private IOTransactionModel transaction;
		private final String outgoColor = "#f2a7a8";
		
		/**
		 * Display conveniently a transaction
		 *
		 * @param t transaction to display
		 */
		transactionDisplayer(IOTransactionModel t) {
			
			transaction = t;
			lblDate = new Label(transaction.getDate().toString());
			lblCaption = new Label(transaction.getName());
			lblPrix = new Label(
					Double.toString(transaction.getAmount()) + " CHF");
			paneDisplay = new GridPane();
			
			paneDisplay.getChildren().add(lblDate);
			paneDisplay.getChildren().add(lblCaption);
			paneDisplay.getChildren().add(lblPrix);
			
			paneDisplay
					.setConstraints(lblDate, 0, 0, 1, 1, HPos.LEFT, VPos.CENTER,
							Priority.SOMETIMES, Priority.ALWAYS);
			paneDisplay.setConstraints(lblCaption, 1, 0, 1, 1, HPos.LEFT,
					VPos.CENTER, Priority.SOMETIMES, Priority.ALWAYS);
			paneDisplay.setConstraints(lblPrix, 2, 0, 1, 1, HPos.RIGHT,
					VPos.CENTER, Priority.SOMETIMES, Priority.ALWAYS);
			
			paneDisplay.setStyle(
					"-fx-background-radius: 10px; -fx-background-color: "
							+ outgoColor + ";");
			
			transactionList.getChildren().add(paneDisplay);
		}
	}
	
	/**
	 * controlle constructor
	 *
	 * @param p controller that called the detail view
	 * @param b budget to display
	 * @param outgo total outgo amount of the budget
	 */
	public Controller_detailBudget(IController p, BudgetLogic b, double outgo) {
		
		parent = p;
		budget = b;
		this.outgo = outgo;
	}
	
	/**
	 * Called to initialize a controller after its root element has been
	 * completely processed.
	 * create the control button (edit/delete) and set datas in the transactions
	 * list and the PieChart
	 *
	 * @param location The location used to resolve relative paths for the
	 * 		root object, or null if the location is not
	 * 		known.
	 * @param resources The resources used to localize the root object, or
	 * 		null if the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		scrollpane.setStyle("-fx-background-color: transparent");
		paneForm.setVisible(false);
		paneForm.setMouseTransparent(true);
		
		// control button management
		btnEdit = new JFXButton();
		btnEdit.setButtonType(JFXButton.ButtonType.FLAT);
		btnEdit.getStyleClass()
				.addAll("setting-button", "RoundButton", "GreenButton");
		btnEdit.setOnAction(event -> callForm(budget));
		
		btnDelete = new JFXButton();
		btnDelete.setButtonType(JFXButton.ButtonType.FLAT);
		btnDelete.getStyleClass()
				.addAll("setting-button", "RoundButton", "RedButton");
		btnDelete.setOnAction(event -> deleteItem(budget));
		
		btnMenu = new JFXButton();
		btnMenu.setButtonType(JFXButton.ButtonType.FLAT);
		btnMenu.getStyleClass()
				.addAll("setting-button", "RoundButton", "NeutralButton");
		
		nodeModifDelete.addAnimatedNode(btnMenu);
		nodeModifDelete.addAnimatedNode(btnEdit);
		nodeModifDelete.addAnimatedNode(btnDelete);
		nodeModifDelete.setSpacing(5d);
		JFXDepthManager.setDepth(nodeModifDelete, 1);
		
		ImageView image = new ImageView(new Image(
				getClass().getResourceAsStream("/gui/Image/preference.png")));
		image.setFitWidth(20);
		image.setFitHeight(20);
		btnMenu.setGraphic(image);
		
		image = new ImageView(new Image(
				getClass().getResourceAsStream("/gui/Image/edit.png")));
		image.setFitWidth(20);
		image.setFitHeight(20);
		btnEdit.setGraphic(image);
		
		image = new ImageView(new Image(
				getClass().getResourceAsStream("/gui/Image/delete.png")));
		image.setFitWidth(20);
		image.setFitHeight(20);
		btnDelete.setGraphic(image);
		
		image = new ImageView(new Image(
				getClass().getResourceAsStream("/gui/Image/return.png")));
		image.setFitWidth(30);
		image.setFitHeight(30);
		btnRetour.setGraphic(image);
		btnRetour.setText("");
		btnRetour.getStyleClass().add("RoundButton");
		btnRetour.setOnAction(event -> parent.modifyItem(budget));
		
		lblTitre.setText(budget.getName());
		lblPlafond.setText(String.valueOf(budget.getAmount()) + " CHF");
		
		JFXDepthManager.setDepth(paneTop, 1);
		JFXDepthManager.setDepth(paneBottom, 1);
		
		lblSolde.setText(Double.toString(budget.getAmount() + outgo) + " CHF");
		
		double pourcentage = Math.abs(outgo / budget.getAmount());
		progessBar.setProgress(pourcentage);
		label_creator.setText(budget.getCreator().getUsername());
		
		if (ClientLogic.getInstance().getId() != budget.getCreator().getId()) {
			btnEdit.setDisable(true);
		}
		
		startDate.setText(budget.getStartingDate().toString());
		endDate.setText(budget.getEndingDate().toString());
		setDataPieChart();
		setListTransaction();
		
	}
	
	/**
	 * call and open the budgetForm to edit the budget
	 *
	 * @param b
	 */
	private void callForm(BudgetLogic b) {
		
		paneForm.setVisible(true);
		paneForm.setMouseTransparent(false);
		FXMLLoader loader = new FXMLLoader(
				getClass().getResource("/gui/view/formBudget.fxml"));
		loader.setController(new Controller_formBudget(this, b));
		
		paneForm.getChildren().clear();
		try {
			paneForm.getChildren().add(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * clear the form
	 *
	 * @param result not used
	 */
	@Override
	public void createItem(Object result) {
		
		unloadform();
	}
	
	/**
	 * request a deletion of the budget to the parent
	 *
	 * @param toDelete object to delete
	 */
	@Override
	public void deleteItem(Object toDelete) {
		
		parent.deleteItem(toDelete);
	}
	
	/**
	 * update the budget's information
	 *
	 * @param toUpdated updated object
	 */
	@Override
	public void modifyItem(Object toUpdated) {
		
		unloadform();
		BudgetLogic bl = (BudgetLogic) toUpdated;
		lblTitre.setText(bl.getName());
		lblPlafond.setText(String.valueOf(bl.getAmount()) + " CHF");
		
		outgo = totalAmount(bl);
		lblSolde.setText(Double.toString(budget.getAmount() + outgo) + " CHF");
		
		double pourcentage = Math.abs(outgo / budget.getAmount());
		progessBar.setProgress(pourcentage);
		setDataPieChart();
		setListTransaction();
	}
	
	/**
	 * set up the transaction list
	 */
	private void setListTransaction() {
		
		transactionList.getChildren().clear();
		LocalDate begin = budget.getStartingDate().toLocalDate().minusDays(1);
		LocalDate end = budget.getEndingDate().toLocalDate().plusDays(1);
		
		if (!budget.isShared()) {
			if (!budget.getCategoriesBudget().isEmpty()) {
				
				for (CategoryLogic cl : budget.getCategoriesBudget()) {
					
					if (IOTransactionLogic.getTransactionsByCategory()
							.containsKey(cl)) {
						
						for (IOTransactionLogic tr : IOTransactionLogic
								.getTransactionsByCategory().get(cl)) {
							
							LocalDate currentDate = tr.getDate().toLocalDate();
							if (currentDate.isAfter(begin) && currentDate
									.isBefore(end) && !tr.isIncome()) {
								
								transactionDisplayer td
										= new transactionDisplayer(tr);
							}
						}
					}
				}
			} else {
				for (BankAccountLogic bal : ClientLogic.getInstance()
						.getBankAccounts()) {
					for (Object year : IOTransactionLogic
							.getYearsWithTransactions().toArray()) {
						for (int i = 0; i < 12; ++i) {
							if (bal.getTransactions().containsKey(year)) {
								for (IOTransactionLogic tr : bal
										.getTransactions()
										.get((Integer) year)[i]) {
									LocalDate currentDate = tr.getDate()
											.toLocalDate();
									if (currentDate.isAfter(begin)
											&& currentDate.isBefore(end) && !tr
											.isIncome()) {
										
										transactionDisplayer td
												= new transactionDisplayer(tr);
									}
								}
							}
						}
					}
				}
			}
		} else {
			for (IOTransactionModel tr : IOTransactionLogic
					.getIOTransactionByBudget(budget.getId())) {
				LocalDate currentDate = tr.getDate().toLocalDate();
				if (currentDate.isAfter(begin) && currentDate.isBefore(end)
						&& !tr.isIncome()) {
					
					transactionDisplayer td = new transactionDisplayer(tr);
				}
			}
		}
	}
	
	/**
	 * set the PieChart datas
	 */
	private void setDataPieChart() {
		
		pieChart.setTitle("Dépense par catégorie");
		
		
		if (!budget.isShared()) {
			if (!budget.getCategoriesBudget().isEmpty()) {
				setData(budget.getCategoriesBudget());
			} else {
				setData(ClientLogic.getInstance().getCategories());
			}
		}
	}
	
	/**
	 * generate the PieChart data for a given category
	 *
	 * @param categories category to handle
	 */
	private void setData(List<CategoryLogic> categories) {
		
		ObservableList<PieChart.Data> pieChartData = FXCollections
				.observableArrayList();
		
		LocalDate begin = budget.getStartingDate().toLocalDate().minusDays(1);
		LocalDate end = budget.getEndingDate().toLocalDate().plusDays(1);
		
		for (CategoryLogic cl : categories) {
			double outgo = 0;
			if (IOTransactionLogic.getTransactionsByCategory()
					.containsKey(cl)) {
				
				for (IOTransactionLogic tr : IOTransactionLogic
						.getTransactionsByCategory().get(cl)) {
					
					LocalDate currentDate = tr.getDate().toLocalDate();
					if (currentDate.isAfter(begin) && currentDate
							.isBefore(end)) {
						
						if (!tr.isIncome()) {
							
							outgo += tr.getAmount() * (-1);
						}
					}
				}
				pieChartData.add(new PieChart.Data(cl.getName(), outgo));
			}
		}
		pieChartData.forEach(data -> data.nameProperty().bind(Bindings
				.concat(data.getName(), " ", data.pieValueProperty(), " CHF")));
		
		pieChart.setData(pieChartData);
		pieChart.setLegendVisible(false);
	}
	
	/**
	 * clear the PaneForm
	 */
	private void unloadform() {
		
		paneForm.getChildren().clear();
		paneForm.setMouseTransparent(true);
		paneForm.setVisible(false);
	}
}
