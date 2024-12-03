package com.bits.hr.service.importXL.ams;

import com.bits.hr.domain.Holidays;
import com.bits.hr.domain.enumeration.HolidayType;
import com.bits.hr.repository.HolidaysRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class HolidaysImportService {

    @Autowired
    HolidaysRepository holidaysRepository;

    @Autowired
    GenericUploadService genericUploadService;

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

                Holidays holidays = new Holidays();

                HolidayType holidayType = getTypeFromString(dataItems.get(0));
                holidays.setHolidayType(holidayType);

                String holidayDescription = dataItems.get(1);
                holidays.setDescription(holidayDescription);

                LocalDate startDate = doubleToDate(Double.parseDouble(dataItems.get(2)));
                holidays.setStartDate(startDate);

                LocalDate endDate = doubleToDate(Double.parseDouble(dataItems.get(3)));
                holidays.setEndDate(endDate);

                boolean moonDependent = parseBooleanValue(dataItems.get(4));
                holidays.setIsMoonDependent(moonDependent);

                save(holidays);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void save(Holidays holiday) {
        //save if not same replica doesn't exist save
        // else update
        Holidays h1 = holidaysRepository
            .findDuplicate(holiday.getStartDate(), holiday.getEndDate(), holiday.getDescription(), holiday.getHolidayType())
            .orElseGet(() -> holidaysRepository.save(holiday));
        if (!h1.equals(holiday)) {
            h1.setStartDate(holiday.getStartDate());
            h1.setStartDate(holiday.getEndDate());
            h1.setDescription(holiday.getDescription());
            h1.setHolidayType(holiday.getHolidayType());
            holidaysRepository.save(h1);
        }
    }

    public HolidayType getTypeFromString(String str) {
        if (str.toLowerCase(Locale.ROOT).trim().equals("govt")) {
            return HolidayType.Govt;
        } else return HolidayType.General;
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
