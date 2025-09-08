package kg.mlsp.staffcontrol.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "sub_services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubService extends BaseRef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", unique = true, nullable = false, length = 64)
    private String code;

    @OneToMany(
            mappedBy = "subService",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<Role> roles;
}
