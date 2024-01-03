package vtb.courses.stage2.logic;

import org.springframework.stereotype.Component;

/**
 * CheckDate - компонент реализующий проверка поля даты в сторке логов
 */
@LogTransformation
@Component
public class CheckDate implements LogVerifier {

    @Override
    public String verify(String strToCheck) {
        if ((strToCheck == null) || strToCheck.isBlank()) {
            throw new IllegalArgumentException("Дата события не может быть пустой!");
        }
        return strToCheck;
    }

}
