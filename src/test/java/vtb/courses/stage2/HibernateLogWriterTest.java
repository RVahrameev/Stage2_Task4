package vtb.courses.stage2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vtb.courses.stage2.db.DbLogWriter;
import vtb.courses.stage2.db.HibernateLogWriter;
import vtb.courses.stage2.struct.LogRecord;
import vtb.courses.stage2.struct.Logins;
import vtb.courses.stage2.struct.User;

import java.sql.Timestamp;
import java.util.List;

public class HibernateLogWriterTest {
    AnnotationConfigApplicationContext applicationContext;
    DbLogWriter dbLogWriter;
    String logSeparator;
    LogRecord logRecord;
    User dbUser;
    List<Logins> logins;
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

        SessionFactory factory = ((HibernateLogWriter)dbLogWriter).getFactory();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        Assertions.assertDoesNotThrow(() ->
            dbUser = session.createQuery("from vtb.courses.stage2.struct.User where username = 'VAKHRAMEEV'", User.class).getSingleResult()
                , "Пользователь VAKHRAMEEV не создан в БД");
        Assertions.assertEquals("вахрамеев роман александрович", dbUser.getFio(), "В БД сохранилась не корректная ФИО");
        Query<Logins> query = session.createQuery("from vtb.courses.stage2.struct.Logins where userId = :dbUser", Logins.class).setParameter("dbUser", dbUser);
        Assertions.assertDoesNotThrow(() ->
                        logins = query.getResultList()
                , "У пользователя VAKHRAMEEV нет записей в Logins");
        Assertions.assertEquals(Timestamp.valueOf("2023-12-16 22:10:11"), logins.get(0).getAccessDate(), "В БД сохранился не корректный accessDate");
        Assertions.assertEquals("Navigator", logins.get(0).getApplication(), "В БД сохранился не корректный application");
        transaction.commit();
        session.close();
    }

}
