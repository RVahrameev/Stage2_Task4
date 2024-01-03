package vtb.courses.stage2.logic;

import org.springframework.stereotype.Component;

/**
 * StringFirstUpper - компонент реализующий нормализацию ФИО в строке лога
 */
@LogTransformation("StringFirstUpper.log")
@Component
public class StringFirstUpper implements LogVerifier {

    @Override
    public String verify(String strToCheck) {
        return strToCheck.substring(0,1).toUpperCase() + strToCheck.substring(1).toLowerCase();
    }

}
