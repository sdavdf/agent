package com.cites.agent.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import com.cites.agent.domain.enums.BusinessType;
import com.cites.agent.domain.enums.SubscriptionPlan;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {
    @Id
    @GeneratedValue(generator = "UUID")
    // Nota: GenericGenerator está deprecado en Hibernate 6.5. Considera actualizar
    // a una alternativa si es posible.
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", nullable = false)
    private BusinessType businessType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionPlan plan;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "subscription_expires_at")
    private LocalDateTime subscriptionExpiresAt;

    @Column(name = "max_professionals")
    private Integer maxProfessionals;

    @Column(name = "max_monthly_appointments")
    private Integer maxMonthlyAppointments;

    @Column(name = "current_month_appointments")
    private Integer currentMonthAppointments = 0;

    @Column(columnDefinition = "jsonb")
    private String settings;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String timezone; 

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}