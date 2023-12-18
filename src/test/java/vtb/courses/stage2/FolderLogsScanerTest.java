package vtb.courses.stage2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.function.UnaryOperator;

public class FolderLogsScanerTest {
    AnnotationConfigApplicationContext applicationContext;
    Properties properties;
    String logSeparator;
    Iterator<LogRecord> logIterator;
    UnaryOperator<LogRecord> logProcessRules;
    ErrorLogger errorLogger;

    Map<String, String> srcLog = new HashMap<>();
    Map<String, String> resLog = new HashMap<>();
    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DecimalFormat decimalFormat = new DecimalFormat("00");

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
        String timeStr;
        for (int i = 1; i <= 10; i++) {
            File logFile = new File(path+"log"+i+".txt");
            logFile.delete();
            for (int j = 0; j < logins.length; j++) {
                for (int k = 0; k < 50; k++) {
                    timeStr = "2023-12-21 10:" + decimalFormat.format(i) + ":" + decimalFormat.format(k);
                    logStr = logins[j]+logSeparator+logins[j]+"Fam"+logSeparator+logins[j]+"Im"+logSeparator+logins[j]+"Otch"+logSeparator+timeStr+logSeparator+"AppName";
                    Files.write(Path.of(path+"log"+i+".txt"), (logStr+"\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    srcLog.put(logins[j]+dataFormat.format(Timestamp.valueOf(timeStr)), logStr);
                }
                //Добавляем по одной плохой записи
                logStr = logins[j]+logSeparator+logins[j]+"Fam"+logSeparator+logins[j]+"Im"+logSeparator+logins[j]+"Otch"+logSeparator+""+logSeparator+"AppName"+"\r\n";
                Files.write(Path.of(path+"log"+i+".txt"), logStr.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        }

    }

    @Test
    @DisplayName("Проверка модуля FolderLogsScaner - итератора по записям лога")
    public void test() throws IOException {
        Assertions.assertDoesNotThrow(() ->
                        applicationContext = new AnnotationConfigApplicationContext("vtb.courses.stage2")
                , "Не удалось создать контект приложения Spring");

        formLogFiles();

        Assertions.assertDoesNotThrow(() ->
                        logIterator = applicationContext.getBean("logIterator", Iterator.class)
                , "Не удалось создать bean logIterator");

        Assertions.assertDoesNotThrow(() ->
                        logProcessRules = applicationContext.getBean("logProcessRules", UnaryOperator.class)
                , "Не удалось создать bean logProcessRules");

        Assertions.assertDoesNotThrow(() ->
                        errorLogger = applicationContext.getBean(ErrorLogger.class)
                , "Не удалось создать bean errorLogger");


        Assertions.assertDoesNotThrow(() -> {
                    int ii = 0;
                    LogRecord currentRecord;
                    while (logIterator.hasNext()) {
                        currentRecord = logIterator.next();
                        try {
                            logProcessRules.apply(currentRecord);
                            resLog.put(currentRecord.getElement(LogElement.LOGIN) + currentRecord.getElement(LogElement.DATE), currentRecord.getSourceString());
                        } catch (Exception e) {
                            errorLogger.accept(currentRecord, e);
                        }
                    }
                }
                , "Не удалось прочитать логи посредством бина logIterator");

        Assertions.assertEquals(srcLog.size(), resLog.size(), "Кол-во исходных записей не равно количеству обработанных");
        srcLog.forEach((k, v)->Assertions.assertEquals(v, resLog.get(k), "Загруженная запись не соответствует исходной"));

    }

    @Test
    public void test2(){
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        DecimalFormat decimalFormat = new DecimalFormat("00");
        System.out.println((new SimpleDateFormat("dd/MM/yyyy hh:mm:ss")).format(timestamp));
        System.out.println(decimalFormat.format(12));
    }
}
