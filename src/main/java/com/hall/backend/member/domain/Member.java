package com.hall.backend.member.domain;

import com.hall.backend.common.persistence.entity.BaseAuditEntity;
import com.hall.backend.member.exception.MemberErrorCode;
import com.hall.backend.member.exception.MemberException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_members")
@Entity
public class Member extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", length = 500)
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone", nullable = false, unique = true, length = 11)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MemberStatus status;

    private Member(String email, String password, String name, String phone) {
        validateEmail(email);
        validatePassword(password);
        validateName(name);
        validatePhone(phone);

        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = MemberRole.MEMBER;
        this.status = MemberStatus.ACTIVATE;
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new MemberException(MemberErrorCode.NAME_REQUIRED);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new MemberException(MemberErrorCode.PASSWORD_REQUIRED);
        }
    }

    public static Member create(String email, String password, String name, String phone) {
        return new Member(email, password, name, phone);
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new MemberException(MemberErrorCode.EMAIL_REQUIRED);
        }
    }

    private void validatePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new MemberException(MemberErrorCode.PHONE_NUMBER_REQUIRED);
        }
    }

    public void updateRoleByAdmin() {
        this.role = MemberRole.ADMIN;
    }

}
