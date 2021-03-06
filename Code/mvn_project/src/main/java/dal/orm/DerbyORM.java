package dal.orm;

import dal.dalexception.DALException;
import dal.irepositories.*;
import dal.repositories.derby.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * DerbyORM classe.
 * the classe who use all repositories, handle the session andtransaction into
 * each repositories
 *
 * @author Guillaume Zaretti
 * @version 1.2
 */
public class DerbyORM implements IORM {
	
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	
	private IClientRepository clientRepository;
	private IBankaccountRepository bankaccountRepository;
	private ICategoryRepository categoryRepository;
	private IBudgetRepository budgetRepository;
	private IDebtRepository debtRepository;
	private IotransactionDeRepository iotranscationRepository;
	private IRecurrenceRepository recurrenceRepository;
	private ICategoriesBudgetRepository categoriesBudgetReposisoty;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IClientRepository getClientRepository() {
		//if (clientRepository == null) {
		clientRepository = new ClientDeRepository(session, transaction);
		//}
		return clientRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IBankaccountRepository getBankaccountRepository() {
		// if(bankaccountRepository == null){
		bankaccountRepository = new BankaccountDeRepository(session,
				transaction);
		// }
		return bankaccountRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ICategoryRepository getCategoryRepository() {
		
		categoryRepository = new CategoryDeRepository(session, transaction);
		return categoryRepository;
	}
	
	/**
	 * Construct an single instance of budgetrepostiory and return it
	 *
	 * @return an instance of IBudgetRepository
	 */
	@Override
	public IBudgetRepository getBudgetRepository() {
		
		budgetRepository = new BudgetDeRepository(session, transaction);
		return budgetRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDebtRepository getDebtRepository() {
		
		debtRepository = new DebtDeRepository(session, transaction);
		return debtRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRecurrenceRepository getRecurrenceRepository() {
		
		recurrenceRepository = new RecurrenceDeRepository(session, transaction);
		return recurrenceRepository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IIotransactionRepository getIotransactionRepository() {
		
		iotranscationRepository = new IotransactionDeRepository(session,
				transaction);
		return iotranscationRepository;
	}
	
	/**
	 * Construct an single instance of ICategoriesBudgetRepository
	 *
	 * @throws DALException
	 */
	@Override
	public ICategoriesBudgetRepository getCategoriesBudgetRepository() {
		
		categoriesBudgetReposisoty = new CategoryBudgetDeRepository(session,
				transaction);
		return categoriesBudgetReposisoty;
	}
	
	/**
	 * Construct an single instance of ISharedBudgetRepository
	 *
	 * @throws DALException
	 */
	@Override
	public ISharedBudgetRepository getSharedBudgetRepository() {
		
		return null;
	}
	
	private void openSession() throws DALException {
		//if(sessionFactory == null) {
		try {
			sessionFactory = new Configuration()
					.configure("hibernate.derby.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			throw new DALException(ex);
		}
		//}
		try {
			session = sessionFactory.openSession();
		} catch (Exception e) {
			throw new DALException(e);
		}
		
	}
	
	private void sessionClose() throws DALException {
		
		try {
			//session.flush();
			session.close();
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
