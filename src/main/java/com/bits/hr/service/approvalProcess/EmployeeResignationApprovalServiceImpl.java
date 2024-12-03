package com.bits.hr.service.approvalProcess;

import com.bits.hr.domain.EmployeeResignation;
import com.bits.hr.repository.EmployeeResignationRepository;
import com.bits.hr.service.approvalProcess.helperMethods.EmployeeResignationApproval;
import com.bits.hr.service.approvalProcess.helperMethods.SaveEmployeeResignation;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
public class EmployeeResignationApprovalServiceImpl implements ApprovalProcessService {

    @Autowired
    EmployeeResignationRepository employeeResignationRepository;

    @Autowired
    SaveEmployeeResignation saveEmployeeResignation;

    @Override
    public boolean approveSelected(List<Long> selectedIds) {
        try {
            List<EmployeeResignation> employeeResignationList = employeeResignationRepository.getPendingResignationsByIds(selectedIds);
            for (EmployeeResignation employeeResignation : employeeResignationList) {
                saveEmployeeResignation.save(EmployeeResignationApproval.processToApprove(employeeResignation).get());
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }

    @Override
    public boolean approveAll() {
        try {
            List<EmployeeResignation> employeeResignationList = employeeResignationRepository.getAllPendingResignations();
            for (EmployeeResignation employeeResignation : employeeResignationList) {
                saveEmployeeResignation.save(EmployeeResignationApproval.processToApprove(employeeResignation).get());
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
        }
        return false;
    }

    @Override
    public boolean denySelected(List<Long> selectedIds) {
        try {
            List<EmployeeResignation> employeeResignationList = employeeResignationRepository.getPendingResignationsByIds(selectedIds);
            for (EmployeeResignation employeeResignation : employeeResignationList) {
                saveEmployeeResignation.save(EmployeeResignationApproval.processToReject(employeeResignation).get());
            }

            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }

    @Override
    public boolean denyAll() {
        try {
            List<EmployeeResignation> employeeResignationList = employeeResignationRepository.getAllPendingResignations();
            for (EmployeeResignation employeeResignation : employeeResignationList) {
                saveEmployeeResignation.save(EmployeeResignationApproval.processToReject(employeeResignation).get());
            }
            return true;
        } catch (Exception ex) {
            log.debug(ex);
            return false;
        }
    }
}
