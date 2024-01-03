package vtb.courses.stage2;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vtb.courses.stage2.logic.*;
import vtb.courses.stage2.struct.LogElement;
import vtb.courses.stage2.utils.Hronometer;

import java.io.IOException;
import java.util.Properties;

/**
 * AppConfig - класс конфигурации приложения Spring<p>
 * Объявляет и настраивает все необходимые приложению бины.<p>
 * Подгружает необходимые настройки из ресурсного файла config.ini
 */

@Configuration
public class AppConfig {

    private LogVerifier fioAction;
    private LogVerifier appCheck;
    private LogVerifier checkDate;
    private LogProcessRules logProcessRules;
    private final static Properties props = new Properties();

    public AppConfig() throws IOException {
        props.load(this.getClass().getResourceAsStream("/config.ini"));
    }

    @Bean(name = "properties")
    public Properties getProperties(){
        return props;
    }

    @Bean(name = "logSeparator")
    public String getSeparator() {
        return "\t";
    }

    @Bean(name = "elementSequence")
    public LogElement[] getLogElementSequence(){
        return new LogElement[] {LogElement.LOGIN, LogElement.FAM, LogElement.IM, LogElement.OTCH, LogElement.DATE, LogElement.APP};
    }

    @Autowired @Qualifier("stringFirstUpper")
    public void setFioAction(LogVerifier verifier) {
        fioAction = getWrapper(verifier);
    }
    @Autowired @Qualifier("checkApplication")
    public void checkApplication(LogVerifier verifier) {
        appCheck = getWrapper(verifier);
    }
    @Autowired @Qualifier("checkDate")
    public void setDateChecker(LogVerifier verifier) {
        checkDate = getWrapper(verifier);
    }

    private LogVerifier getWrapper(LogVerifier operation) {
        if (operation.getClass().isAnnotationPresent(LogTransformation.class)) {
            return (new Hronometer<LogVerifier>()).getProxy(operation, operation.getClass().getAnnotation(LogTransformation.class).value());
        } else {
            return operation;
        }
    }

    // Создаём и настраиваем на сессию бин правил обработки логов
    @Autowired
    public void setLogProcessRules(LogProcessRules logProcessRules) {
        logProcessRules.addRule(LogElement.FAM, fioAction);
        logProcessRules.addRule(LogElement.IM, fioAction);
        logProcessRules.addRule(LogElement.OTCH, fioAction);
        logProcessRules.addRule(LogElement.APP, appCheck);
        logProcessRules.addRule(LogElement.DATE, checkDate);
        this.logProcessRules = logProcessRules;
    }
}
