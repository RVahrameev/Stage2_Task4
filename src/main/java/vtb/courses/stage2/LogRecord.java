package vtb.courses.stage2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class LogRecord {
    private HashMap<LogElement, String> parts = new HashMap<>();
    private String fileName;
    private String sourceString;

    @Autowired @Qualifier("logSeparator")
    private String separator;
    @Autowired @Qualifier("elementSequence")
    private LogElement[] elementSequence;

    public LogRecord(String logString, String fileName) {
        setParts(logString, elementSequence);
        this.fileName = fileName;
        this.sourceString = logString;
    }

    private void setParts(String logStr, LogElement[] elementSequence) {
       int i = 0;
       for (String s: logStr.split(separator)) {
           parts.put(elementSequence[i], s);
           i++;
       }
    }

    public void setElement(LogElement logElement, String logPart) {
        parts.put(logElement, logPart);
    }

    public String getElement(LogElement logElement){
        return parts.get(logElement);
    }

    public String getFileName() {
        return fileName;
    }

    public String getSourceString() {
        return sourceString;
    }
}
