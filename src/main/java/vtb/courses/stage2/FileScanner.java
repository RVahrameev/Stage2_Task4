package vtb.courses.stage2;

import java.io.File;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileScanner {
    Stream
    public String[] getFiles(String path) {
        return Arrays.stream((new File(path)).listFiles()).filter(x -> x.isFile()).map(x -> x.getName()).toArray(String[]::new);
    }
}
