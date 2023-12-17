package vtb.courses.stage2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Properties;
import java.util.function.UnaryOperator;

public class FolderLogsScanerTest {
    AnnotationConfigApplicationContext applicationContext;
    Properties properties;
    String logSeparator;
    Iterator<LogRecord> logIterator;
    UnaryOperator<LogRecord> logProcessRules;
    ErrorLogger errorLogger;

    public void formLogFiles() throws IOException {
        Assertions.assertDoesNotThrow(() ->
                        properties = applicationContext.getBean("properties", Properties.class)
                , "Не удалось создать bean properties");
        Assertions.assertDoesNotThrow(() ->
                        logSeparator = applicationContext.getBean("logSeparator", String.class)
                , "Не удалось создать bean logSeparator");

        String path = properties.getProperty("LOG_DIR");
        path = path.endsWith("/") ? path : path + '/';
        String[] logins = {"ivanov","petrov","sidorov"};
        String logStr;
        for (int i = 1; i <= 10; i++) {
            File logFile = new File(path+"log"+i+".txt");
            logFile.delete();
            for (int j = 0; j < logins.length; j++) {
                for (int k = 0; k < 50; k++) {
                    logStr = logins[j]+logSeparator+logins[j]+"Fam"+logSeparator+logins[j]+"Im"+logSeparator+logins[j]+"Otch"+logSeparator+"2023/12/21 10:"+i+":"+k+logSeparator+"AppName"+"\r\n";
                    Files.write(Path.of(path+"log"+i+".txt"), logStr.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                }
                //Добавляем по одной плохой записи
                logStr = logins[j]+logSeparator+logins[j]+"Fam"+logSeparator+logins[j]+"Im"+logSeparator+logins[j]+"Otch"+logSeparator+""+logSeparator+"AppName"+"\r\n";
                Files.write(Path.of(path+"log"+i+".txt"), logStr.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        }

    }

    @Test
    public void test() throws IOException {
        Assertions.assertDoesNotThrow(() ->
                        applicationContext = new AnnotationConfigApplicationContext("vtb.courses.stage2")
                , "Не удалось создать контект приложения Spring");

        formLogFiles();
        properties = applicationContext.getBean("properties", Properties.class);

        Assertions.assertDoesNotThrow(() ->
                        logIterator = applicationContext.getBean("logIterator", Iterator.class)
                , "Не удалось создать bean logIterator");

        Assertions.assertDoesNotThrow(() ->
                        logProcessRules = applicationContext.getBean("logProcessRules", UnaryOperator.class)
                , "Не удалось создать bean logProcessRules");

        Assertions.assertDoesNotThrow(() ->
                        errorLogger = applicationContext.getBean(ErrorLogger.class)
                , "Не удалось создать bean errorLogger");

        LogRecord currentRecord;
        while (logIterator.hasNext()) {
            currentRecord = logIterator.next();
            try {
                logProcessRules.apply(currentRecord);
            } catch (Exception e) {
                errorLogger.accept(currentRecord, e);
            }
        }

    }
}
