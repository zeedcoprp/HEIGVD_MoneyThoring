package bll.logic;

import bll.mappers.DAL.DALIOTransactionMapper;
import bll.model.BankAccountModel;
import dal.dalexception.DALException;
import dal.ientites.IDALIotransactionEntity;
import dal.orm.IORM;
import dal.orm.PgORM;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	
	private ArrayList<IOTransactionLogic> transactions = new ArrayList<>();
	
	private HashMap<Integer, ArrayList<IOTransactionLogic>[]> transactionsMap
			= new HashMap<>();
	
	
	public BankAccountLogic() {
		
		ClientLogic.getInstance().addBankAccount(this);
	}
	
	/**
	 * TODO
	 *
	 * @param name
	 * @param bankName
	 * @param type
	 * @param amount
	 * @param isDefault
	 */
	public BankAccountLogic(String name, String bankName, String type,
			double amount, boolean isDefault, int clientID) {
		
		super(name, bankName, type, amount, clientID);
		
		changeDefault(isDefault);
		
		ClientLogic.getInstance().addBankAccount(this);
		
		createBankAccount(new PgORM());
	}
	
	private void addToHashMap(IOTransactionLogic tl) {
		
		Date date = tl.getDate();
		int year = date.toLocalDate().getYear();
		int month = date.toLocalDate().getMonthValue();
		
		if (transactionsMap.containsKey(year)) {
			
			transactionsMap.get(year)[month].add(tl);
		} else {
			
			ArrayList<IOTransactionLogic>[] tab = new ArrayList[12];
			
			for (int i = 0; i < 12; ++i) {
				tab[i] = new ArrayList<>();
			}
			
			transactionsMap.put(year, tab);
			
			tab[month].add(tl);
		}
	}
	
	public String toString() {
		
		return getName();
	}
	
	/**
	 * Increment the bank account by the value of the parameter.
	 *
	 * @param io Income to createItem to the amount.
	 */
	private void updateAmount(double io) {
		
		setAmount(getAmount() + io);
	}
	
	/**
	 * Add the transaction to the transaction list of the bank account.
	 *
	 * @param transaction New transaction to createItem to the list.
	 */
	public void addTransaction(IOTransactionLogic transaction) {
		
		transactions.add(transaction);
		transaction.setBankAccountID(getId());
		addToHashMap(transaction);
		
		updateAmount(transaction.getAmount());
	}
	
	/**
	 * Get the list of transactions for this bank account.
	 *
	 * @return List of transactions.
	 */
	public HashMap<Integer, ArrayList<IOTransactionLogic>[]> getTransactions() {
		
		return transactionsMap;
	}
	
	/**
	 * "Suppress" the bank account. It simply makes it invisible for the user.
	 */
	public void supp() {
		
		setVisible(false);
		setDefault(false);
		updateBankAccount(new PgORM());
	}
	
	/**
	 * TODO
	 *
	 * @param isDefault
	 */
	private void changeDefault(boolean isDefault) {
		
		ClientLogic cl = ClientLogic.getInstance();
		
		if (isDefault) {
			List<BankAccountLogic> list = cl.getBankAccounts();
			
			for (BankAccountLogic ba : list) {
				if (ba.isDefault()) {
					ba.setDefault(false);
					ba.updateBankAccount(new PgORM());
					break;
				}
			}
		}
		
		setDefault(isDefault);
	}
	
	/**
	 * TODO
	 *
	 * @param name
	 * @param bankName
	 * @param type
	 * @param amount
	 * @param isDefault
	 */
	public void update(String name, String bankName, String type, double amount,
			boolean isDefault) {
		
		setName(name);
		setType(type);
		setBankName(bankName);
		setAmount(amount);
		changeDefault(isDefault);
		
		updateBankAccount(new PgORM());
	}
	
	/**
	 * TODO
	 */
	public void setDataFromDB() {
		
		try {
			IORM orm = new PgORM();
			
			orm.beginTransaction();
			
			List<IDALIotransactionEntity> ba = orm.getIotransactionRepository()
					.getIotransactionsByBankaccount(getId());
			
			transactions.addAll(DALIOTransactionMapper.toBos(ba));
			
		} catch (DALException e) {
			e.printStackTrace();
		}
	}
}
