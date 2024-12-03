package com.bits.hr.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserFeedback.
 */
@Entity
@Table(name = "user_feedback")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Size(min = 0, max = 255)
    @Column(name = "suggestion", length = 255)
    private String suggestion;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UserFeedback id(Long id) {
        this.setId(id);
        return this;
    }

    public UserFeedback rating(Integer rating) {
        this.setRating(rating);
        return this;
    }

    public UserFeedback suggestion(String suggestion) {
        this.setSuggestion(suggestion);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserFeedback user(User user) {
        this.setUser(user);
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
        if (!(o instanceof UserFeedback)) {
            return false;
        }
        return id != null && id.equals(((UserFeedback) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserFeedback{" +
            "id=" + getId() +
            ", rating=" + getRating() +
            ", suggestion='" + getSuggestion() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return this.rating;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getSuggestion() {
        return this.suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
