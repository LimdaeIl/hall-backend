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

    /**
     * 회원용 공연 검색
     *
     * 검색 조건:
     * - 지정된 상태에 해당하는 공연만 조회
     * - 제목 부분 일치 검색
     * - 아티스트 부분 일치 검색
     */
    @Query(
            value = """
                    SELECT c
                    FROM Concert c
                    WHERE c.status IN :statuses
                      AND (
                          :title IS NULL
                          OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))
                      )
                      AND (
                          :artist IS NULL
                          OR LOWER(c.artist) LIKE LOWER(CONCAT('%', :artist, '%'))
                      )
                    """,
            countQuery = """
                    SELECT COUNT(c)
                    FROM Concert c
                    WHERE c.status IN :statuses
                      AND (
                          :title IS NULL
                          OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))
                      )
                      AND (
                          :artist IS NULL
                          OR LOWER(c.artist) LIKE LOWER(CONCAT('%', :artist, '%'))
                      )
                    """
    )
    Page<Concert> searchForMember(
            @Param("title") String title,
            @Param("artist") String artist,
            @Param("statuses") Collection<ConcertStatus> statuses,
            Pageable pageable
    );

    /**
     * 관리자용 공연 검색
     *
     * 검색 조건:
     * - 제목 부분 일치 검색
     * - 아티스트 부분 일치 검색
     * - 공연 상태 일치 검색
     */
    @Query(
            value = """
                    SELECT c
                    FROM Concert c
                    WHERE (
                        :title IS NULL
                        OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))
                    )
                      AND (
                          :artist IS NULL
                          OR LOWER(c.artist) LIKE LOWER(CONCAT('%', :artist, '%'))
                      )
                      AND (
                          :status IS NULL
                          OR c.status = :status
                      )
                    """,
            countQuery = """
                    SELECT COUNT(c)
                    FROM Concert c
                    WHERE (
                        :title IS NULL
                        OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))
                    )
                      AND (
                          :artist IS NULL
                          OR LOWER(c.artist) LIKE LOWER(CONCAT('%', :artist, '%'))
                      )
                      AND (
                          :status IS NULL
                          OR c.status = :status
                      )
                    """
    )
    Page<Concert> searchForAdmin(
            @Param("title") String title,
            @Param("artist") String artist,
            @Param("status") ConcertStatus status,
            Pageable pageable
    );
}
