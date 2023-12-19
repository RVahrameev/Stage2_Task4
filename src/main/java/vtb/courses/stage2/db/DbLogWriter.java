package vtb.courses.stage2.db;

import vtb.courses.stage2.struct.LogRecord;

/**
 * Интерфейс DbLogWriter содержит минимальный набор методов для сохранения записей логов в БД
 */
public interface DbLogWriter {
    void openSession();
    void closeSession();
    void writeLogRecord(LogRecord logRecord);
}
