package vtb.courses.stage2.struct;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  Класс User представляет хранимую в БД сущность о пользователях.<p>
 *  Также класс используется для хранния кеша пользователей, чтобы каждый раз не лазить в БД
 */

@Entity(name = "users")
public class User {

    private final static List<User> users = new ArrayList<>();

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

    /**
     * getUserIdx - ищет пользователя во внутреннем кеше
     */
    private static int getUserIdx(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).username.equals(username)) {
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

    public String getFio() {
        return fio;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * getUser - возвращает объект пользователя из внутреннего кеша или создаёт новый
     */
    public static User getUser(String username, String fio) {
        // сначала ищем пользователя в кеше
        int idx = getUserIdx(username);
        // если не нашли - создаём и сохраняем в кеш
        if (idx == -1) {
            User newUser = new User(username, fio);
            users.add(newUser);
            return newUser;
        } else {
            return users.get(idx);
        }
    }

    /**
     * getUser - возвращает объект пользователя из внутреннего кеша или создаёт новый
     */
    public static User getUser(LogRecord logRecord) {
        // просто перевызов к перекрытому методу
        return getUser(logRecord.getElement(LogElement.LOGIN),
                String.join(" ",
                        logRecord.getElement(LogElement.FAM),
                        logRecord.getElement(LogElement.IM),
                        logRecord.getElement(LogElement.OTCH)
                )
        );
    }


}
