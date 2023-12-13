package vtb.courses.stage2;

import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class FolderLogs implements Iterator {
    private String path;

    public FolderLogs(String path) {
        this.path = path;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }
}
