package vtb.courses.stage2;

import org.springframework.stereotype.Component;

@Component
public class LogElementProcessors {
    public static String stringFirstUpper(String text) {
        return text.substring(0,1).toUpperCase() + text.substring(1).toLowerCase();
    }

    public static String checkApplication(String text) {
        if (text.equals("web") || text.equals("mobile")) {
            return text;
        } else {
            return "other:"+text;
        }
    }

    public static String checkDate(String text) {
        if ((text == null) || text.isBlank()) {
            throw new IllegalArgumentException("Дата события не может быть пустой!");
        }
        return text;
    }
}
