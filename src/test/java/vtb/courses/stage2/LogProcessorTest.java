package vtb.courses.stage2;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.hibernate.query.NativeQuery;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vtb.courses.stage2.db.DbLogWriter;
import vtb.courses.stage2.db.HibernateLogWriter;
import vtb.courses.stage2.logic.LogProcessor;

public class LogProcessorTest {
    AnnotationConfigApplicationContext applicationContext;
    DbLogWriter dbLogWriter;
    LogProcessor logProcessor;

    @Test
    @DisplayName("Интеграционный тест")
    public void test() {
        Assertions.assertDoesNotThrow(() ->
                applicationContext = new AnnotationConfigApplicationContext("vtb.courses.stage2")
                , "Не удалось создать контекст приложения Spring");
        Assertions.assertDoesNotThrow(() ->
                dbLogWriter = applicationContext.getBean(DbLogWriter.class)
                , "Не удалось создать bean DbLogWriter");

        FolderLogsScanerTest logsScanerTest = new FolderLogsScanerTest();
        Assertions.assertDoesNotThrow(() ->
                logsScanerTest.formLogFiles(applicationContext)
                , "Не удалось создать тестовые файлы логов");

        Assertions.assertDoesNotThrow(() ->
                        logProcessor = applicationContext.getBean(LogProcessor.class)
                , "Не удалось создать bean LogProcessor");
        Assertions.assertDoesNotThrow(() ->
                logProcessor.uploadLogs()
                , "Не удалось загрузить записи логов в БД");

        SessionFactory factory = ((HibernateLogWriter)dbLogWriter).getFactory();
        Session session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        NativeQuery nativeQuery = session.createNativeQuery("select count(1) from users");
        long recordCount = (Long) nativeQuery.getSingleResult();
        Assertions.assertEquals(3, recordCount, "Созданное в БД количество пользователей неверное");
        nativeQuery = session.createNativeQuery("select count(1) from logins");
        recordCount = (Long)nativeQuery.getSingleResult();
        Assertions.assertEquals(1500, recordCount, "Созданное в БД количество логов");
    }
}
