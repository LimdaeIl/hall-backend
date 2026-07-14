package com.hall.backend.member.infrastructure.jpa;

import com.hall.backend.member.domain.Member;
import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Member> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select m
        from Member m
        where m.id = :memberId
        """)
    Optional<Member> findByIdForUpdate(
            @Param("memberId") Long memberId
    );

    boolean existsByName(String name);
}
