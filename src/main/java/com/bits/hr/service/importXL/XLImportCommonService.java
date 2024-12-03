package com.bits.hr.service.importXL;

import java.util.List;

public class XLImportCommonService {

    public static boolean isXLRowValid(List<String> dataItems) {
        if (dataItems.isEmpty()) {
            return false;
        }
        if (dataItems.get(0).equals("0")) {
            return false;
        }
        if (dataItems.get(0).equals("")) {
            return false;
        }
        return true;
    }

    public static boolean isValidCell(String st) {
        st = st.trim();
        if (st.equals("-")) return false; else return true;
    }
}
