package com.bits.hr.service.dto;

import java.util.List;

public class EmployeeIdCardSummaryDTO {

    private int totalUploaded;
    private int missing;
    private List<EmployeeMinimalDTO> uploadedEmployeeList;
    private List<EmployeeMinimalDTO> missingEmployeeList;

    public int getTotalUploaded() {
        return totalUploaded;
    }

    public void setTotalUploaded(int totalUploaded) {
        this.totalUploaded = totalUploaded;
    }

    public int getMissing() {
        return missing;
    }

    public void setMissing(int missing) {
        this.missing = missing;
    }

    public List<EmployeeMinimalDTO> getUploadedEmployeeList() {
        return uploadedEmployeeList;
    }

    public void setUploadedEmployeeList(List<EmployeeMinimalDTO> uploadedEmployeeList) {
        this.uploadedEmployeeList = uploadedEmployeeList;
    }

    public List<EmployeeMinimalDTO> getMissingEmployeeList() {
        return missingEmployeeList;
    }

    public void setMissingEmployeeList(List<EmployeeMinimalDTO> missingEmployeeList) {
        this.missingEmployeeList = missingEmployeeList;
    }
}
