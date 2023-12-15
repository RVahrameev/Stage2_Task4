package vtb.courses.stage2;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {
        AppConfig appConfig = new AppConfig();
        LogProcessor logProcessor = new LogProcessor();
        logProcessor.uploadLogs();
    }
}
