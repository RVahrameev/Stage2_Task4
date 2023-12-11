package vtb.courses.stage2;


import jakarta.persistence.*;

import java.sql.Time;

@Entity
public class Logins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "access_date")
    private Time accessDate;

    @Column(name = "user_id")
    @ManyToOne @JoinColumn(name = "id")
    private User userId;

    @Column
    private String application;
}
