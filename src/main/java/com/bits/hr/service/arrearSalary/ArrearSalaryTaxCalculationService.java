package com.bits.hr.service.arrearSalary;

import com.bits.hr.service.dto.ArrearPaymentDTO;
import com.bits.hr.service.incomeTaxManagement.model.TaxQueryConfig;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ArrearSalaryTaxCalculationService {
    TrackingPoint getTrackingPoint(long employeeId, TaxQueryConfig taxQueryConfig);

    List<ArrearPaymentDTO> getArrearsForTaxCalculation(long employeeId, TaxQueryConfig taxQueryConfig);

    double calculateTaxWithArrear(long employeeId, TaxQueryConfig taxQueryConfig);
}
