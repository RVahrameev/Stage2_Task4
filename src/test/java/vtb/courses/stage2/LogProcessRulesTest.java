package vtb.courses.stage2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.function.UnaryOperator;

public class LogProcessRulesTest {
    AnnotationConfigApplicationContext applicationContext;
    UnaryOperator<LogRecord> logProcessRules;
    LogRecord logRecord;
    String logSeparator;


    @Test
    @DisplayName("Проверка модуля LogProcessRules")
    public void Test(){
        String logStr;
        Assertions.assertDoesNotThrow(() ->
                        applicationContext = new AnnotationConfigApplicationContext("vtb.courses.stage2")
                , "Не удалось создать контекст приложения Spring");
        Assertions.assertDoesNotThrow(() ->
                        logSeparator = applicationContext.getBean("logSeparator", String.class)
                , "Не удалось создать bean logSeparator");
        Assertions.assertDoesNotThrow(() ->
                        logProcessRules = applicationContext.getBean("logProcessRules", UnaryOperator.class)
                , "Не удалось создать bean logProcessRules");
        System.out.println("logProcessRules Test = "+logProcessRules);
        logStr = "VAKHRAMEEV"+logSeparator+"вахрамеев"+logSeparator+"роман"+logSeparator+"александрович"+logSeparator+"2023-12-16 22:10:11"+logSeparator+"Navigator";
        Assertions.assertDoesNotThrow(() ->
                        logRecord = new LogRecord(logStr, "FileName")
                , "Не удалось создать экземпляр LogRecord");

        logProcessRules.apply(logRecord);
        Assertions.assertEquals("Вахрамеев", logRecord.getElement(LogElement.FAM), "Фамилия не соответствует ожиданиям");
        Assertions.assertEquals("Роман", logRecord.getElement(LogElement.IM), "Имя не соответствует ожиданиям");
        Assertions.assertEquals("Александрович", logRecord.getElement(LogElement.OTCH), "Отчество не соответствует ожиданиям");
        Assertions.assertEquals("2023-12-16 22:10:11", logRecord.getElement(LogElement.DATE), "Время не соответствует ожиданиям");
        Assertions.assertEquals("other:Navigator", logRecord.getElement(LogElement.APP), "Поле \"Приложение\" не соответствует ожиданиям");
    }
}
