package bll.logic;

import bll.mappers.DAL.DALCategoryBudgetMapper;
import bll.mappers.DAL.DALClientMapper;
import bll.mappers.DAL.DALSharedBudgetMapper;
import bll.model.BudgetModel;
import bll.model.CategoryBudgetModel;
import bll.model.ClientModel;
import bll.model.SharedBudgetModel;
import dal.dalexception.DALException;
import dal.ientites.IDALCategoriesbudgetEntity;
import dal.ientites.IDALClientEntity;
import dal.ientites.IDALSharedbudgetEntity;
import dal.irepositories.IBudgetRepository;
import dal.irepositories.ICategoriesBudgetRepository;
import dal.irepositories.ISharedBudgetRepository;
import dal.orm.IORM;
import dal.orm.MasterORM;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * BudgetLogic class.
 * Implements the business logic of the BudgetModel and the SharedBudgetModel.
 * Implements methods to create, change and delete budgets and some
 * methods concerning categories and / or linked users.
 * Before changing these attributes, the methods check their integrity
 * to avoid data problems.
 *
 * @author Daniel Gonzalez Lopez
 * @author Héléna Line Reymond
 * @version 2.0
 */
public class BudgetLogic extends BudgetModel {
	
	// Categories of the budget
	private ArrayList<CategoryLogic> categories = new ArrayList<>();
	
	// Clients of the shared budget
	private ArrayList<ClientModel> clients = new ArrayList<>();
	
	/**
	 * Construct an instance and link the budget to the user instance.
	 */
	public BudgetLogic() {
		
		ClientLogic.getInstance().addBudget(this);
	}
	
	/**
	 * Construct an instance of budget. Link it to the user instance and create
	 * the entry in the database.
	 *
	 * @param name
	 * @param amount
	 * @param isShared Whether the budget is shared or not
	 * @param isRecurrent (Not implemented, always false)
	 * @param startingDate
	 * @param endingDate
	 * @param gap (for recurrence, not used for now)
	 * @param categoryList
	 * @param clientList
	 */
	public BudgetLogic(String name, double amount, boolean isShared,
			boolean isRecurrent, Date startingDate, Date endingDate, int gap,
			ArrayList<CategoryLogic> categoryList,
			ArrayList<ClientModel> clientList) {
		
		super(name, amount, isShared, isRecurrent, startingDate, endingDate,
				gap);
		
		setCategoriesBudget(categoryList);
		
		if (isShared()) {
			setClientsBudget(clientList);
		}
		
		ClientLogic.getInstance().addBudget(this);
		
		// Get the instance of the ORM
		IORM orm = MasterORM.getInstance().getORM();
		
		// Create the budget in the database
		createBudget(orm);
		
		// Update the categories <-> budget list table in the database.
		updateCategoriesBudget(orm);
		
		if (isShared()) {
			
			// Update the users <-> budget list table in database.
			updateClientsBudget(orm);
		}
	}
	
	/**
	 * Update the budget.
	 */
	public void update(String name, double amount, boolean isShared,
			boolean isRecurrent, Date startingDate, Date endingDate, int gap,
			ArrayList<CategoryLogic> categoryList,
			ArrayList<ClientModel> clientList) {
		
		setName(name);
		setAmount(amount);
		setShared(isShared);
		setRecurrent(isRecurrent);
		setStartingDate(startingDate);
		setEndingDate(endingDate);
		setGap(gap);
		setCategoriesBudget(categoryList);
		
		if (isShared()) {
			setClientsBudget(clientList);
		}
		
		IORM orm = MasterORM.getInstance().getORM();
		
		// Update the budget in the database
		updateBudget(orm);
		
		// Update the categories <-> budget list table in the database.
		updateCategoriesBudget(orm);
		
		if (isShared()) {
			
			// Update the users <-> budget list table in database.
			updateClientsBudget(orm);
		}
	}
	
