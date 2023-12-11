package vtb.courses.stage2;

import jakarta.persistence.*;

@Entity(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OneToMany
    private int id;
    @Column
    private String username;
    @Column
    private String fio;
}
