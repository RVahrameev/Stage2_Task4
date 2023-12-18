package vtb.courses.stage2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.object.SqlQuery;

import java.util.Iterator;

public class HibernateLogWriterTest {
    AnnotationConfigApplicationContext applicationContext;
    DbLogWriter dbLogWriter;
    String logSeparator;
    LogRecord logRecord;

    @Test
    @DisplayName("Проверка компонета записи данных в БД (HibernateLogWriter)")
    public void test() {
        Assertions.assertDoesNotThrow(() ->
                        applicationContext = new AnnotationConfigApplicationContext("vtb.courses.stage2")
                , "Не удалось создать контекст приложения Spring");
        Assertions.assertDoesNotThrow(() ->
                        dbLogWriter = applicationContext.getBean("dbLogWriter", DbLogWriter.class)
                , "Не удалось создать bean dbLogWriter");
        Assertions.assertDoesNotThrow(() ->
                        logSeparator = applicationContext.getBean("logSeparator", String.class)
                , "Не удалось создать bean logSeparator");

        String logStr = "VAKHRAMEEV"+logSeparator+"вахрамеев"+logSeparator+"роман"+logSeparator+"александрович"+logSeparator+"2023-12-16 22:10:11"+logSeparator+"Navigator";
        Assertions.assertDoesNotThrow(() ->
                        logRecord = new LogRecord(logStr, "testfilename")
                , "Не удалось создать экземпляр LogRecord");
        dbLogWriter.openSession();
        dbLogWriter.writeLogRecord(logRecord);
        dbLogWriter.closeSession();

        Configuration config = new Configuration();
        config.configure();
        SessionFactory factory = config.buildSessionFactory();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("select * from users where username = 'VAKHRAMEEV'", User.class);
        User user = (User)nativeQuery.getSingleResult();
        System.out.println(user.getUsername());

        transaction.commit();
        session.close();
    }

}
