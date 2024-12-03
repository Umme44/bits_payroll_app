package com.bits.hr.service.importXL.pf.helperMethods;

import com.bits.hr.domain.PfAccount;
import com.bits.hr.domain.enumeration.PfAccountStatus;
import com.bits.hr.domain.enumeration.PfLoanStatus;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.PfAccountRepository;
import com.bits.hr.service.dto.EmployeeDTO;
import com.bits.hr.service.importXL.pf.helperMethods.dataConverter.StringToDate;
import com.bits.hr.service.importXL.pf.helperMethods.enumHelper.GetPfAccountStatusEnumFromString;
import com.bits.hr.service.importXL.pf.helperMethods.enumHelper.GetPfLoanStatusEnumFromString;
import com.bits.hr.service.mapper.EmployeeMapper;
import java.time.LocalDate;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PfHelperService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PfAccountRepository pfAccountRepository;

    public PfAccountStatus getPfAccountStatusEnumFromString(String str) {
        return GetPfAccountStatusEnumFromString.get(str);
    }

    public PfLoanStatus getPfLoanStatusEnumFromString(String str) {
        return GetPfLoanStatusEnumFromString.get(str);
    }

    public LocalDate stringToDate(String str) {
        return StringToDate.convert(str);
    }

    public Optional<PfAccount> crateNewPfAccountFromSysDataAndSave(String pfCode, String pin) {
        if (pfAccountRepository.getPfAccountByPinAndPfCode(pfCode, pin).isPresent()) {
            return pfAccountRepository.getPfAccountByPinAndPfCode(pfCode, pin);
        }

        try {
            EmployeeDTO employeeDTO = employeeMapper.toDto(employeeRepository.findEmployeeByPin(pin).get());
            PfAccount pfAccount = new PfAccount();
            pfAccount.setPin(pin);
            pfAccount.setPfCode(pfCode);
            pfAccount.setStatus(PfAccountStatus.ACTIVE);
            pfAccount.setMembershipStartDate(employeeDTO.getDateOfConfirmation());
            pfAccount.setAccHolderName(employeeDTO.getFullName());
            pfAccount.setDesignationName(employeeDTO.getDesignationName());
            pfAccount.setDepartmentName(employeeDTO.getDepartmentName());
            pfAccount.setUnitName(employeeDTO.getUnitName());
            pfAccount.setDateOfJoining(employeeDTO.getDateOfJoining());
            pfAccount.setDateOfConfirmation(employeeDTO.getDateOfConfirmation());

            return Optional.of(pfAccountRepository.save(pfAccount));
        } catch (Exception ex) {
            log.error("Employee does not exist !!! for the following pin:" + pin);
            return Optional.empty();
        }
    }
}
