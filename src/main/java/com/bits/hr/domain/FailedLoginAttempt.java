package com.bits.hr.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FailedLoginAttempt.
 */
@Entity
@Table(name = "failed_login_attempt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FailedLoginAttempt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @NotNull
    @Column(name = "continuous_failed_attempts", nullable = false)
    private Integer continuousFailedAttempts;

    @Column(name = "last_failed_attempt")
    private Instant lastFailedAttempt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FailedLoginAttempt id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public FailedLoginAttempt userName(String userName) {
        this.setUserName(userName);
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getContinuousFailedAttempts() {
        return this.continuousFailedAttempts;
    }

    public FailedLoginAttempt continuousFailedAttempts(Integer continuousFailedAttempts) {
        this.setContinuousFailedAttempts(continuousFailedAttempts);
        return this;
    }

    public void setContinuousFailedAttempts(Integer continuousFailedAttempts) {
        this.continuousFailedAttempts = continuousFailedAttempts;
    }

    public Instant getLastFailedAttempt() {
        return this.lastFailedAttempt;
    }

    public FailedLoginAttempt lastFailedAttempt(Instant lastFailedAttempt) {
        this.setLastFailedAttempt(lastFailedAttempt);
        return this;
    }

    public void setLastFailedAttempt(Instant lastFailedAttempt) {
        this.lastFailedAttempt = lastFailedAttempt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FailedLoginAttempt)) {
            return false;
        }
        return id != null && id.equals(((FailedLoginAttempt) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FailedLoginAttempt{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", continuousFailedAttempts=" + getContinuousFailedAttempts() +
            ", lastFailedAttempt='" + getLastFailedAttempt() + "'" +
            "}";
    }
}
