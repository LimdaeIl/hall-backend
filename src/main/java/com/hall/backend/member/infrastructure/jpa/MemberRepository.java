package com.hall.backend.member.infrastructure.jpa;

import com.hall.backend.member.domain.Member;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByName(String name);

    Optional<Member> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT m
            FROM Member m
            WHERE m.id = :memberId
            """)
    Optional<Member> findByIdForUpdate(
            @Param("memberId") Long memberId
    );
}
