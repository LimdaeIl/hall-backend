package com.hall.backend.concert.application;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import com.hall.backend.concert.infrastructure.ConcertRepository;
import com.hall.backend.concert.presentation.dto.request.CreateConcertRequest;
import com.hall.backend.concert.presentation.dto.response.CreateConcertResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateConcertService {

    private final ConcertRepository concertRepository;


    @Transactional
    public CreateConcertResponse create(CreateConcertRequest request) {
        Concert concert = Concert.create(request.title(), request.artist(), request.description());
        concertRepository.save(concert);

        return CreateConcertResponse.from(concert);
    }
}
