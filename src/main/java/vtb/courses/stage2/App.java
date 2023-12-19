package vtb.courses.stage2;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vtb.courses.stage2.logic.LogProcessor;

/**
 * Реализация 4ой задачи.
 * Параметры конфигурации задаются в файле config.ini
 */
public class App 
{
    public static void main( String[] args ) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("vtb.courses.stage2.logic", "vtb.courses.stage2");
        LogProcessor logProcessor = applicationContext.getBean("logProcessor", LogProcessor.class);
        logProcessor.uploadLogs();
    }
}
