package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.AcknowledgementStatus;
import com.bits.hr.util.annotation.ValidateAlphaNumeric;
import com.bits.hr.util.annotation.ValidateNaturalText;
import com.bits.hr.util.annotation.ValidateNumeric;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A TaxAcknowledgementReceipt.
 */
@Entity
@Table(name = "tax_acknowledgement_receipt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TaxAcknowledgementReceipt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "tin_number", nullable = false)
    private String tinNumber;

    @NotNull
    @Column(name = "receipt_number", nullable = false)
    @ValidateNumeric
    private String receiptNumber;

    @NotNull
    @Column(name = "taxes_circle", nullable = false)
    @ValidateAlphaNumeric
    private String taxesCircle;

    @NotNull
    @Column(name = "taxes_zone", nullable = false)
    @ValidateNaturalText
    private String taxesZone;

    @NotNull
    @Column(name = "date_of_submission", nullable = false)
    private LocalDate dateOfSubmission;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "acknowledgement_status", nullable = false)
    private AcknowledgementStatus acknowledgementStatus;

    @Column(name = "received_at")
    private Instant receivedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    private AitConfig fiscalYear;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    @ManyToOne
    private User receivedBy;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private User updatedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public TaxAcknowledgementReceipt id(Long id) {
        this.setId(id);
        return this;
    }

    public TaxAcknowledgementReceipt tinNumber(String tinNumber) {
        this.setTinNumber(tinNumber);
        return this;
    }

    public TaxAcknowledgementReceipt receiptNumber(String receiptNumber) {
        this.setReceiptNumber(receiptNumber);
        return this;
    }

    public TaxAcknowledgementReceipt taxesCircle(String taxesCircle) {
        this.setTaxesCircle(taxesCircle);
        return this;
    }

    public TaxAcknowledgementReceipt taxesZone(String taxesZone) {
        this.setTaxesZone(taxesZone);
        return this;
    }

    public TaxAcknowledgementReceipt dateOfSubmission(LocalDate dateOfSubmission) {
        this.setDateOfSubmission(dateOfSubmission);
        return this;
    }

    public TaxAcknowledgementReceipt filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public TaxAcknowledgementReceipt acknowledgementStatus(AcknowledgementStatus acknowledgementStatus) {
        this.setAcknowledgementStatus(acknowledgementStatus);
        return this;
    }

    public TaxAcknowledgementReceipt receivedAt(Instant receivedAt) {
        this.setReceivedAt(receivedAt);
        return this;
    }

    public TaxAcknowledgementReceipt createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public TaxAcknowledgementReceipt updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public AitConfig getFiscalYear() {
        return this.fiscalYear;
    }

    public void setFiscalYear(AitConfig aitConfig) {
        this.fiscalYear = aitConfig;
    }

    public TaxAcknowledgementReceipt fiscalYear(AitConfig aitConfig) {
        this.setFiscalYear(aitConfig);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public TaxAcknowledgementReceipt employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public User getReceivedBy() {
        return this.receivedBy;
    }

    public void setReceivedBy(User user) {
        this.receivedBy = user;
    }

    public TaxAcknowledgementReceipt receivedBy(User user) {
        this.setReceivedBy(user);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public TaxAcknowledgementReceipt createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public User getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(User user) {
        this.updatedBy = user;
    }

    public TaxAcknowledgementReceipt updatedBy(User user) {
        this.setUpdatedBy(user);
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
        if (!(o instanceof TaxAcknowledgementReceipt)) {
            return false;
        }
        return id != null && id.equals(((TaxAcknowledgementReceipt) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TaxAcknowledgementReceipt{" +
            "id=" + getId() +
            ", tinNumber='" + getTinNumber() + "'" +
            ", receiptNumber='" + getReceiptNumber() + "'" +
            ", taxesCircle='" + getTaxesCircle() + "'" +
            ", taxesZone='" + getTaxesZone() + "'" +
            ", dateOfSubmission='" + getDateOfSubmission() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", acknowledgementStatus='" + getAcknowledgementStatus() + "'" +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTinNumber() {
        return this.tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getReceiptNumber() {
        return this.receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getTaxesCircle() {
        return this.taxesCircle;
    }

    public void setTaxesCircle(String taxesCircle) {
        this.taxesCircle = taxesCircle;
    }

    public String getTaxesZone() {
        return this.taxesZone;
    }

    public void setTaxesZone(String taxesZone) {
        this.taxesZone = taxesZone;
    }

    public LocalDate getDateOfSubmission() {
        return this.dateOfSubmission;
    }

    public void setDateOfSubmission(LocalDate dateOfSubmission) {
        this.dateOfSubmission = dateOfSubmission;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public AcknowledgementStatus getAcknowledgementStatus() {
        return this.acknowledgementStatus;
    }

    public void setAcknowledgementStatus(AcknowledgementStatus acknowledgementStatus) {
        this.acknowledgementStatus = acknowledgementStatus;
    }

    public Instant getReceivedAt() {
        return this.receivedAt;
    }

    public void setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
