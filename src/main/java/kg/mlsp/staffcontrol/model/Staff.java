package kg.mlsp.staffcontrol.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kg.mlsp.staffcontrol.converter.CryptoConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staffs")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 255)
    @Convert(converter = CryptoConverter.class)
    private String pin;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "phone", length = 20)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "organization_id", referencedColumnName = "id", nullable = true)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "position_id", referencedColumnName = "id", nullable = true)
    private Position position;

    public String getOrganizationCode() {
        return organization != null ? organization.getCode() : null;
    }

    public String getPositionCode() {
        return position != null ? position.getCode() : null;
    }
}
