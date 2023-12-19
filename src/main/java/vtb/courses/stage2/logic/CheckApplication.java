package vtb.courses.stage2.logic;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

@LogTransformation
@Component
public class CheckApplication implements UnaryOperator<String> {
    @Override
    public String apply(String s) {
        if (s.equals("web") || s.equals("mobile")) {
            return s;
        } else {
            return "other:" + s;
        }
    }

    @Bean(name = "checkApplication")
    public static UnaryOperator<String> getOperation(){
        return new CheckApplication();
    }

}
