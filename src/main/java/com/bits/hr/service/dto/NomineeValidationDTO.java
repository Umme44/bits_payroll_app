package com.bits.hr.service.dto;

/**
 * A DTO for pre validation the {@link com.bits.hr.domain.Nominee} & {@link com.bits.hr.domain.PfNominee} entity.
 */
import java.io.Serializable;
import java.util.Objects;

public class NomineeValidationDTO implements Serializable {

    private boolean isNidVerificationRequired;
    private boolean isNidVerified;
    private boolean isGuardianNidVerificationRequired;
    private boolean isGuardianNidVerified;
    private double remainingSharePercentage;
    private boolean doesSharePercentageExceed;

    public NomineeValidationDTO() {}

    public boolean getIsNidVerificationRequired() {
        return isNidVerificationRequired;
    }

    public void setNidVerificationRequired(boolean nidVerificationRequired) {
        isNidVerificationRequired = nidVerificationRequired;
    }

    public boolean getIsNidVerified() {
        return isNidVerified;
    }

    public void setNidVerified(boolean nidVerified) {
        isNidVerified = nidVerified;
    }

    public boolean getIsGuardianNidVerificationRequired() {
        return isGuardianNidVerificationRequired;
    }

    public void setGuardianNidVerificationRequired(boolean guardianNidVerificationRequired) {
        isGuardianNidVerificationRequired = guardianNidVerificationRequired;
    }

    public boolean getIsGuardianNidVerified() {
        return isGuardianNidVerified;
    }

    public void setGuardianNidVerified(boolean guardianNidVerified) {
        isGuardianNidVerified = guardianNidVerified;
    }

    public double getRemainingSharePercentage() {
        return remainingSharePercentage;
    }

    public void setRemainingSharePercentage(double remainingSharePercentage) {
        this.remainingSharePercentage = remainingSharePercentage;
    }

    public boolean getDoesSharePercentageExceed() {
        return doesSharePercentageExceed;
    }

    public void setDoesSharePercentageExceed(boolean doesSharePercentageExceed) {
        this.doesSharePercentageExceed = doesSharePercentageExceed;
    }

    @Override
    public String toString() {
        return (
            "NomineeValidationDTO{" +
            "isNidVerificationRequired=" +
            isNidVerificationRequired +
            ", isNidVerified=" +
            isNidVerified +
            ", isGuardianNidVerificationRequired=" +
            isGuardianNidVerificationRequired +
            ", isGuardianNidVerified=" +
            isGuardianNidVerified +
            ", remainingSharePercentage=" +
            remainingSharePercentage +
            ", doesSharePercentageExceed=" +
            doesSharePercentageExceed +
            '}'
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NomineeValidationDTO that = (NomineeValidationDTO) o;
        return (
            isNidVerificationRequired == that.isNidVerificationRequired &&
            isNidVerified == that.isNidVerified &&
            isGuardianNidVerificationRequired == that.isGuardianNidVerificationRequired &&
            isGuardianNidVerified == that.isGuardianNidVerified &&
            Double.compare(that.remainingSharePercentage, remainingSharePercentage) == 0 &&
            doesSharePercentageExceed == that.doesSharePercentageExceed
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            isNidVerificationRequired,
            isNidVerified,
            isGuardianNidVerificationRequired,
            isGuardianNidVerified,
            remainingSharePercentage,
            doesSharePercentageExceed
        );
    }
}
