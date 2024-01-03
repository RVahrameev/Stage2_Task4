package vtb.courses.stage2.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import vtb.courses.stage2.struct.LogElement;
import vtb.courses.stage2.struct.LogRecord;
import java.util.HashMap;

/**
 * Класс LogProcessRules реализует бин, который содержит правила обработки элементов лога
 * и метод для применения этих правил к записи лога.
 */
@Component
public class LogProcessRulesImpl implements LogProcessRules {
    private final HashMap<LogElement, LogVerifier[]> rules = new HashMap<>();
    private LogElement[] logElementSequence;

    /**
     * Метод addRule - позволяет задать для определённого элемента логов некое правило обработки
     */
    public void addRule(LogElement logElement, LogVerifier... rules) {
        this.rules.put(logElement, rules);
    }

    /** Метод apply применяет к записи лога правила обработки заданные извне в мапе rules
     */
    @Override
    public LogRecord processRecord(LogRecord logRecord) {
        // цикл по соствляющим элементам сторки лога
        for (LogElement logElement: logElementSequence) {
            String element = logRecord.getElement(logElement);
            // если для этого типа элемента лога, заданы обработчики - зовём их по очереди
            if (rules.containsKey(logElement)) {
                for (LogVerifier operator : rules.get(logElement)) {
                    element = operator.verify(element);
                }
                // результат возвращаем в исходную строку логов
                logRecord.setElement(logElement, element);
            }
        }
        return logRecord;
    }

    @Autowired
    @Qualifier("elementSequence")
    public void setLogElementSequence(LogElement[] logElementSequence) {
        this.logElementSequence = logElementSequence;
    }
}
