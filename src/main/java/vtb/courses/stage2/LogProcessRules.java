package vtb.courses.stage2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@Component
public class LogProcessRules implements UnaryOperator<LogRecord> {
    private HashMap<LogElement, UnaryOperator<String>[]> rules = new HashMap<>();
    @Autowired
    @Qualifier("elementSequence")
    private LogElement[] logElementSequence;

    public void addRule(LogElement logElement, UnaryOperator<String>... rules) {
        this.rules.put(logElement, rules);
    }

    public String processRulesOn(LogElement logElement, String element) {
        for (UnaryOperator<String> operator: rules.get(logElement)) {
            element = operator.apply(element);
        }
        return element;
    }

    // Метод применяет к записи лога правила обработки
    @Override
    public LogRecord apply(LogRecord logRecord) {
        for (LogElement logElement: logElementSequence) {
            String element = logRecord.getElement(logElement);
            for (UnaryOperator<String> operator: rules.get(logElement)) {
                element = operator.apply(element);
            }
            logRecord.setElement(logElement, element);
        }
        return logRecord;
    }
}
