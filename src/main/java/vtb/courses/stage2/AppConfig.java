package vtb.courses.stage2;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Configuration
public class AppConfig {

    private Properties props = new Properties();

    public AppConfig() throws IOException {
        props.load(new FileInputStream(new File(".\\resources\\config.ini")));
    }

    @Bean(name = "logIterator")
    @Scope("prototype")
    public Iterator<LogRecord> getLogIterator() {
        return new FolderLogsScaner(props.getProperty("LOG_DIR"));
    }

    @Bean(name = "logSeparator")
    public String getSeparator() {
        return " ";
    }

    @Bean(name = "elementSequence")
    public LogElement[] getLogElementSequence(){
        return new LogElement[] {LogElement.LOGIN, LogElement.FAM, LogElement.IM, LogElement.OTCH, LogElement.DATE, LogElement.APP};
    }

    @Bean(name = "dbLogWriter")
    public DbLogWriter getDbLogWriter() {
        return new PostGreLogWriter();
    }


    @Bean(name = "errorLoggeer")
    public ErrorLogger getErrorLogger() {
        return new ErrorLogger();
    }

    @Bean
    public Properties getProperties(){
        return props;
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
