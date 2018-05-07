package bll.logic;

import bll.model.IOTransactionModel;
import dal.dalexception.DALException;
import dal.orm.IORM;
import dal.orm.PgORM;

import java.sql.Date;

/**
 * TODO
 *
 * @author Daniel Gonzalez Lopez, Héléna Line Reymond
 * @version 1.0
 */
public class IOTransactionLogic extends IOTransactionModel {

    private CategoryLogic category;


    public IOTransactionLogic() {}
    
    /**
     * TODO
     * @param amount
     * @param name
     * @param description
     * @param date
     * @param currency
     * @param category
     * @param bankAccount
     */
    public IOTransactionLogic(double amount, String name, String description,
                              Date date, String currency, CategoryLogic category,
                              BankAccountLogic bankAccount) {

        super(amount, name, description, date, currency, (amount >= 0));

        this.category = category;
        
        setCategoryID(category.getId());

        bankAccount.addTransaction(this);
        
        createIOTransaction(new PgORM());
    }
    
    /**
     * TODO
     */
    public void update(double amount, String name, String description,
		    Date date, String currency, CategoryLogic category) {
        
        setAmount(amount);
        setName(name);
        setDescription(description);
        setDate(date);
        setCurrency(currency);
        
        this.category = category;
	    setCategoryID(category.getId());
        
        updateIOTransaction(new PgORM());
    }
    
    public void supp() {
        
        try {
            IORM orm = new PgORM();
            
            orm.beginTransaction();
            
            orm.getIotransactionRepository().delete(getId());
            orm.commit();
            
        } catch (DALException e) {
            e.printStackTrace();
        }
    }
}
