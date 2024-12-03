package com.bits.hr.service.importXL.pf.helperMethods.dataConverter;

import static com.bits.hr.service.importXL.employee.rowConstants.EmployeeLegacyImportRowConstant.LRC_Doj;

import com.bits.hr.service.importXL.employee.helperMethods.dataConverter.DoubleToDate;
import java.time.LocalDate;

public class StringToDate {

    public static LocalDate convert(String str) {
        LocalDate doj = null;
        if (!str.equals("0")) {
            doj = DoubleToDate.convert(Double.parseDouble(str));
        }
        return null;
    }
}
