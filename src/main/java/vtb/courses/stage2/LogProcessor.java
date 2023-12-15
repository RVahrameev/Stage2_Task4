package vtb.courses.stage2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import java.util.Iterator;

@Component
public class LogProcessor {

    private Iterator<LogRecord> logIterator;

    private DbLogWriter dbWriter;

    private UnaryOperator<LogRecord> processRules;

    private BiConsumer<LogRecord, Exception> errorLogger;

    public LogProcessor() {
    }

    @Autowired
    @Qualifier("logIterator")
    public void setLogIterator(Iterator<LogRecord> logIterator) {
        this.logIterator = logIterator;
    }

    @Autowired
    @Qualifier("dbLogWriter")
    public void setDbWriter(DbLogWriter dbWriter) {
        this.dbWriter = dbWriter;
    }

    @Autowired
    @Qualifier("logProcessRules")
    public void setLogProcessRules(UnaryOperator<LogRecord> logProcessRules) {
        this.processRules = logProcessRules;
    }

    @Autowired
    @Qualifier("errorLoggeer")
    public void setErrorLogger(BiConsumer<LogRecord, Exception> errorLogger) {
        this.errorLogger = errorLogger;
    }

    public void uploadLogs() {
        LogRecord currentRecord;
        while (logIterator.hasNext()) {
            currentRecord = logIterator.next();
            try {
                dbWriter.writeLogRecord(processRules.apply(currentRecord));
            } catch (Exception e) {
                errorLogger.accept(currentRecord, e);
            }
        }
    }
}
