package vtb.courses.stage2;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        FileScanner f = new FileScanner();
        for (String s: f.getFiles("E:\\InnoTechCourses\\Spring"))
            System.out.println(s);
        System.out.println( "Hello World!" );
    }
}
