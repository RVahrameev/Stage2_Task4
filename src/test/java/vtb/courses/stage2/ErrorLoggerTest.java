package vtb.courses.stage2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ErrorLoggerTest {

    AnnotationConfigApplicationContext applicationContext;
    ErrorLogger errorLogger;
    String logSeparator;
    String errorFileName = "File1.log";
    String logStr;
    LogRecord logRecord;
    Properties properties;
    String path;

    /**
     * Тест проверяет работу бина ErrorLogger
     * Записывает с его помощью тестовую строку в файл лога ошибок загрузки и проверяет результат записи
     */
    @Test
    public void test() throws IOException {


        Assertions.assertDoesNotThrow(() ->
                applicationContext = new AnnotationConfigApplicationContext("vtb.courses.stage2")
        , "Не удалось создать контекст приложения Spring");
        Assertions.assertDoesNotThrow(() ->
                errorLogger = applicationContext.getBean(ErrorLogger.class)
        , "Не удалось создать bean errorLogger");
        Assertions.assertDoesNotThrow(() ->
                logSeparator = applicationContext.getBean("logSeparator", String.class)
        , "Не удалось создать bean logSeparator");
        logStr = "VAKHRAMEEV"+logSeparator+"вахрамеев"+logSeparator+"роман"+logSeparator+"александрович"+logSeparator+"2023-12-16 22:10:11"+logSeparator+"Navigator";
        Assertions.assertDoesNotThrow(() ->
                logRecord = new LogRecord(logStr, errorFileName)
        , "Не удалось создать экземпляр LogRecord");
        Assertions.assertDoesNotThrow(() ->
                        properties = applicationContext.getBean("properties", Properties.class)
                , "Не удалось создать bean properties");
        Assertions.assertDoesNotThrow(() ->
                        path = properties.getProperty("ERROR_LOG_DIR")
                , "В файле config.ini не указан путь ERROR_LOG_DIR к папке фиксации ошибок загрузки");
        path = path.endsWith("/") ? path : path + '/';
        File errorFile = new File(path + errorFileName + "_ERR");
        errorFile.delete();
        Assertions.assertDoesNotThrow(() ->
                errorLogger.accept(logRecord, new Exception("Тестовая ошибка"))
        , "Не удалось записать ошибку в лог ошибок обработки через errorLogger");
        String res_str = (new BufferedReader(new FileReader(path + errorFileName + "_ERR"))).readLine();
        Assertions.assertEquals(logStr +": Тестовая ошибка", res_str, "Сохранённая запись в файле ошибок не соответствует ожиданиям.");
    }
}
