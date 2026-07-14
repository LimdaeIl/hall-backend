package com.hall.backend.concert.infrastructure;

import com.hall.backend.concert.domain.Concert;
import com.hall.backend.concert.domain.ConcertStatus;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    boolean existsByTitle(String title);

    Optional<Concert> findByIdAndStatusIn(
            Long concertId,
            Collection<ConcertStatus> statuses
    );
    @Query(
            value = """
                    SELECT c
                    FROM Concert c
                    WHERE c.status IN :statuses
                      AND (
                            :title IS NULL
                            OR LOWER(c.title) LIKE LOWER(
                                CONCAT('%', :title, '%')
                            )
                      )
                      AND (
                            :artist IS NULL
                            OR LOWER(c.artist) LIKE LOWER(
                                CONCAT('%', :artist, '%')
                            )
                      )
                    """,
            countQuery = """
                    SELECT COUNT(c)
                    FROM Concert c
                    WHERE c.status IN :statuses
                      AND (
                            :title IS NULL
                            OR LOWER(c.title) LIKE LOWER(
                                CONCAT('%', :title, '%')
                            )
                      )
                      AND (
                            :artist IS NULL
                            OR LOWER(c.artist) LIKE LOWER(
                                CONCAT('%', :artist, '%')
                            )
                      )
                    """
    )
    Page<Concert> searchForMember(
            @Param("title") String title,
            @Param("artist") String artist,
            @Param("statuses") Collection<ConcertStatus> statuses,
            Pageable pageable
    );
}
