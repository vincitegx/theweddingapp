package com.slinkdigital.user.domain;

import com.slinkdigital.user.domain.security.Role;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author TEGA
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = "email", name="uniqueEmailContraint")
})
public class Users implements Serializable {
    
//    public Users(Long id){
//        this.id = id;
//    }
//    
//    public Users(String email, String password){
//        this.email = email;
//        this.password = password;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean nonLocked;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name="users_role", joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
}
