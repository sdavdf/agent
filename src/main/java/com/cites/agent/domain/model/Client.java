package com.cites.agent.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
  name = "clients",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenant_id", "email"}),
    @UniqueConstraint(columnNames = {"tenant_id", "phone"})
  }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Client {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String phone;
    
    private String dni;
    
    @Column(columnDefinition = "jsonb")
    private String preferences;
    
    @Column(name = "no_show_risk_score")
    private Float noShowRiskScore;
    
    @Column(name = "total_appointments")
    private Integer totalAppointments = 0;
    
    @Column(name = "completed_appointments")
    private Integer completedAppointments = 0;
    
    @Column(name = "no_show_count")
    private Integer noShowCount = 0;
    
    @Column(name = "cancellation_count")
    private Integer cancellationCount = 0;
    
    @Column(name = "last_appointment_at")
    private LocalDateTime lastAppointmentAt;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public double getNoShowRate() {
        if (totalAppointments == 0) return 0.0;
        return (double) noShowCount / totalAppointments;
    }
}
