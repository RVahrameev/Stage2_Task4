package vtb.courses.stage2.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

/**
 * StringFirstUpper - компонент реализующий нормализацию ФИО в строке лога
 */
@LogTransformation("StringFirstUpper.log")
@Component
public class StringFirstUpper implements UnaryOperator<String> {
    @Override
    public String apply(String s) {
        return s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
    }

    @Bean(name = "stringFirstUpper")
    public static UnaryOperator<String> getOperation() {
        return new StringFirstUpper();
    }
}
