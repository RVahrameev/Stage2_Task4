package vtb.courses.stage2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Properties;

import static java.lang.System.nanoTime;

/**
 * Класс Hronometer - служит для замера времени исполения методов заданых интерфейсом T
 * и сохранения информации в файл
 */
@Component
public class Hronometer<T> implements InvocationHandler {
    private T wrappedObject;
    private static String logPath;
    private String logFileName;

    @Autowired
    @Qualifier("properties")
    public void setProperties(Properties properties) {
        logPath = properties.getProperty("ERROR_LOG_DIR");
        logPath = logPath.endsWith("/") ? logPath : logPath + '/';
    }
    public T getProxy(T object, String logFileName) {
        wrappedObject = object;
        this.logFileName = ((logFileName == null || logFileName.isEmpty())?"Hronometer.log":logFileName);
        return (T) Proxy.newProxyInstance(
                object.getClass().getClassLoader(),
                object.getClass().getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startNano = nanoTime();
        LocalDateTime startTime = LocalDateTime.now();
        Object result = method.invoke(wrappedObject, args);
        if (logPath != null) {
            Files.write(Path.of(logPath+logFileName), (startTime+"\t"+wrappedObject.getClass().getName()+"\t"+args[0]+"\t"+result+"\t"+(nanoTime()-startNano)+"нс\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        return result;
    }
}
