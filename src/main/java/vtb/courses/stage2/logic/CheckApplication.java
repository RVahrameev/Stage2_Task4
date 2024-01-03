package vtb.courses.stage2.logic;

import org.springframework.stereotype.Component;

/**
 * CheckApplication - компонент реализующий функцию проверки поля приложения в строке логов
 */
@LogTransformation
@Component
public class CheckApplication implements LogVerifier {

    @Override
    public String verify(String strToCheck) {
        if (strToCheck.equals("web") || strToCheck.equals("mobile")) {
            return strToCheck;
        } else {
            return "other:" + strToCheck;
        }
    }
}
