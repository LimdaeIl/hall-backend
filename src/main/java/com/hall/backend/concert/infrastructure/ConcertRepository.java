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

public interface ConcertRepository
        extends JpaRepository<Concert, Long> {

    boolean existsByTitle(String title);

    Optional<Concert> findByIdAndStatusIn(
            Long concertId,
            Collection<ConcertStatus> statuses
    );

    @Query(
            value = """
                    select c
                    from Concert c
                    where c.status in :statuses
                      and (
                            :title is null
                            or lower(c.title) like lower(
                                concat('%', :title, '%')
                            )
                      )
                      and (
                            :artist is null
                            or lower(c.artist) like lower(
                                concat('%', :artist, '%')
                            )
                      )
                    """,
            countQuery = """
                    select count(c)
                    from Concert c
                    where c.status in :statuses
                      and (
                            :title is null
                            or lower(c.title) like lower(
                                concat('%', :title, '%')
                            )
                      )
                      and (
                            :artist is null
                            or lower(c.artist) like lower(
                                concat('%', :artist, '%')
                            )
                      )
                    """
    )
    Page<Concert> searchForMember(
            @Param("title") String title,
            @Param("artist") String artist,
            @Param("statuses")
            Collection<ConcertStatus> statuses,
            Pageable pageable
    );

    @Query(
            value = """
                    select c
                    from Concert c
                    where (
                            :title is null
                            or lower(c.title) like lower(
                                concat('%', :title, '%')
                            )
                          )
                      and (
                            :artist is null
                            or lower(c.artist) like lower(
                                concat('%', :artist, '%')
                            )
                          )
                      and (
                            :status is null
                            or c.status = :status
                          )
                    """,
            countQuery = """
                    select count(c)
                    from Concert c
                    where (
                            :title is null
                            or lower(c.title) like lower(
                                concat('%', :title, '%')
                            )
                          )
                      and (
                            :artist is null
                            or lower(c.artist) like lower(
                                concat('%', :artist, '%')
                            )
                          )
                      and (
                            :status is null
                            or c.status = :status
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
