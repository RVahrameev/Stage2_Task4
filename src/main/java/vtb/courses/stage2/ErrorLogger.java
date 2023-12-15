package vtb.courses.stage2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.function.BiConsumer;

@Component
public class ErrorLogger implements BiConsumer<LogRecord, Exception> {

    private Properties properties;
    private String logPath;

    @Autowired
    public void setProperties(Properties properties){
        this.properties = properties;
        logPath = properties.getProperty("ERROR_LOG_DIR");
        logPath = logPath.endsWith("\\") ? logPath : logPath + '\\';
    }

    @Override
    public void accept(LogRecord logRecord, Exception e) {
        try {
            Files.write(Path.of(logPath+logRecord.getFileName()+"_ERR"), (logRecord.getSourceString()+": "+e.getMessage()).getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
