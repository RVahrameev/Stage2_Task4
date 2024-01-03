package vtb.courses.stage2.fileio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vtb.courses.stage2.struct.LogRecord;

import java.io.*;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * Класс FolderLogsScaner реализует итератор позволяющий пройтись по всем строкам логов, хранящихся в определённой папке
 */

@Component
public class FolderLogsScannerImpl implements FolderLogsScanner {
    private final int cacheSize = 100;
    private String path;
    private String[] files;
    private int fileIdx;
    private BufferedReader fileReader;

    private final String[] logCache;
    private final int[] fileNameIdx;
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
        // определяем папку с данными
        String path = props.getProperty("LOG_DIR");
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Путь к папке не может быть пустым!");
        }
        this.path = path.endsWith("/") ? path : path + '/';

        // вычитываем из папки список файлов, которые будем читать
        File[] files = (new File(path)).listFiles();
        if (files != null) {
            this.files = Arrays.stream(files).filter(File::isFile).map(File::getName).toArray(String[]::new);
        } else {
            throw new NoSuchElementException("В папке логов нет данных");
        }
    }

    public FolderLogsScannerImpl() {
        logCache = new String[cacheSize];
        fileNameIdx = new int[cacheSize];
        fileIdx = -1;
    }

    @Override
    public boolean hasNext() {
        if (logCacheIdx == logCacheNum) {
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
            if (files.length == 0 || fileIdx == files.length-1) {
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
            // наполняем кеш порцией данных из файлов логов
            for (int i = 0; i < cacheSize; i++) {
                try {
                    logCache[i] = fileReader.readLine();
                    fileNameIdx[i] = fileIdx;
                    if (logCache[i] != null) {
                        logCacheNum++;
                    } else {
                        // если очередное вычитывание из файла ничего не дало, то переключаемся на следующий файл
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
