package vtb.courses.stage2;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Iterator;

@Configuration
public class AppConfig {
    @Bean
    public Iterator<LogRecord> getLogIterator() {

    }

    @Bean(name = "logSeparator")
    public String getSeparator() {
        return " ";
    }

    @Bean(name = "elementSequence")
    public LogElement[] getLogElementSequence(){
        return new LogElement[] {LogElement.LOGIN, LogElement.FAM, LogElement.IM, LogElement.OTCH, LogElement.DATE, LogElement.APP};
    }
}
