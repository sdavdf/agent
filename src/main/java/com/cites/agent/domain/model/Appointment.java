package com.cites.agent.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import com.cites.agent.domain.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "appointments",
    indexes = {
        @Index(name = "idx_appointment_professional_time", columnList = "professional_id, scheduled_at"),
        @Index(name = "idx_appointment_tenant", columnList = "tenant_id")
    }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professional_id", nullable = false)
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    @Column(name = "predicted_no_show_probability")
    private Float predictedNoShowProbability;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancelled_by")
    private String cancelledBy; // CLIENT / PROFESSIONAL / SYSTEM

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "ai_insights", columnDefinition = "jsonb")
    private String aiInsights;

    @Column(name = "reminder_count")
    @Builder.Default
    private Integer reminderCount = 0;

    @Column(columnDefinition = "text")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = AppointmentStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /* ====== MÉTODOS DE DOMINIO ====== */

    public boolean isHighRisk() {
        return predictedNoShowProbability != null && predictedNoShowProbability > 0.6f;
    }

    public LocalDateTime getEndTime() {
        return scheduledAt.plusMinutes(durationMinutes);
    }

    public boolean overlaps(LocalDateTime start, LocalDateTime end) {
        return scheduledAt.isBefore(end) && getEndTime().isAfter(start);
    }
}
