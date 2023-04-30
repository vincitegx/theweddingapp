package com.slinkdigital.user.domain;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
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
@Table(name = "password_reset_token", indexes = {
    @Index(columnList = "email, resetToken", unique = true, name = "unique_reset_token_index")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetToken implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false, unique = true)
    private String resetToken;
    
    @Column(nullable = false, updatable = false)
    protected Instant createdAt;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    public PasswordResetToken(String email, LocalDateTime expiresAt) {
        this.email = email;
        this.expiresAt = expiresAt;
    }
    
    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}
