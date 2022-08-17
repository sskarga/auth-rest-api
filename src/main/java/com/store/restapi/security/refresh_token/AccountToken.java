package com.store.restapi.security.refresh_token;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "accounts_token")
@Getter
@Setter
@NoArgsConstructor
public class AccountToken {
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "UUID", strategy = "uuid4")
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(name="account_id")
    private Long accountId;

    @Column(name="created_on")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column(name="expires_at")
    private LocalDateTime expiresAt;

    public AccountToken(Long accountId, LocalDateTime expiresAt) {
        this.id = UUID.randomUUID();
        this.accountId = accountId;
        this.expiresAt = expiresAt;
    }

    public AccountToken(UUID id, Long accountId, LocalDateTime createdOn, LocalDateTime expiresAt) {
        this.id = id;
        this.accountId = accountId;
        this.createdOn = createdOn;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccountToken that = (AccountToken) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AccountToken{" +
                "id=" + id +
                ", createdOn=" + createdOn +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
