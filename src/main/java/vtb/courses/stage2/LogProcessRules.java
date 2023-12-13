package vtb.courses.stage2;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.function.UnaryOperator;

@Component
public class LogProcessRules {
    private HashMap<LogElement, UnaryOperator<String>[]> rules = new HashMap<>();

    public void addRule(LogElement logElement, UnaryOperator<String>... rules) {
        this.rules.put(logElement, rules);
    }

    public String processRulesOn(LogElement logElement, String element) {
        for (UnaryOperator<String> operator: rules.get(logElement)) {
            element = operator.apply(element);
        }
        return element;
    }
}
