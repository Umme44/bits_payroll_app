package com.bits.hr.service.dto;

import java.io.Serializable;
import javax.persistence.Lob;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.bits.hr.domain.ProcReq} entity.
 */
public class ProcReqDTO implements Serializable {

    private Long id;

    @NotNull
    @DecimalMin(value = "1")
    private Double quantity;

    @Lob
    private String referenceFilePath;

    @Lob
    private byte[] referenceFileData;

    private String referenceFileDataContentType;

    private Long itemInformationId;

    private String itemInformationName;
    private String itemInformationCode;

    @Lob
    private String itemInformationSpecification;

    private String unitOfMeasurementName;

    private Long procReqMasterId;

    private String procReqMasterRequisitionNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getReferenceFilePath() {
        return referenceFilePath;
    }

    public void setReferenceFilePath(String referenceFilePath) {
        this.referenceFilePath = referenceFilePath;
    }

    public byte[] getReferenceFileData() {
        return referenceFileData;
    }

    public void setReferenceFileData(byte[] referenceFileData) {
        this.referenceFileData = referenceFileData;
    }

    public String getReferenceFileDataContentType() {
        return referenceFileDataContentType;
    }

    public void setReferenceFileDataContentType(String referenceFileDataContentType) {
        this.referenceFileDataContentType = referenceFileDataContentType;
    }

    public Long getItemInformationId() {
        return itemInformationId;
    }

    public void setItemInformationId(Long itemInformationId) {
        this.itemInformationId = itemInformationId;
    }

    public String getItemInformationName() {
        return itemInformationName;
    }

    public void setItemInformationName(String itemInformationName) {
        this.itemInformationName = itemInformationName;
    }

    public String getItemInformationCode() {
        return itemInformationCode;
    }

    public void setItemInformationCode(String itemInformationCode) {
        this.itemInformationCode = itemInformationCode;
    }

    public String getItemInformationSpecification() {
        return itemInformationSpecification;
    }

    public void setItemInformationSpecification(String itemInformationSpecification) {
        this.itemInformationSpecification = itemInformationSpecification;
    }

    public String getUnitOfMeasurementName() {
        return unitOfMeasurementName;
    }

    public void setUnitOfMeasurementName(String unitOfMeasurementName) {
        this.unitOfMeasurementName = unitOfMeasurementName;
    }

    public Long getProcReqMasterId() {
        return procReqMasterId;
    }

    public void setProcReqMasterId(Long procReqMasterId) {
        this.procReqMasterId = procReqMasterId;
    }

    public String getProcReqMasterRequisitionNo() {
        return procReqMasterRequisitionNo;
    }

    public void setProcReqMasterRequisitionNo(String procReqMasterRequisitionNo) {
        this.procReqMasterRequisitionNo = procReqMasterRequisitionNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProcReqDTO)) {
            return false;
        }

        return id != null && id.equals(((ProcReqDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProcReqDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", referenceFilePath='" + getReferenceFilePath() + "'" +
            ", referenceFileData='" + getReferenceFileData() + "'" +
            ", itemInformationId=" + getItemInformationId() +
            ", itemInformationName='" + getItemInformationName() + "'" +
            ", procReqMasterId=" + getProcReqMasterId() +
            ", procReqMasterRequisitionNo='" + getProcReqMasterRequisitionNo() + "'" +
            "}";
    }
}
