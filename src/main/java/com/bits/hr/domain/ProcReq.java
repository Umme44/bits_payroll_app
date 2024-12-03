package com.bits.hr.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A ProcReq.
 */
@Entity
@Table(name = "proc_req")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @DecimalMin(value = "1")
    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "reference_file_path")
    private String referenceFilePath;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "department", "unitOfMeasurement", "createdBy", "updatedBy" }, allowSetters = true)
    private ItemInformation itemInformation;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "department",
            "requestedBy",
            "recommendedBy01",
            "recommendedBy02",
            "recommendedBy03",
            "recommendedBy04",
            "recommendedBy05",
            "nextApprovalFrom",
            "rejectedBy",
            "closedBy",
            "updatedBy",
            "createdBy",
            "procReqs",
        },
        allowSetters = true
    )
    private ProcReqMaster procReqMaster;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public ProcReq id(Long id) {
        this.setId(id);
        return this;
    }

    public ProcReq quantity(Double quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public ProcReq referenceFilePath(String referenceFilePath) {
        this.setReferenceFilePath(referenceFilePath);
        return this;
    }

    public ItemInformation getItemInformation() {
        return this.itemInformation;
    }

    public void setItemInformation(ItemInformation itemInformation) {
        this.itemInformation = itemInformation;
    }

    public ProcReq itemInformation(ItemInformation itemInformation) {
        this.setItemInformation(itemInformation);
        return this;
    }

    public ProcReqMaster getProcReqMaster() {
        return this.procReqMaster;
    }

    public void setProcReqMaster(ProcReqMaster procReqMaster) {
        this.procReqMaster = procReqMaster;
    }

    public ProcReq procReqMaster(ProcReqMaster procReqMaster) {
        this.setProcReqMaster(procReqMaster);
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
        if (!(o instanceof ProcReq)) {
            return false;
        }
        return id != null && id.equals(((ProcReq) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcReq{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", referenceFilePath='" + getReferenceFilePath() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getReferenceFilePath() {
        return this.referenceFilePath;
    }

    public void setReferenceFilePath(String referenceFilePath) {
        this.referenceFilePath = referenceFilePath;
    }
}
