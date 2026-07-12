package com.hall.backend.member.infrastructure.jpa;

import com.hall.backend.member.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean findByName(String name);

    Optional<Member> findByEmail(String email);
}
