package com.hall.backend.concert.application;

import com.hall.backend.concert.domain.Seat;
import com.hall.backend.concert.domain.SeatGrade;
import com.hall.backend.concert.exception.ConcertErrorCode;
import com.hall.backend.concert.exception.ConcertException;
import com.hall.backend.concert.infrastructure.SeatRepository;
import com.hall.backend.concert.presentation.dto.request.CreateSeatRequest;
import com.hall.backend.concert.presentation.dto.response.CreateSeatResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CreateSeatService {

    private final SeatRepository seatRepository;

    @Transactional
    public CreateSeatResponse create(CreateSeatRequest request) {

        Set<String> seatNumbers = new HashSet<>();
        Set<String> positions = new HashSet<>();

        List<Seat> seats = request.seats().stream().map(seatRequest -> {
                    String seatNumber = seatRequest.seatNumber().trim().toUpperCase(Locale.ROOT);

                    if (!seatNumbers.add(seatNumber)) {
                        throw new ConcertException(ConcertErrorCode.DUPLICATE_SEAT_NUMBER);
                    }

                    String position = seatRequest.rowNumber()
                            + "-"
                            + seatRequest.columnNumber();

                    if (!positions.add(position)) {
                        throw new ConcertException(ConcertErrorCode.DUPLICATE_SEAT_POSITION);
                    }

                    SeatGrade grade = SeatGrade.valueOf(
                            seatRequest.grade().trim().toUpperCase(Locale.ROOT));

                    return Seat.create(seatNumber, grade, seatRequest.rowNumber(),
                            seatRequest.columnNumber());
                })
                .toList();

        List<Seat> savedSeats = seatRepository.saveAllAndFlush(seats);

        return CreateSeatResponse.from(savedSeats);
    }
}
