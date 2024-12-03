package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.RequestMethod;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A EventLog.
 */
@Entity
@Table(name = "event_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Size(min = 0, max = 255)
    @Column(name = "title", length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_method")
    private RequestMethod requestMethod;

    @NotNull
    @Column(name = "performed_at", nullable = false)
    private Instant performedAt;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "data")
    private String data;

    @Column(name = "entity_name")
    private String entityName;

    @ManyToOne(optional = false)
    @NotNull
    private User performedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public EventLog id(Long id) {
        this.setId(id);
        return this;
    }

    public EventLog title(String title) {
        this.setTitle(title);
        return this;
    }

    public EventLog requestMethod(RequestMethod requestMethod) {
        this.setRequestMethod(requestMethod);
        return this;
    }

    public EventLog performedAt(Instant performedAt) {
        this.setPerformedAt(performedAt);
        return this;
    }

    public EventLog data(String data) {
        this.setData(data);
        return this;
    }

    public EventLog entityName(String entityName) {
        this.setEntityName(entityName);
        return this;
    }

    public User getPerformedBy() {
        return this.performedBy;
    }

    public void setPerformedBy(User user) {
        this.performedBy = user;
    }

    public EventLog performedBy(User user) {
        this.setPerformedBy(user);
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
        if (!(o instanceof EventLog)) {
            return false;
        }
        return id != null && id.equals(((EventLog) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLog{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", requestMethod='" + getRequestMethod() + "'" +
            ", performedAt='" + getPerformedAt() + "'" +
            ", data='" + getData() + "'" +
            ", entityName='" + getEntityName() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RequestMethod getRequestMethod() {
        return this.requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Instant getPerformedAt() {
        return this.performedAt;
    }

    public void setPerformedAt(Instant performedAt) {
        this.performedAt = performedAt;
    }

    public String getData() {
        return this.data;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setData(String data) {
        this.data = data;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
