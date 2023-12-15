package vtb.courses.stage2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class PostGreLogWriter implements DbLogWriter{

    private Properties props;
    private SessionFactory factory;
    private Session session;
    private Transaction transaction;

    private void init() {
        //configuring Hibernate
        Configuration config = new Configuration();
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        config.setProperty("hibernate.connection.url", props.getProperty("CONNECTION_STRING"));
        config.setProperty("hibernate.connection.username", props.getProperty("USERNAME"));
        config.setProperty("hibernate.connection.password", props.getProperty("PASSWORD"));
        factory = config.buildSessionFactory();
        config.addClass(User.class);
        config.addClass(Logins.class);
    }
    @Override
    public void openSession() {
        Session session = factory.openSession();
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

    @Autowired
    public void setProps(Properties props) {
        this.props = props;
    }
}
