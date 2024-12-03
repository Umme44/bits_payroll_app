package com.bits.hr.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AttendanceSyncCache.
 */
@Entity
@Table(name = "attendance_sync_cache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttendanceSyncCache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Column(name = "employee_pin")
    private String employeePin;

    @Column(name = "timestamp")
    private Instant timestamp;

    @Column(name = "terminal")
    private Integer terminal;

    public AttendanceSyncCache() {}

    public AttendanceSyncCache(Long id, String employeePin, Timestamp timestamp, Integer terminal) {
        this.id = id;
        this.employeePin = employeePin;
        this.terminal = terminal;
        if (timestamp != null) {
            //Needed to convert into localDateTime because of time offset
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            this.timestamp = localDateTime.toInstant(ZoneOffset.of("+06:00"));
        }
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public AttendanceSyncCache id(Long id) {
        this.setId(id);
        return this;
    }

    public AttendanceSyncCache employeePin(String employeePin) {
        this.setEmployeePin(employeePin);
        return this;
    }

    public AttendanceSyncCache timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public AttendanceSyncCache terminal(Integer terminal) {
        this.setTerminal(terminal);
        return this;
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttendanceSyncCache)) {
            return false;
        }
        return id != null && id.equals(((AttendanceSyncCache) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttendanceSyncCache{" +
            "id=" + getId() +
            ", employeePin='" + getEmployeePin() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", terminal=" + getTerminal() +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeePin() {
        return this.employeePin;
    }

    public void setEmployeePin(String employeePin) {
        this.employeePin = employeePin;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTerminal() {
        return this.terminal;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }
}
