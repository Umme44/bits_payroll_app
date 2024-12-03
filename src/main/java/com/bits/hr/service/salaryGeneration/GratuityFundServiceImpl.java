package com.bits.hr.service.salaryGeneration;

import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Service;

@Service
public class GratuityFundServiceImpl implements GratuityFundService {

    // GF= numberOfServiceYear * CurrentPayableBasic * 1.5

    @Override
    public double calculateGratuityFund(LocalDate dateOfJoining, LocalDate calculationDate, Double CurrentMainGrossBasic) {
        int serviceYear = CalculateServiceYear(dateOfJoining, calculationDate);
        double gratuity = 1.5 * serviceYear;

        if (gratuity > 15) {
            return 15 * CurrentMainGrossBasic;
        } else {
            return gratuity * CurrentMainGrossBasic;
        }
    }

    @Override
    public double getProvisionForGratuityFundPerMonth(LocalDate dateOfJoining, LocalDate calculationDate, Double currentMainGrossBasic) {
        int serviceYear = CalculateServiceYear(dateOfJoining, calculationDate);
        double gratuity = 1.5 * serviceYear;

        if (gratuity > 15) {
            //return 15 * CurrentMainGrossBasic;
            return 0;
        } else {
            return (1.5d * currentMainGrossBasic) / 12;
        }
    }

    @Override
    public int CalculateServiceYear(LocalDate dateOfJoining, LocalDate calculationDate) {
        /*For Every 6 month of service , employee will gain 1 service year
         * for 3 month of service , there will be 0 service year
         * for 9 month of service , there will be 1 service year
         * for 16 month of service , there will be 1 service year
         * ( 16 month = 1 yr 4 month , 1 year=1 service year  4 month = 0 service year
         */

        //calculate months
        Period difference = Period.between(dateOfJoining, calculationDate);
        int serviceYear = difference.getYears();
        int serviceMonth = difference.getMonths() % 12;
        if (serviceMonth >= 6) {
            serviceYear = serviceYear + 1;
        }
        return serviceYear;
    }
}
