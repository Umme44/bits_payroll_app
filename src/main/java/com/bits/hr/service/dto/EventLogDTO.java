package com.bits.hr.service.dto;

import com.bits.hr.domain.enumeration.RequestMethod;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.bits.hr.domain.EventLog} entity.
 */
public class EventLogDTO implements Serializable {

    private Long id;

    @Size(min = 0, max = 255)
    private String title;

    private RequestMethod requestMethod;

    @NotNull
    private Instant performedAt;

    @Lob
    private String data;

    private String entityName;

    private Long performedById;

    private String performedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Instant getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(Instant performedAt) {
        this.performedAt = performedAt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getPerformedById() {
        return performedById;
    }

    public void setPerformedById(Long userId) {
        this.performedById = userId;
    }

    public String getPerformedByLogin() {
        return performedByLogin;
    }

    public void setPerformedByLogin(String userLogin) {
        this.performedByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventLogDTO)) {
            return false;
        }

        return id != null && id.equals(((EventLogDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventLogDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", requestMethod='" + getRequestMethod() + "'" +
            ", performedAt='" + getPerformedAt() + "'" +
            ", data='" + getData() + "'" +
            ", entityName='" + getEntityName() + "'" +
            ", performedById=" + getPerformedById() +
            ", performedByLogin='" + getPerformedByLogin() + "'" +
            "}";
    }
}
