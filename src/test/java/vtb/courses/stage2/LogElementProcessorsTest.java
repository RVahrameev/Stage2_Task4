package vtb.courses.stage2;

import jakarta.persistence.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LogElementProcessorsTest {

    @Test
    @DisplayName("Тестирование функций преобразования полей лога. Обработчиков класса LogElementProcessors")
    public void test(){
        Assertions.assertEquals("Test", LogElementProcessors.stringFirstUpper("teSt"), "Не работае функция stringFirstUpper");
        Assertions.assertEquals("web-mobile-other:other", LogElementProcessors.checkApplication("web")+'-'+LogElementProcessors.checkApplication("mobile")+'-'+LogElementProcessors.checkApplication("other"), "Не корректно работает функция checkApplication");
        Assertions.assertThrows(IllegalArgumentException.class, ()->LogElementProcessors.checkDate(null), "Не работае функция checkDate нет исключения при дате = null");
        Assertions.assertThrows(IllegalArgumentException.class, ()->LogElementProcessors.checkDate(" "), "Не работае функция checkDate нет исключения при пустой дате");
    }
}
