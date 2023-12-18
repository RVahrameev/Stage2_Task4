package vtb.courses.stage2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.Properties;

@Component
public class HibernateLogWriter implements DbLogWriter{

    private SessionFactory factory;
    private Session session;
    private Transaction transaction;
    private Properties properties;

    public HibernateLogWriter() {}
    public void init() {
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

    @Autowired
    @Qualifier("properties")
    public void setProperties(Properties properties) {
        this.properties = properties;
        init();
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
        User dbUser = session.createQuery("from User where username = '"+logRecord.getElement(LogElement.LOGIN)+"'", User.class).getResultList().get(0);
        System.out.println(dbUser);
        User user = User.getUser(logRecord);
        Logins loginRec = new Logins(logRecord, user);
        session.persist(loginRec);
    }

}
