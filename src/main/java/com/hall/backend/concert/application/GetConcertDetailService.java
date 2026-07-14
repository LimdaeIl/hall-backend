package com.hall.backend.concert.application;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.domain.ConcertStatus;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import com.hall.backend.concert.infrastructure.ConcertRepository;
import com.hall.backend.concert.presentation.dto.response.GetConcertDetailResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GetConcertDetailService {

    private static final List<ConcertStatus> MEMBER_VISIBLE_STATUSES =
            List.of(
                    ConcertStatus.OPEN,
                    ConcertStatus.CLOSED
            );

    private final ConcertRepository concertRepository;

    @Transactional(readOnly = true)
    public GetConcertDetailResponse getConcert(Long concertId) {
        Concert concert = concertRepository.findByIdAndStatusIn(concertId, MEMBER_VISIBLE_STATUSES)
                .orElseThrow(() -> new ConcertException(ConcertErrorCode.CONCERT_NOT_FOUND));

        return GetConcertDetailResponse.from(concert);
    }
}
