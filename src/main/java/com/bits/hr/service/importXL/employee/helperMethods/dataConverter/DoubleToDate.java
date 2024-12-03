package com.bits.hr.service.importXL.employee.helperMethods.dataConverter;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.apache.poi.ss.usermodel.DateUtil;

public class DoubleToDate {

    public static LocalDate convert(Double d) {
        Date javaDate = DateUtil.getJavaDate(d);
        LocalDate date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }
}
