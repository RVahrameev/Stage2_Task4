package vtb.courses.stage2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vtb.courses.stage2.db.DbLogWriter;
import vtb.courses.stage2.db.HibernateLogWriter;
import vtb.courses.stage2.fileio.ErrorLogger;
import vtb.courses.stage2.fileio.FolderLogsScaner;
import vtb.courses.stage2.logic.*;
import vtb.courses.stage2.struct.LogElement;
import vtb.courses.stage2.struct.LogRecord;
import vtb.courses.stage2.utils.Hronometer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.function.UnaryOperator;

/**
 * AppConfig - класс конфигурации приложения Spring<p>
 * Объявляет и настраивает все необходимые приложению бины.<p>
 * Подгружает необходимые настройки из ресурсного файла config.ini
 */

@Configuration
public class AppConfig {

    private UnaryOperator<String> fioAction;
    private UnaryOperator<String> appCheck;
    private UnaryOperator<String> checkDate;

    private final static Properties props = new Properties();

    public AppConfig() throws IOException {
        props.load(this.getClass().getResourceAsStream("/config.ini"));
    }

    @Bean(name = "properties")
    public Properties getProperties(){
        return props;
    }

    @Bean(name = "logIterator")
    public Iterator<LogRecord> getLogIterator() {
        return new FolderLogsScaner();
    }

    @Bean(name = "logSeparator")
    public String getSeparator() {
        return "\t";
    }

    @Bean(name = "elementSequence")
    public LogElement[] getLogElementSequence(){
        return new LogElement[] {LogElement.LOGIN, LogElement.FAM, LogElement.IM, LogElement.OTCH, LogElement.DATE, LogElement.APP};
    }

    @Bean(name = "dbLogWriter")
    public DbLogWriter getDbLogWriter() {
        return new HibernateLogWriter();
    }


    @Bean(name = "errorLogger")
    public ErrorLogger getErrorLogger() {
        return new ErrorLogger();
    }

    @Bean(name = "logProcessRules")
    public UnaryOperator<LogRecord> getLogProcessRules() {
        LogProcessRules logProcessRules = new LogProcessRules();
        logProcessRules.addRule(LogElement.FAM, fioAction);
        logProcessRules.addRule(LogElement.IM, fioAction);
        logProcessRules.addRule(LogElement.OTCH, fioAction);
        logProcessRules.addRule(LogElement.APP, appCheck);
        logProcessRules.addRule(LogElement.DATE, checkDate);
        return logProcessRules;
    }

    @Bean(name = "logProcessor")
    public LogProcessor getLogProcessor(){
        return new LogProcessor();
    }

    @Autowired @Qualifier("stringFirstUpper")
    public void setFioAction(UnaryOperator<String> operation) {
        fioAction = getWrapper(operation);
    }
    @Autowired @Qualifier("checkApplication")
    public void setAppCheck(UnaryOperator<String> operation) {
        appCheck = getWrapper(operation);
    }
    @Autowired @Qualifier("checkDate")
    public void setDateChecker(UnaryOperator<String> operation) {
        checkDate = getWrapper(operation);
    }

    private UnaryOperator<String> getWrapper(UnaryOperator<String> operation) {
        if (operation.getClass().isAnnotationPresent(LogTransformation.class)) {
            return (new Hronometer<UnaryOperator<String>>()).getProxy(operation, operation.getClass().getAnnotation(LogTransformation.class).value());
        } else {
            return operation;
        }
    }
}
