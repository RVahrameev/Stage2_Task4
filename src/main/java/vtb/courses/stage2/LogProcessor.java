package vtb.courses.stage2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.function.Consumer;

@Component
public class LogProcessor {

    private Iterator<LogRecord> logIterator;

    private Consumer<LogRecord> dbWriter;

    private LogProcessRules processRules;

    @Autowired
    @Qualifier("logIterator")
    public void setLogIterator(Iterator<LogRecord> logIterator) {
        this.logIterator = logIterator;
    }

    @Autowired
    public void setDbWriter(Consumer<LogRecord> dbWriter) {
        this.dbWriter = dbWriter;
    }

    @Autowired
    public void setLogProcessRules(LogProcessRules logProcessRules) {
        this.processRules = logProcessRules;
    }

}
