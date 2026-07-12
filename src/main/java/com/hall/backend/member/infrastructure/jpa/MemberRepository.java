package com.hall.backend.member.infrastructure.jpa;

import com.hall.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean findByName(String name);
}
