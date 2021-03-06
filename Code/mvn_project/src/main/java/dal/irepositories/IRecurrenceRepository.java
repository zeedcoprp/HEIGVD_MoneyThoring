package dal.irepositories;

import dal.dalexception.DALException;
import dal.ientites.IDALRecurrenceEntity;

import java.util.List;

/**
 * IRecurrenceRepository give the access methodes for handle the
 * recurrence into persistence
 *
 * @author Guillaume Zaretti
 * @version 1.2
 */
public interface IRecurrenceRepository {
	
	/**
	 * Retreave a recurrence by id
	 *
	 * @param id of the recurrence
	 *
	 * @return IDALRecurrenceEntity the recurrence
	 *
	 * @throws DALException
	 */
	public IDALRecurrenceEntity getRecurrence(int id) throws DALException;
	
	/**
	 * Retreave all recurrence
	 *
	 * @return List<IDALRecurrenceEntity> the list of recurrence
	 *
	 * @throws DALException
	 */
	public List<IDALRecurrenceEntity> getRecurrences() throws DALException;
	
	/**
	 * Retrieve the recurrence by transaction id
	 *
	 * @param id of the transaction
	 *
	 * @return List<IDALRecurrenceEntity>
	 *
	 * @throws DALException
	 */
	public IDALRecurrenceEntity getRecurrenceByTransaction(int id)
			throws DALException;
	
	/**
	 * Update recurrence
	 *
	 * @param recurrence the recurrence to update
	 *
	 * @throws DALException
	 */
	public void update(IDALRecurrenceEntity recurrence) throws DALException;
	
	/**
	 * add recurrence
	 *
	 * @param recurrence the recurrence to add
	 *
	 * @throws DALException
	 */
	public Integer addRecurrence(IDALRecurrenceEntity recurrence)
			throws DALException;
	
	/**
	 * delete recurrence by id
	 *
	 * @param id the recurrence ID to delete
	 *
	 * @throws DALException
	 */
	public void delete(int id) throws DALException;
}
