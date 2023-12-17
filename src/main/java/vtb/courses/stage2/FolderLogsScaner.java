package vtb.courses.stage2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;

@Component
public class FolderLogsScaner implements Iterator<LogRecord> {
    private final int cacheSize = 100;
    private String path;
    private String[] files;
    private int fileIdx;
    private BufferedReader fileReader;

    private String[] logCache;
    private int[] fileNameIdx;
    private int logCacheIdx;
    private int logCacheNum;
    private Properties props;

    @Autowired
    @Qualifier("properties")
    public void setProps(Properties props) {
        this.props = props;
        init();
    }

    public void init() {
        String path = props.getProperty("LOG_DIR");
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Путь к папке не может быть пустым!");
        }
        this.path = path.endsWith("/") ? path : path + '/';
        this.files = Arrays.stream((new File(path)).listFiles()).filter(x -> x.isFile()).map(x -> x.getName()).toArray(String[]::new);
    }

    public FolderLogsScaner() {
        logCache = new String[cacheSize];
        fileNameIdx = new int[cacheSize];
        fileIdx = -1;
    }

    @Override
    public boolean hasNext() {
        if ((logCache == null) || (logCacheIdx == logCacheNum)) {
            precacheLogs();
        }
        return (logCacheNum != 0);
    }

    @Override
    public LogRecord next() {
        if (hasNext()) {
            return new LogRecord(logCache[logCacheIdx], files[fileNameIdx[logCacheIdx++]]);
        } else {
            throw new NoSuchElementException();
        }
    }

    private BufferedReader getNextFileReader() {
        try {
            if (files.length == 0 || fileIdx == files.length) {
                return null;
            } else {
                return new BufferedReader(new FileReader(path + files[++fileIdx]));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void precacheLogs() {
        // при первом вызове инициализация, подключаемся к первому файлу
        if (fileReader == null) {
            fileReader = getNextFileReader();
        }
        logCacheNum = 0;
        logCacheIdx = 0;
        if (fileReader != null) {
            for (int i = 0; i < cacheSize; i++) {
                try {
                    logCache[i] = fileReader.readLine();
                    fileNameIdx[i] = fileIdx - 1;
                    if (logCache[i] != null) {
                        logCacheNum++;
                    } else {
                        fileReader = getNextFileReader();
                        if (fileReader == null) {
                            break;
                        } else {
                            i--;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
