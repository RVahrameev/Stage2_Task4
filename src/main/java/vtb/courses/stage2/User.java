package vtb.courses.stage2;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
public class User {

    private static List<User> users = new ArrayList<>();

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String username;
    @Column
    private String fio;

    public User() {
    }

    private User(String username, String fio) {
        this.username = username;
        this.fio = fio;
    }
    private static int getUserIdx(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).username == username) {
                return i;
            }
        }
        return -1;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static User getUser(String username, String fio) {
        int idx = getUserIdx(username);
        if (idx == -1) {
            return new User(username, fio);
        } else {
            return users.get(idx);
        }
    }

    public static User getUser(LogRecord logRecord) {
        User user = getUser(logRecord.getElement(LogElement.LOGIN),
                String.join(" ",
                        logRecord.getElement(LogElement.FAM),
                        logRecord.getElement(LogElement.IM),
                        logRecord.getElement(LogElement.OTCH)
                )
        );
        return user;
    }


}
