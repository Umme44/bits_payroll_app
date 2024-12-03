package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HoldFbDisbursement.
 */
@Entity
@Table(name = "hold_fb_disbursement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HoldFbDisbursement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "disbursed_at", nullable = false)
    private LocalDate disbursedAt;

    @Size(min = 0, max = 255)
    @Column(name = "remarks", length = 255)
    private String remarks;

    @ManyToOne(optional = false)
    @NotNull
    private User disbursedBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "employee", "festival" }, allowSetters = true)
    private FestivalBonusDetails festivalBonusDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public HoldFbDisbursement id(Long id) {
        this.setId(id);
        return this;
    }

    public HoldFbDisbursement disbursedAt(LocalDate disbursedAt) {
        this.setDisbursedAt(disbursedAt);
        return this;
    }

    public HoldFbDisbursement remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public User getDisbursedBy() {
        return this.disbursedBy;
    }

    public void setDisbursedBy(User user) {
        this.disbursedBy = user;
    }

    public HoldFbDisbursement disbursedBy(User user) {
        this.setDisbursedBy(user);
        return this;
    }

    public FestivalBonusDetails getFestivalBonusDetail() {
        return this.festivalBonusDetail;
    }

    public void setFestivalBonusDetail(FestivalBonusDetails festivalBonusDetails) {
        this.festivalBonusDetail = festivalBonusDetails;
    }

    public HoldFbDisbursement festivalBonusDetail(FestivalBonusDetails festivalBonusDetails) {
        this.setFestivalBonusDetail(festivalBonusDetails);
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
        if (!(o instanceof HoldFbDisbursement)) {
            return false;
        }
        return id != null && id.equals(((HoldFbDisbursement) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HoldFbDisbursement{" +
            "id=" + getId() +
            ", disbursedAt='" + getDisbursedAt() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDisbursedAt() {
        return this.disbursedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setDisbursedAt(LocalDate disbursedAt) {
        this.disbursedAt = disbursedAt;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
