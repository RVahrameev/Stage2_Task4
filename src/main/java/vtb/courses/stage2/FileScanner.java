package vtb.courses.stage2;

import java.io.File;
import java.util.Arrays;

public class FileScanner {
    public String[] getFiles(String path) {
        return Arrays.stream((new File(path)).listFiles()).filter(x -> x.isFile()).map(x -> x.getName()).toArray(String[]::new);
    }
}
