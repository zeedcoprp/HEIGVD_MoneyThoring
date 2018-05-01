package bll.logic;

import bll.model.BankAccountModel;
import dal.ientites.IDALBankaccountEntity;

import java.util.ArrayList;

/**
 * ClientLogic class.
 * Implements the business logic of the ClientModel. The methods allow to
 * change the attributes of the client like his email, his username or his
 * password. Before changing these attributes, the methods check their integrity
 * to avoid data problems.
 *
 * @author Daniel Gonzalez Lopez
 * @version 1.1
 */
public class BankAccountLogic extends BankAccountModel {
	
	private ArrayList<IOTransactionLogic> transactions;
	
	
	/**
	 * TODO
	 *
	 * @param name
	 * @param bankName
	 * @param type
	 * @param amount
	 * @param isDefault
	 */
	public BankAccountLogic(String name, String bankName, String type, double amount, boolean isDefault, int clientID) {
		
		super(name, bankName, type, amount, isDefault, clientID);
		
		ClientLogic.getInstance().addBankAccount(this);
		transactions = new ArrayList<>();
	}
	
	public BankAccountLogic(IDALBankaccountEntity bae) {
		
		setId(bae.getId());
		setName(bae.getName());
		setBankName(bae.getNamebank());
		setType(bae.getTypeaccount());
		setAmount(bae.getAmount());
		setDefault(bae.isIsdefault());
		setClientId(bae.getClientId());
		
		ClientLogic.getInstance().addBankAccount(this);
		transactions = new ArrayList<>();
		
	}
	
	public String toString() {
		
		return super.getName();
	}
	
	/**
	 * Increment the bank account by the value of the parameter.
	 *
	 * @param io Income to formReturn to the amount.
	 */
	private void updateAmount(double io) {
		
		setAmount(getAmount() + io);
	}
	
	/**
	 * Add the transaction to the transaction list of the bank account.
	 *
	 * @param transaction New transaction to formReturn to the list.
	 */
	public void addTransaction(IOTransactionLogic transaction) {
		
		transactions.add(transaction);
		transaction.setBankAccountID(getId());
		
		updateAmount(transaction.getAmount());
	}
	
	/**
	 * Get the list of transactions for this bank account.
	 *
	 * @return List of transactions.
	 */
	public ArrayList<IOTransactionLogic> getTransactions() {
		
		return transactions;
	}
	
	/**
	 * "Suppress" the bank account. It simply makes it invisible for the user.
	 */
	public void supp() {
		
		setVisible(false);
	}
}
