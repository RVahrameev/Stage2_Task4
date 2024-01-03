package vtb.courses.stage2.fileio;

import vtb.courses.stage2.struct.LogRecord;

public interface ErrorLogger {
    void logError(LogRecord logRecord, Exception e);
}
