package vtb.courses.stage2;

public interface DbLogWriter {
    void openSession();
    void closeSession();
    void writeLogRecord(LogRecord logRecord);
}
