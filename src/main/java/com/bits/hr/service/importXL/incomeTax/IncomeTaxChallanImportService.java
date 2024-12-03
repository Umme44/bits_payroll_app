package com.bits.hr.service.importXL.incomeTax;

import com.bits.hr.domain.AitConfig;
import com.bits.hr.domain.IncomeTaxChallan;
import com.bits.hr.domain.enumeration.Month;
import com.bits.hr.repository.AitConfigRepository;
import com.bits.hr.repository.IncomeTaxChallanRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class IncomeTaxChallanImportService {

    @Autowired
    private IncomeTaxChallanRepository incomeTaxChallanRepository;

    @Autowired
    private AitConfigRepository aitConfigRepository;

    @Autowired
    private GenericUploadService genericUploadService;

    public boolean importFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);
            List<String> header = data.remove(0);
            for (List<String> dataItems : data) {
                if (dataItems.isEmpty()) {
                    continue;
                }
                if (dataItems.get(0).equals("0")) {
                    continue;
                }
                if (dataItems.get(0).equals("")) {
                    continue;
                }

                /**
                 0 -> SL\NO
                 1 -> Overwrite(true/false)
                 2 -> Challan No
                 3 -> Challan Date
                 4 -> Amount
                 5 -> Salary Year
                 6 -> Salary Month
                 7 -> Fiscal Year (2021-2022)
                 8 -> Remarks
                 * */
                IncomeTaxChallan incomeTaxChallan = new IncomeTaxChallan();

                boolean overwriteIfExist = parseBooleanValue(dataItems.get(1));
                String challanNo = dataItems.get(2);
                LocalDate challanDate = doubleToDate(Double.parseDouble(dataItems.get(3)));
                double amount = Double.parseDouble(dataItems.get(4));
                int salaryYear = (int) Double.parseDouble(dataItems.get(5));
                int salaryMonth = (int) Double.parseDouble(dataItems.get(6));

                LocalDate firstDayOfMonth = LocalDate.of(salaryYear, salaryMonth, 01);
                AitConfig aitConfig = aitConfigRepository.findAllBetweenOneDate(firstDayOfMonth).get(0);

                String remarks = dataItems.get(8);

                incomeTaxChallan.setChallanNo(challanNo);
                incomeTaxChallan.setChallanDate(challanDate);
                incomeTaxChallan.setAmount(amount);
                incomeTaxChallan.setYear(salaryYear);
                incomeTaxChallan.setMonth(Month.fromInteger(salaryMonth));
                incomeTaxChallan.setAitConfig(aitConfig);
                incomeTaxChallan.setRemarks(remarks);

                save(incomeTaxChallan, overwriteIfExist);
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public void save(IncomeTaxChallan incomeTaxChallan, boolean overwriteIfExist) {
        //save if not same replica doesn't exist save
        // else update

        List<IncomeTaxChallan> incomeTaxChallanList;
        incomeTaxChallanList =
            incomeTaxChallanRepository.getIncomeTaxChallanByChallanNoAndYearAndMonth(
                incomeTaxChallan.getChallanNo(),
                incomeTaxChallan.getYear(),
                incomeTaxChallan.getMonth()
            );

        if (incomeTaxChallanList.size() > 0) {
            // deleting duplicates
            incomeTaxChallanRepository.deleteAll(incomeTaxChallanList);
        }
        incomeTaxChallanRepository.save(incomeTaxChallan);
    }

    LocalDate doubleToDate(Double d) {
        Date javaDate = DateUtil.getJavaDate(d);
        LocalDate date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }

    boolean parseBooleanValue(String value) {
        if (value.toUpperCase().equals("TRUE") || value.equals("1")) return Boolean.TRUE; else if (
            value.toUpperCase().equals("FALSE") || value.equals("0")
        ) return Boolean.FALSE; else return Boolean.FALSE;
    }
}
