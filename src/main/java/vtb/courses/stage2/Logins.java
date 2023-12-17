package vtb.courses.stage2;


import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class Logins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "access_date")
    private Timestamp accessDate;

    @Column(name = "user_id")
    @ManyToOne(targetEntity = User.class) @JoinColumn(name = "id")
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

}
