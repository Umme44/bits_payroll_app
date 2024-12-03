package com.bits.hr.domain;

import com.bits.hr.domain.enumeration.LeaveAmountType;
import com.bits.hr.domain.enumeration.LeaveType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LeaveBalance.
 */
@Entity
@Table(name = "leave_balance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeaveBalance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "sequence_generator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type")
    private LeaveType leaveType;

    @Column(name = "opening_balance")
    private Integer openingBalance;

    @Column(name = "closing_balance")
    private Integer closingBalance;

    @Column(name = "consumed_during_year")
    private Integer consumedDuringYear;

    @Column(name = "year")
    private Integer year;

    @Column(name = "amount")
    private Integer amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_amount_type")
    private LeaveAmountType leaveAmountType;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "designation", "department", "reportingTo", "nationality", "bankBranch", "band", "unit", "user" },
        allowSetters = true
    )
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public LeaveBalance id(Long id) {
        this.setId(id);
        return this;
    }

    public LeaveBalance leaveType(LeaveType leaveType) {
        this.setLeaveType(leaveType);
        return this;
    }

    public LeaveBalance openingBalance(Integer openingBalance) {
        this.setOpeningBalance(openingBalance);
        return this;
    }

    public LeaveBalance closingBalance(Integer closingBalance) {
        this.setClosingBalance(closingBalance);
        return this;
    }

    public LeaveBalance consumedDuringYear(Integer consumedDuringYear) {
        this.setConsumedDuringYear(consumedDuringYear);
        return this;
    }

    public LeaveBalance year(Integer year) {
        this.setYear(year);
        return this;
    }

    public LeaveBalance amount(Integer amount) {
        this.setAmount(amount);
        return this;
    }

    public LeaveBalance leaveAmountType(LeaveAmountType leaveAmountType) {
        this.setLeaveAmountType(leaveAmountType);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LeaveBalance employee(Employee employee) {
        this.setEmployee(employee);
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
        if (!(o instanceof LeaveBalance)) {
            return false;
        }
        return id != null && id.equals(((LeaveBalance) o).id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeaveBalance{" +
            "id=" + getId() +
            ", leaveType='" + getLeaveType() + "'" +
            ", openingBalance=" + getOpeningBalance() +
            ", closingBalance=" + getClosingBalance() +
            ", consumedDuringYear=" + getConsumedDuringYear() +
            ", year=" + getYear() +
            ", amount=" + getAmount() +
            ", leaveAmountType='" + getLeaveAmountType() + "'" +
            "}";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveType getLeaveType() {
        return this.leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Integer getOpeningBalance() {
        return this.openingBalance;
    }

    public void setOpeningBalance(Integer openingBalance) {
        this.openingBalance = openingBalance;
    }

    public Integer getClosingBalance() {
        return this.closingBalance;
    }

    public void setClosingBalance(Integer closingBalance) {
        this.closingBalance = closingBalance;
    }

    public Integer getConsumedDuringYear() {
        return this.consumedDuringYear;
    }

    public void setConsumedDuringYear(Integer consumedDuringYear) {
        this.consumedDuringYear = consumedDuringYear;
    }

    public Integer getYear() {
        return this.year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getAmount() {
        return this.amount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LeaveAmountType getLeaveAmountType() {
        return this.leaveAmountType;
    }

    public void setLeaveAmountType(LeaveAmountType leaveAmountType) {
        this.leaveAmountType = leaveAmountType;
    }
}
