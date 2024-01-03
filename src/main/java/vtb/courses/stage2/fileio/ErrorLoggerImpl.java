package vtb.courses.stage2.fileio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vtb.courses.stage2.struct.LogRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

/**
 * Класс ErrorLogger реализует бин для записи в журнал ошибок, информации о записи лога вызвавшей исключение при обработке
 */
@Component
public class ErrorLoggerImpl implements ErrorLogger {

    private String logPath;

    public ErrorLoggerImpl() {
    }

    @Autowired
    @Qualifier("properties")
    public void setProperties(Properties properties) {
        logPath = properties.getProperty("ERROR_LOG_DIR");
        logPath = logPath.endsWith("/") ? logPath : logPath + '/';
    }

    @Override
    public void logError(LogRecord logRecord, Exception e) {
        try {
            Files.write(Path.of(logPath+logRecord.getFileName()+"_ERR"), (logRecord.getSourceString()+": "+e.getMessage()+"\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
