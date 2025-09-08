package kg.mlsp.staffcontrol.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseRef{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", unique = true, nullable = false, length = 64)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_service_id")
    private SubService subService;

    @OneToMany(
            mappedBy = "role",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<Action> actions;
}