package kg.mlsp.staffcontrol.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 150)
    private String email;

    @Column(name = "last_login_date")
    private LocalDateTime lastLoginDate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "must_change_password")
    private boolean mustChangePassword = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    private Staff staff;

    @OneToMany(mappedBy = "user")
    private List<UserSubServiceRoleAction> userSubServiceRoleActions;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public List<Role> getRoles() {
        return userSubServiceRoleActions == null ? List.of() :
                userSubServiceRoleActions.stream()
                        .map(UserSubServiceRoleAction::getRole)
                        .distinct()
                        .toList();
    }

    public List<SubService> getSubServices() {
        return userSubServiceRoleActions == null ? List.of() :
                userSubServiceRoleActions.stream()
                        .map(UserSubServiceRoleAction::getSubService)
                        .distinct()
                        .toList();
    }
}