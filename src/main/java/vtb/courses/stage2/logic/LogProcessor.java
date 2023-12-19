package vtb.courses.stage2.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vtb.courses.stage2.db.DbLogWriter;
import vtb.courses.stage2.struct.LogRecord;

import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

import java.util.Iterator;

/**
 * Класс LogProcessor представляет собой собственно реализацию основного алгоритма задачи<p>
 * Посредством вызова бинов<p>
 * - logIterator<p>
 * - logProcessRules<p>
 * - dbLogWriter<p>
 * - errorLogger<p>
 * производит загрузку логов в БД и сохранения информации о не загруженных логах в журнале ошибок
 */
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
    @Qualifier("errorLogger")
    public void setErrorLogger(BiConsumer<LogRecord, Exception> errorLogger) {
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
                dbWriter.writeLogRecord(processRules.apply(currentRecord));
            } catch (Exception e) {
                // в случае ошибки завём бин фиксации ошибочной стоки в файле ошибок загрузки
                errorLogger.accept(currentRecord, e);
            }
        }
        dbWriter.closeSession();
    }
}
