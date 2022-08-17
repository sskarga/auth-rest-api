package com.store.restapi.pincode;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "accounts_pincode")
@Getter
@Setter
@RequiredArgsConstructor
public class PinCode {

    @Id
    private Long accountId;

    @Column(name = "code", nullable = false)
    private Integer code;

    @Column(name = "code_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private CodeType codeType;

    @Column(name="created_on")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name="expires_at", nullable = false)
    private LocalDateTime expiresAt;

    public PinCode(Long accountId, Integer code, CodeType codeType, LocalDateTime expiresAt) {
        this.accountId = accountId;
        this.code = code;
        this.codeType = codeType;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PinCode pinCode = (PinCode) o;
        return accountId != null && Objects.equals(accountId, pinCode.accountId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PinCode{" +
                "AccountId=" + accountId +
                ", code=" + code +
                ", pinType=" + codeType +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