	/**
	 * Remove the budget from the database only if the budget isn't shared.
	 * Otherwise change the creator or remove the link between the client and
	 * the budget.
	 */
	public void supp() {
		
		IORM orm = MasterORM.getInstance().getORM();
		
		try {
			orm.beginTransaction();
			
			IBudgetRepository repo = orm.getBudgetRepository();
			
			// Delete the budget if it isn't shared
			// Or
			// It is shared, but there is only the creator working on it
			if (!isShared() || (isShared() && getClientID() == ClientLogic
					.getInstance().getId() && clients.isEmpty())) {
				repo.delete(getId());
			} else {
				
				ISharedBudgetRepository repoS = orm.getSharedBudgetRepository();
				
				// If the client is the creator
				// Change the creator
				if (getClientID() == ClientLogic.getInstance().getId()) {
					
					ClientModel newCreator = clients.get(0);
					setClientID(newCreator.getId());
					
					// Remove the link between the new creator and the budget
					IDALSharedbudgetEntity sb = repoS
							.getSharedbudget(newCreator.getId(), getId());
					repoS.delete(sb);
				} else {
					
					// Remove the link between the client and the budget
					IDALSharedbudgetEntity sb = repoS
							.getSharedbudget(ClientLogic.getInstance().getId(),
									getId());
					clients.remove(ClientLogic.getInstance());
					repoS.delete(sb);
				}
			}
			
			orm.commit();
			
			// Delete the budget from the client
			ClientLogic.getInstance().removeBudget(this);
			
			// Update the database
			updateBudget(orm);
			
		} catch (DALException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get the creator of the budget.
	 *
	 * @return creator of the budget.
	 */
	public ClientModel getCreator() {
		
		ClientModel creator = null;
		IORM orm = MasterORM.getInstance().getORM();
		
		try {
			orm.beginTransaction();
			
			IDALClientEntity creatorEntity = orm.getClientRepository()
					.getClient(getClientID());
			creator = DALClientMapper.toClientModel(creatorEntity);
			
		} catch (DALException e) {
			e.printStackTrace();
		}
		
		return creator;
	}
	
	/**
	 * Get the categories of the budget.
	 *
	 * @return categories of the budget.
	 */
	public ArrayList<CategoryLogic> getCategoriesBudget() {
		
		return categories;
	}
	
	/**
	 * Get the clients of the shared budget.
	 *
	 * @return clients of the budget.
	 */
	public ArrayList<ClientModel> getClientsBudget() {
		
		return clients;
	}
	
	/**
	 * Set the categories of the budget.
	 *
	 * @param categoryList Categories of the budget.
	 */
	private void setCategoriesBudget(ArrayList<CategoryLogic> categoryList) {
		
		categories.clear();
		
		for (CategoryLogic cat : categoryList) {
			categories.add(cat);
		}
	}
	
	/**
	 * Set the clients of the shared budget.
	 *
	 * @param clientList Clients of the budget.
	 */
	private void setClientsBudget(ArrayList<ClientModel> clientList) {
		
		clients.clear();
		
		for (ClientModel client : clientList) {
			clients.add(client);
		}
	}
	
	/**
	 * Remove the given category from the list.
	 *
	 * @param category category to remove.
	 */
	public void removeCategory(CategoryLogic category) {
		
		categories.remove(category);
	}
	
	/**
	 * Update the categories of the budget.
	 */
	private void updateCategoriesBudget(IORM orm) {
		
		try {
			orm.beginTransaction();
			
			ICategoriesBudgetRepository repo = orm
					.getCategoriesBudgetRepository();
			
			// Delete CategoriesBudget for the id of the budget
			repo.delete(getId());
			
			// Add the new CategoriesBudget list
			for (CategoryLogic category : categories) {
				
				CategoryBudgetModel cat = new CategoryBudgetModel(
						category.getId(), getId());
				repo.addCategoriesBudget(DALCategoryBudgetMapper.toDbo(cat));
			}
			
			orm.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Update the clients of the shared budget.
	 */
	private void updateClientsBudget(IORM orm) {
		
		try {
			orm.beginTransaction();
			
			ISharedBudgetRepository repo = orm.getSharedBudgetRepository();
			
			// Delete all SharedBudget for the id of the budget
			repo.delete(getId());
			
			// Add the new SharedBudget list
			for (ClientModel client : clients) {
				
				SharedBudgetModel cl = new SharedBudgetModel(client.getId(),
						getId());
				repo.addSharedbudget(DALSharedBudgetMapper.toDboPG(cl));
			}
			
			orm.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Update the categories and the clients of the budget from the DB.
	 */
	protected void setDataFromDB(IORM orm) {
		
		try {
			
			// Get the categories of the budget
			List<IDALCategoriesbudgetEntity> cb = orm
					.getCategoriesBudgetRepository()
					.getCategoriesBudgetByBudget(getId());
			
			for (CategoryBudgetModel b : DALCategoryBudgetMapper.toBos(cb)) {
				categories.add(CategoryLogic.getByID(b.getCategoryID()));
			}
			
			// Get the clients of the shared budget
			if (isShared()) {
				List<IDALSharedbudgetEntity> sb = orm
						.getSharedBudgetRepository()
						.getSharedbudgetByBudget(getId());
				
				for (SharedBudgetModel s : DALSharedBudgetMapper.toBos(sb)) {
					ClientModel c = DALClientMapper.toClientModel(
							orm.getClientRepository()
									.getClient(s.getClientID()));
					clients.add(c);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a representation of the budget (its name)
	 *
	 * @return String representing the budget.
	 */
	@Override
	public String toString() {
		
		return this.getName();
	}
}