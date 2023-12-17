package vtb.courses.stage2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class PostGreLogWriter implements DbLogWriter{

    private SessionFactory factory;
    private Session session;
    private Transaction transaction;
    private Properties properties;

    public PostGreLogWriter() {}
    public void init() {
        //configuring Hibernate
        Configuration config = new Configuration();
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        config.setProperty("hibernate.connection.url", properties.getProperty("CONNECTION_STRING"));
        config.setProperty("hibernate.connection.username", properties.getProperty("USERNAME"));
        config.setProperty("hibernate.connection.password", properties.getProperty("PASSWORD"));
        factory = config.buildSessionFactory();
        //config.addClass(User.class);
        //config.addClass(Logins.class);
    }

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
        User user = User.getUser(logRecord);
        Logins loginRec = new Logins(logRecord, user);
        session.persist(loginRec);
    }

}
