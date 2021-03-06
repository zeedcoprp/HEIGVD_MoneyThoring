package dal.orm;

import dal.dalexception.DALException;
import dal.irepositories.*;
import dal.repositories.pgsql.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * PgORM classe.
 * the classe who use all repositories, handle the session andtransaction into
 * each repositories
 *
 * @author Guillaume Zaretti
 * @version 1.2
 */
public class PgORM implements IORM {
	
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	
	private IClientRepository clientRepository;
	private IBankaccountRepository bankaccountRepository;
	private ICategoryRepository categoryRepository;
	private IBudgetRepository budgetRepository;
	private IDebtRepository debtRepository;
	private IIotransactionRepository iotransactionRepository;
	private IRecurrenceRepository recurrenceRepository;
	private ISharedBudgetRepository sharedBudgetRepository;
	private ICategoriesBudgetRepository categoriesBudgetRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IClientRepository getClientRepository() {
		//if (clientRepository == null) {
		clientRepository = new ClientPgRepository(session, transaction);
		// }
		return clientRepository;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBankaccountRepository getBankaccountRepository() {
		//if (bankaccountRepository == null) {
		bankaccountRepository = new BankaccountPgRepository(session,
				transaction);
		// }
		return bankaccountRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICategoryRepository getCategoryRepository() {
		
		categoryRepository = new CategoryPgRepository(session, transaction);
		return categoryRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBudgetRepository getBudgetRepository() {
		
		budgetRepository = new BudgetPgRepository(session, transaction);
		return budgetRepository;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDebtRepository getDebtRepository() {
		
		debtRepository = new DebtPgRepository(session, transaction);
		return debtRepository;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRecurrenceRepository getRecurrenceRepository() {
		
		recurrenceRepository = new RecurrencePgRepository(session, transaction);
		return recurrenceRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IIotransactionRepository getIotransactionRepository() {
		
		iotransactionRepository = new IotransactionPgRepository(session,
				transaction);
		return iotransactionRepository;
	}
	
	/**
	 * Construct an single instance of ICategoriesBudgetRepository
	 *
	 * @throws DALException
	 */
	@Override
	public ICategoriesBudgetRepository getCategoriesBudgetRepository() {
		
		categoriesBudgetRepository = new CategoryBudgetPgRepository(session,
				transaction);
		return categoriesBudgetRepository;
	}
	
	/**
	 * Construct an single instance of ISharedBudgetRepository
	 *
	 * @throws DALException
	 */
	@Override
	public ISharedBudgetRepository getSharedBudgetRepository() {
		
		sharedBudgetRepository = new SharedBudgetPgRepository(session,
				transaction);
		return sharedBudgetRepository;
	}
	
	private void openSession() throws DALException {
		
		try {
			sessionFactory = new Configuration()
					.configure("hibernate.pgsql.cfg.xml").buildSessionFactory();
		} catch (Exception e) {
			throw new DALException(e);
		}
		session = sessionFactory.openSession();
		
	}
	
	private void sessionClose() throws DALException {
		
		try {
			session.close();
			sessionFactory.close();
		} catch (HibernateException e) {
			throw new DALException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beginTransaction() throws DALException {
		
		try {
			openSession();
			transaction = session.beginTransaction();
			
		} catch (Exception e) {
			throw new DALException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rollback() throws DALException {
		
		try {
			transaction.rollback();
		} catch (Exception e) {
			throw new DALException(e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commit() throws DALException {
		
		try {
			transaction.commit();
			sessionClose();
		} catch (Exception e) {
			throw new DALException(e);
		}
		
	}
}
