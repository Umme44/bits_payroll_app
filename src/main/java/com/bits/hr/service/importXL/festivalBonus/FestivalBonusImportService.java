package com.bits.hr.service.importXL.festivalBonus;

import com.bits.hr.domain.Employee;
import com.bits.hr.domain.Festival;
import com.bits.hr.domain.FestivalBonusDetails;
import com.bits.hr.domain.enumeration.Religion;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.repository.FestivalBonusDetailsRepository;
import com.bits.hr.repository.FestivalRepository;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.DateUtil;
import com.bits.hr.util.PinUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
@Transactional
public class FestivalBonusImportService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FestivalRepository festivalRepository;

    @Autowired
    private FestivalBonusDetailsRepository festivalBonusDetailsRepository;

    @Autowired
    private GenericUploadService genericUploadService;

    public boolean importFile(MultipartFile file) {
        try {
            List<ArrayList<String>> data = genericUploadService.upload(file);

            //  0. Title ( must be unique )	     festival Bonus For eid -ul-fitr 2020
            //  1. Festival Name	             Eid-Ul-fitr
            //  2. Festival Date	             1-Jan-22
            //  3. Bonus Disbursement Date	     1-Jan-22
            //  4. Religion	                     ALL
            //  5. Type	                         General
            //  6. Blank
            //  7. Employee PIN	    Name	    Gross	Basic	BonusAmount	    Remarks	    isHold
            //  8.      0            1            2       3          4             5           6

            /// save section
            /// maintain duplicate in festival
            /// delete previous entries in festival_bonus_details if Festival exist
            Festival festival = createFestival(data);

            // Festival Bonus Details starts from row 8
            List<FestivalBonusDetails> festivalBonusDetailsList = new ArrayList<>();
            for (int i = 8; i < data.size(); i++) {
                if (data.get(i).get(0).equals("0")) {
                    continue;
                }
                Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(PinUtil.formatPin(data.get(i).get(0).trim()));
                if (!employeeOptional.isPresent()) {
                    continue;
                }
                FestivalBonusDetails festivalBonusDetail = createFestivalBonusDetails(data.get(i), employeeOptional.get(), festival);
                festivalBonusDetailsList.add(festivalBonusDetail);
            }

            boolean result = save(festival, festivalBonusDetailsList);

            if (result) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    boolean save(Festival festival, List<FestivalBonusDetails> festivalBonusDetails) {
        try {
            // Save Festival
            Optional<Festival> existingOne = festivalRepository.findDuplicateFestivalByTitleName(festival.getTitle());
            if (existingOne.isPresent()) {
                deletePreviousFestivalBonusDetails(existingOne.get().getId());
                festival.setId(existingOne.get().getId());
            }
            Festival savedFestival = festivalRepository.save(festival);

            // Save festival bonus details
            for (int i = 0; i < festivalBonusDetails.size(); i++) {
                List<FestivalBonusDetails> existingBonusDetail = festivalBonusDetailsRepository.getDuplicates(
                    festivalBonusDetails.get(i).getEmployee().getId(),
                    savedFestival.getId()
                );

                if (existingBonusDetail.size() > 0) {
                    festivalBonusDetails.get(i).setId(existingBonusDetail.get(0).getId());
                    festivalBonusDetails.get(i).setFestival(savedFestival);
                } else {
                    festivalBonusDetails.get(i).setFestival(savedFestival);
                }
                festivalBonusDetailsRepository.save(festivalBonusDetails.get(i));
            }
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    Festival createFestival(List<ArrayList<String>> data) {
        //  0. Title ( must be unique )	     festival Bonus For eid -ul-fitr 2020
        //  1. Festival Name	             Eid-Ul-fitr
        //  2. Festival Date	             1-Jan-22
        //  3. Bonus Disbursement Date	     1-Jan-22
        //  4. Religion	                     ALL
        //  6. Type	                         General
        Festival festival = new Festival();
        festival.setTitle(data.get(0).get(1).trim());
        festival.setFestivalName(data.get(1).get(1));
        festival.setFestivalDate(DateUtil.xlStringToDate(data.get(2).get(1).trim()));
        festival.setBonusDisbursementDate(DateUtil.xlStringToDate(data.get(3).get(1).trim()));
        festival.setReligion(getReligion(data.get(4).get(1)));
        festival.setIsProRata(isProRata(data.get(5).get(1)));

        return festival;
    }

    private FestivalBonusDetails createFestivalBonusDetails(ArrayList<String> dataList, Employee employee, Festival festival) {
        //  7. Employee PIN	    Name	    Gross	Basic	BonusAmount	    Remarks	    isHold
        //  8.      0            1            2       3          4             5           6
        FestivalBonusDetails newFestivalBonusDetails = new FestivalBonusDetails();
        newFestivalBonusDetails.setEmployee(employee);
        newFestivalBonusDetails.setGross(Double.parseDouble(dataList.get(2).trim()));
        newFestivalBonusDetails.setBasic(Double.parseDouble(dataList.get(3).trim()));
        newFestivalBonusDetails.setBonusAmount(Double.parseDouble(dataList.get(4).trim()));
        newFestivalBonusDetails.setRemarks(getRemarks(dataList.get(5)));
        newFestivalBonusDetails.setIsHold(getFestivalBonusHoldStatus(dataList.get(6)));

        return newFestivalBonusDetails;
    }

    Optional<FestivalBonusDetails> findDuplicateFestivalBonusDetails(FestivalBonusDetails festivalBonusDetails) {
        List<FestivalBonusDetails> existingOne = festivalBonusDetailsRepository.getDuplicates(
            festivalBonusDetails.getEmployee().getId(),
            festivalBonusDetails.getFestival().getId()
        );
        return Optional.of(existingOne.get(0));
    }

    void deletePreviousFestivalBonusDetails(long festivalId) {
        List<FestivalBonusDetails> festivalBonusDetails = festivalBonusDetailsRepository.findByFestivalId(festivalId);
        for (int i = 0; i < festivalBonusDetails.size(); i++) {
            festivalBonusDetailsRepository.delete(festivalBonusDetails.get(i));
        }
    }

    Optional<Festival> findDuplicateFestivalByTitleName(ArrayList<String> dataList) {
        // Argument : First row of the excel sheet
        // Title ( must be unique )	     festival Bonus For eid -ul-fitr 2020
        //            0                                    1

        String titleName = dataList.get(1).trim();
        Optional<Festival> festivalOptional = festivalRepository.findDuplicateFestivalByTitleName(titleName);
        return festivalOptional;
    }

    private Boolean getFestivalBonusHoldStatus(String s) {
        String isHold = s.trim();
        if (isHold.equalsIgnoreCase("true")) {
            return true;
        } else {
            return false;
        }
    }

    private String getRemarks(String s) {
        if (s == "") {
            return null;
        }
        if (s == "0") {
            return null;
        }
        if (s.equalsIgnoreCase("N/A")) {
            return null;
        }
        if (s == "-") {
            return null;
        }
        return s;
    }

    private Religion getReligion(String s) {
        String religion = s.trim().toUpperCase();

        switch (religion) {
            case "ALL":
                return Religion.ALL;
            case "ISLAM":
                return Religion.ISLAM;
            case "HINDU":
                return Religion.HINDU;
            case "BUDDHA":
                return Religion.BUDDHA;
            case "CHRISTIAN":
                return Religion.CHRISTIAN;
            default:
                return Religion.OTHER;
        }
    }

    private Boolean isProRata(String s) {
        String festivalType = s.trim().toLowerCase();

        if (festivalType.equalsIgnoreCase("General")) {
            return false;
        } else {
            return true;
        }
    }
}
