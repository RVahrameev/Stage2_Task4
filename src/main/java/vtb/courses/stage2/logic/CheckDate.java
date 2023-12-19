package vtb.courses.stage2.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@LogTransformation
@Component
public class CheckDate implements UnaryOperator<String> {
    @Override
    public String apply(String s) {
        if ((s == null) || s.isBlank()) {
            throw new IllegalArgumentException("Дата события не может быть пустой!");
        }
        return s;
    }

    @Bean(name = "checkDate")
    public static UnaryOperator<String> getOperation(){
        return new CheckDate();
    }

}
