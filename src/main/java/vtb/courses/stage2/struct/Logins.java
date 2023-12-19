package vtb.courses.stage2.struct;


import jakarta.persistence.*;

import java.sql.Timestamp;

/**
 * Класс Logins представляет хранимую в БД сущьность о действиях пользователя
 */

@Entity
public class Logins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "access_date")
    private Timestamp accessDate;

    @ManyToOne(targetEntity = User.class) @JoinColumn(name = "user_id")
    private User userId;

    @Column
    private String application;

    public Logins() {
    }

    public Logins(LogRecord logRecord, User user) {
        this.accessDate = Timestamp.valueOf(logRecord.getElement(LogElement.DATE));
        this.userId = user;
        this.application = logRecord.getElement(LogElement.APP);
    }

    public int getId() {
        return id;
    }

    public Timestamp getAccessDate() {
        return accessDate;
    }

    public User getUserId() {
        return userId;
    }

    public String getApplication() {
        return application;
    }
}
