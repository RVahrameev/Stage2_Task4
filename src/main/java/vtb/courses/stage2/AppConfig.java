package vtb.courses.stage2;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.function.UnaryOperator;

@Configuration
public class AppConfig {

    private static Properties props = new Properties();

    public AppConfig() throws IOException {
        props.load(this.getClass().getResourceAsStream("/config.ini"));
    }

    @Bean(name = "properties")
    public Properties getProperties(){
        return props;
    }

    @Bean(name = "logIterator")
    @Scope("singleton")
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
        logProcessRules.addRule(LogElement.FAM, LogElementProcessors::stringFirstUpper);
        logProcessRules.addRule(LogElement.IM, LogElementProcessors::stringFirstUpper);
        logProcessRules.addRule(LogElement.OTCH, LogElementProcessors::stringFirstUpper);
        logProcessRules.addRule(LogElement.APP, LogElementProcessors::checkApplication);
        logProcessRules.addRule(LogElement.DATE, LogElementProcessors::checkDate);
        return logProcessRules;
    }
}
