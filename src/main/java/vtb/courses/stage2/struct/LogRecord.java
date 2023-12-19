package vtb.courses.stage2.struct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Класс LogRecord представляет собой временное хранилище для записи лога, на пути от файла к БД. <p>
 * Содержит:<p>
 *     - исходную строку лога<p>
 *     - отдельные элементы строки лога разложенные по типам элементов лога<p>
 *     - имя файла которому принадлежит строка
 */
@Component
public class LogRecord {
    private final HashMap<LogElement, String> parts = new HashMap<>();
    private String fileName;
    private String sourceString;

    private static String separator;
    private static LogElement[] elementSequence;

    @Autowired @Qualifier("logSeparator")
    public void setSeparator(String separator) {
        LogRecord.separator = separator;
    }

    @Autowired @Qualifier("elementSequence")
    public void setElementSequence(LogElement[] elementSequence) {
        LogRecord.elementSequence = elementSequence;
    }

    public LogRecord(){}

    public LogRecord(String logString, String fileName) {
        setParts(logString, elementSequence);
        this.fileName = fileName;
        this.sourceString = logString;
    }

    private void setParts(String logStr, LogElement[] elementSequence) {
        int i = 0;
        for (String s: logStr.split(separator)) {
            parts.put(elementSequence[i++], s);
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
