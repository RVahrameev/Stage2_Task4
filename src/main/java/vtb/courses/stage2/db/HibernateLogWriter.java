package vtb.courses.stage2.db;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;
import vtb.courses.stage2.struct.LogElement;
import vtb.courses.stage2.struct.LogRecord;
import vtb.courses.stage2.struct.Logins;
import vtb.courses.stage2.struct.User;

/**
 * Класс HibernateLogWriter инициализирует Hibernate  и реализует интефейс сохранения записей логов в БД
 */
@Component
public class HibernateLogWriter implements DbLogWriter {

    private final SessionFactory factory;
    private Session session;
    private Transaction transaction;

    public HibernateLogWriter() {
        //configuring Hibernate
        Configuration config = new Configuration();
        config.configure();
//        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
//        config.setProperty("hibernate.connection.url", properties.getProperty("CONNECTION_STRING"));
//        config.setProperty("hibernate.connection.username", properties.getProperty("USERNAME"));
//        config.setProperty("hibernate.connection.password", properties.getProperty("PASSWORD"));
        config.addAnnotatedClass(User.class);
        config.addAnnotatedClass(Logins.class);
        factory = config.buildSessionFactory();
    }


    @Override
    public void openSession() {
        session = factory.openSession();
        transaction = session.beginTransaction();
    }

    @Override
    public void closeSession() {
        transaction.commit();
        session.close();
    }

    @Override
    public void writeLogRecord(LogRecord logRecord) {
        User dbUser;
        try {
            // сначала пробуем считать пользователя из БД
            dbUser = session.createQuery("from vtb.courses.stage2.struct.User where username = '" + logRecord.getElement(LogElement.LOGIN) + "'", User.class).getSingleResult();
        } catch (NoResultException e){
            // если в БД не нашли - создаём
            dbUser = User.getUser(logRecord);
            session.persist(dbUser);
        }
        Logins loginRec = new Logins(logRecord, dbUser);
        session.persist(loginRec);
    }

    public SessionFactory getFactory() {
        return factory;
    }
}
