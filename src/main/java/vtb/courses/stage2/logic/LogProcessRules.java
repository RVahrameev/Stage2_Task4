package vtb.courses.stage2.logic;

import vtb.courses.stage2.struct.LogElement;
import vtb.courses.stage2.struct.LogRecord;

public interface LogProcessRules {
    void addRule(LogElement logElement, LogVerifier... rules);
    LogRecord processRecord(LogRecord logRecord);
}
