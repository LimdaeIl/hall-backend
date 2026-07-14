package com.hall.backend.concert.domain;

import com.hall.backend.common.persistence.entity.BaseAuditEntity;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
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
public class Concert extends BaseAuditEntity {

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
    @Column(nullable = false)
    private ConcertStatus status;

    private Concert(String title, String artist, String description) {
        validateTitle(title);
        validateArtist(artist);

        this.title = title;
        this.artist = artist;
        this.description = description;
        this.status = ConcertStatus.PREPARING;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ConcertException(ConcertErrorCode.TITLE_CANNOT_BE_NULL_OR_BLANK);
        }
    }

    private void validateArtist(String artist) {
        if (artist == null || artist.isBlank()) {
            throw new ConcertException(ConcertErrorCode.ARTIST_CANNOT_BE_NULL_OR_BLANK);
        }
    }


    public static Concert create(String title, String artist, String description) {
        return new Concert(title, artist, description);
    }

    public void open() {
        this.status = ConcertStatus.OPEN;
    }

    public void close() {
        this.status = ConcertStatus.CLOSED;
    }

    public void updateInformation(
            String title,
            String artist,
            String description
    ) {
        if (title != null) {
            validateTitle(title);
            this.title = title.trim();
        }

        if (artist != null) {
            validateArtist(artist);
            this.artist = artist.trim();
        }

        if (description != null) {
            validateDescription(description);
            this.description = description.trim();
        }
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > 1000) {
            throw new ConcertException(ConcertErrorCode.DESCRIPTION_TOO_LONG);
        }
    }

    public void changeStatus(ConcertStatus newStatus) {
        if (newStatus == null) {
            throw new ConcertException(ConcertErrorCode.CONCERT_STATUS_REQUIRED);
        }

        if (this.status == newStatus) {
            throw new ConcertException(ConcertErrorCode.CONCERT_STATUS_NOT_CHANGED);
        }

        validateStatusTransition(newStatus);
        this.status = newStatus;
    }

    private void validateStatusTransition(
            ConcertStatus newStatus
    ) {
        boolean validTransition = switch (this.status) {
            case PREPARING ->
                    newStatus == ConcertStatus.OPEN || newStatus == ConcertStatus.CANCELLED;

            case OPEN -> newStatus == ConcertStatus.CLOSED || newStatus == ConcertStatus.CANCELLED;

            case CLOSED, CANCELLED -> false;
        };

        if (!validTransition) {
            throw new ConcertException(ConcertErrorCode.INVALID_CONCERT_STATUS_TRANSITION);
        }
    }
}
