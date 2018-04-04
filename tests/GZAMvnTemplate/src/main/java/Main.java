import mt.dal.entities.VoitureEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.Iterator;
import java.util.List;

public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {
            Query query = session.createQuery("from mt.dal.entities.VoitureEntity");
            List listess = query.list();
            Iterator iterator = listess.iterator();
            while (iterator.hasNext()) {
                VoitureEntity user = (VoitureEntity) iterator.next();
                System.out.println(user.getEmail());

            }
        }finally {
            session.close();
        }
        System.out.println("End");
    }
}