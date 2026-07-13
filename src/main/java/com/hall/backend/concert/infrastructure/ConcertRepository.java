package com.hall.backend.concert.infrastructure;

import com.hall.backend.concert.domain.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    boolean existsByTitle(String title);
}
