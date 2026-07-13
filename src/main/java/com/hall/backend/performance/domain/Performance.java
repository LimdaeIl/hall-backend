package com.hall.backend.performance.domain;

import com.hall.backend.concert.domain.Concert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_performances")
@Entity
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime reservationOpensAt;

    @Column(nullable = false)
    private LocalDateTime reservationClosesAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PerformanceStatus status;

    public boolean isReservable(LocalDateTime now) {
        return status == PerformanceStatus.OPEN
                && !now.isBefore(reservationOpensAt)
                && now.isBefore(reservationClosesAt);
    }
}