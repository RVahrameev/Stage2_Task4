package vtb.courses.stage2.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vtb.courses.stage2.db.DbLogWriter;
import vtb.courses.stage2.fileio.ErrorLogger;
import vtb.courses.stage2.fileio.FolderLogsScanner;
import vtb.courses.stage2.struct.LogRecord;

/**
 * Класс LogProcessor представляет собой собственно реализацию основного алгоритма задачи<p>
 * Посредством вызова бинов<p>
 * - FolderLogsScanner<p>
 * - LogProcessRules<p>
 * - DbLogWriter<p>
 * - ErrorLogger<p>
 * производит загрузку логов в БД и сохранения информации о не загруженных логах в журнале ошибок
 */
@Component
public class LogProcessorImpl implements LogProcessor {

    private FolderLogsScanner logIterator;

    private DbLogWriter dbWriter;

    private LogProcessRules processRules;

    private ErrorLogger errorLogger;

    public LogProcessorImpl() {
    }

    @Autowired
    public void setLogIterator(FolderLogsScanner logIterator) {
        this.logIterator = logIterator;
    }

    @Autowired
    public void setDbWriter(DbLogWriter dbWriter) {
        this.dbWriter = dbWriter;
    }

    @Autowired
    public void setLogProcessRules(LogProcessRules logProcessRules) {
        this.processRules = logProcessRules;
    }

    @Autowired
    public void setErrorLogger(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;
    }

    public void uploadLogs() {
        LogRecord currentRecord;
        // посточно вычитываем логи с помощью бина-итератора
        dbWriter.openSession();
        while (logIterator.hasNext()) {
            currentRecord = logIterator.next();
            try {
                // применяем к вычитанной стоке логов правила обоработки и сохраняем результат в БД
                dbWriter.writeLogRecord(processRules.processRecord(currentRecord));
            } catch (Exception e) {
                // в случае ошибки завём бин фиксации ошибочной стоки в файле ошибок загрузки
                errorLogger.logError(currentRecord, e);
            }
        }
        dbWriter.closeSession();
    }
}
