package vtb.courses.stage2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class LogProcessor {

    private Iterator<LogRecord> logIterator;

    @Autowired
    public void setLogIterator(Iterator<LogRecord> logIterator) {
        this.logIterator = logIterator;
    }
}
