package com.hall.backend.concert.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_concerts")
@Entity
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String artist;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ConcertStatus status;

    public Concert(
            String title,
            String artist,
            String description
    ) {
        this.title = title;
        this.artist = artist;
        this.description = description;
        this.status = ConcertStatus.PREPARING;
    }

    public void open() {
        this.status = ConcertStatus.OPEN;
    }

    public void close() {
        this.status = ConcertStatus.CLOSED;
    }
}
