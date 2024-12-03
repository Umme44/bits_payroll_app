package com.bits.hr.service.importXL.employee.helperMethods.dataConverter;

import java.time.Instant;
import java.util.Date;
import org.apache.poi.ss.usermodel.DateUtil;

public class DoubleToInstant {

    public static Instant convert(Double d) {
        Date javaDate = DateUtil.getJavaDate(d);
        //System.out.println(new SimpleDateFormat("MM/dd/yyyy").format(javaDate));
        Instant date = javaDate.toInstant();
        return date;
    }
}
