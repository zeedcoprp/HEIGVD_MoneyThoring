package dal.repositories.derby;


import dal.dalexception.DALException;
import dal.entities.derby.ClientDeEntity;
import dal.entities.pgsql.ClientPgEntity;
import dal.ientites.IDALClientEntity;
import dal.irepositories.IClientRepository;
import dal.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.List;

public class ClientDeRepository implements IClientRepository {

    private Session session;
    private Transaction transaction;

    public  ClientDeRepository(Session session, Transaction transaction){
        this.session = session;
        this.transaction = transaction;
    }

    public IDALClientEntity getClient(int id) throws DALException {
        ClientDeEntity client = null;

        try {
            client = (ClientDeEntity) session.createCriteria(ClientDeEntity.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();
        } catch (Exception e) {
            throw new DALException(e);
        }

        return client;
    }

    public void addClient(IDALClientEntity client) throws DALException {

        ClientDeEntity newClient = null;
        if (client.getClass() == ClientDeEntity.class)
            newClient = (ClientDeEntity) client;
        else
            throw new DALException();

        try {
            session.save(newClient);
        } catch (Exception e) {
            throw new DALException(e);
        }
    }

    public List<IDALClientEntity> getClients() throws DALException {
        List<IDALClientEntity> clients = null;
        try {
            clients = session.createQuery("from ClientDeEntity").list();


        } catch (Exception e) {
            throw new DALException(e);
        }
        return clients;
    }


    public void update(IDALClientEntity client) throws DALException{

        ClientDeEntity clientPg = null;
        if (client.getClass() == ClientDeEntity.class)
            clientPg = (ClientDeEntity) client;
        else
            throw new DALException();

        try {


            session.update(clientPg);


        } catch (Exception e) {
            throw new DALException(e);
        }
    }

    public void delete(int id) throws DALException {
        IDALClientEntity client = null;
        try {
            client = (ClientDeEntity) session.createCriteria(ClientDeEntity.class)
                    .add(Restrictions.eq("id", id))
                    .uniqueResult();

            session.delete(client);
        } catch (Exception e) {
            throw new DALException(e);
        }

    }

    @Override
    public boolean pseudoExist(String username) throws DALException {
        ClientDeEntity client = null;

        try {
            client = (ClientDeEntity) session.createCriteria(ClientDeEntity.class)
                    .add(Restrictions.eq("username", username))
                    .uniqueResult();
        } catch (Exception e) {
            throw new DALException(e);
        }

        return (client != null);
    }

    @Override
    public boolean mailExist(String email) throws DALException {
        ClientDeEntity client = null;

        try {
            client = (ClientDeEntity) session.createCriteria(ClientDeEntity.class)
                    .add(Restrictions.eq("email", email))
                    .uniqueResult();
        } catch (Exception e) {
            throw new DALException(e);
        }

        return (client != null);
    }

    @Override
    public boolean checkUserAndPassword(String usernameOrEmail, String password) throws DALException {
        ClientDeEntity client = null;

        try {
            client = (ClientDeEntity) session.createCriteria(ClientDeEntity.class)
                    .add(Restrictions.and(Restrictions.or(Restrictions.eq("email", usernameOrEmail),
                            Restrictions.eq("username", usernameOrEmail)),
                            Restrictions.eq("password", password))).uniqueResult();
        } catch (Exception e) {
            throw new DALException(e);
        }

        return (client != null);
    }

    @Override
    public String retriveSaltByUserLogin(String usernameOrEmail) throws DALException {
        ClientDeEntity client = null;
        String salt = "";
        try {
            client = (ClientDeEntity) session.createCriteria(ClientDeEntity.class)
                    .add(Restrictions.and(Restrictions.or(Restrictions.eq("email", usernameOrEmail),
                            Restrictions.eq("username", usernameOrEmail)))).uniqueResult();
        } catch (Exception e) {
            throw new DALException(e);
        }

        if(client != null)
            salt = client.getSalt();

        return salt;
    }
}
