package com.bits.hr.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Config.
 */
@Entity
@Table(name = "config")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "key", length = 255, nullable = false, unique = true)
    private String key;

    @Size(min = 1, max = 255)
    @Column(name = "value", length = 255)
    private String value;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Config id(Long id) {
        this.setId(id);
        return this;
    }

    public Config key(String key) {
        this.setKey(key);
        return this;
    }

    public Config value(String value) {
        this.setValue(value);
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
        if (!(o instanceof Config)) {
            return false;
        }
        return id != null && id.equals(((Config) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Config{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
