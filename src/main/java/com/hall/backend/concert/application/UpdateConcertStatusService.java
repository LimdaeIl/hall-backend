package com.hall.backend.concert.application;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import com.hall.backend.concert.infrastructure.ConcertRepository;
import com.hall.backend.concert.presentation.dto.request.UpdateConcertStatusRequest;
import com.hall.backend.concert.presentation.dto.response.UpdateConcertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdateConcertStatusService {

    private final ConcertRepository concertRepository;

    @Transactional
    public UpdateConcertResponse updateStatus(Long concertId, UpdateConcertStatusRequest request) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new ConcertException(ConcertErrorCode.CONCERT_NOT_FOUND));

        concert.changeStatus(request.status());

        return UpdateConcertResponse.from(concert);
    }
}
