package com.hall.backend.concert.application;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import com.hall.backend.concert.infrastructure.ConcertRepository;
import com.hall.backend.concert.presentation.dto.request.UpdateConcertRequest;
import com.hall.backend.concert.presentation.dto.response.UpdateConcertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateConcertService {

    private final ConcertRepository concertRepository;

    @Transactional
    public UpdateConcertResponse update(Long concertId, UpdateConcertRequest request) {
        if (request.hasNoChanges()) {
            throw new ConcertException(ConcertErrorCode.NO_CONCERT_UPDATE_FIELDS);
        }

        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() ->
                        new ConcertException(ConcertErrorCode.CONCERT_NOT_FOUND)
                );

        validateDuplicateTitle(concert, request.title());

        concert.updateInformation(
                normalize(request.title()),
                normalize(request.artist()),
                normalizeDescription(request.description())
        );

        return UpdateConcertResponse.from(concert);
    }

    private void validateDuplicateTitle(Concert concert, String requestedTitle) {
        String normalizedTitle = normalize(requestedTitle);

        if (normalizedTitle == null) {
            return;
        }

        if (normalizedTitle.equals(concert.getTitle())) {
            return;
        }

        if (concertRepository.existsByTitle(normalizedTitle)) {
            throw new ConcertException(ConcertErrorCode.DUPLICATE_CONCERT_TITLE);
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        return value.trim();
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }

        return description.trim();
    }
}


