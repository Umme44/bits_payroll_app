package com.bits.hr.service.utility;

import java.util.Map;

public interface UtilityService {
    /**
     * It will detect that, is there any xlsx file already
     * uploaded by given year and month, and all status
     * return by kay-value pair with boolean and string.
     * like, if employee-xlsx has uploaded by given year, then it will return
     * [employee:true].
     *
     * @param year  year
     * @param month month
     * @return map
     */
    Map<String, Boolean> hasUploadedXlsx(int year, int month);
}
