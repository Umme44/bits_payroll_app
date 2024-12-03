package com.bits.hr.service.importXL.rrf;

import com.bits.hr.domain.*;
import com.bits.hr.domain.enumeration.EmployeeCategory;
import com.bits.hr.domain.enumeration.RequisitionResourceType;
import com.bits.hr.domain.enumeration.RequisitionStatus;
import com.bits.hr.repository.*;
import com.bits.hr.service.importXL.GenericUploadService;
import com.bits.hr.util.PinUtil;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RRFImportService {

    @Autowired
    private GenericUploadService genericUploadService;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private BandRepository bandRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RecruitmentRequisitionFormRepository rrfRepository;

    @Autowired
    private UnitRepository unitRepository;


    public boolean importFile(MultipartFile file) {
        try {
            List<RecruitmentRequisitionForm> rrfList = new ArrayList<>();

            List<ArrayList<String>> data = genericUploadService.upload(file);

            List<String> header1 = data.remove(0);
            List<String> header2 = data.remove(0);

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

                RecruitmentRequisitionForm rrf = new RecruitmentRequisitionForm();

                String dateOfRequisitionString = dataItems.get(1).trim();
                if (dateOfRequisitionString.equals("-")) {
                    continue; // date of requisition cant be null;
                }
                LocalDate dateOfRequisition = doubleToDate(Double.parseDouble(dataItems.get(1)));
                rrf.setDateOfRequisition(dateOfRequisition);

                String expectedJoiningDateString = dataItems.get(2).trim();
                if (expectedJoiningDateString.equals("-")) {
                    continue; // expectedJoiningDate cant be null;
                }
                LocalDate expectedJoiningDate = doubleToDate(Double.parseDouble(dataItems.get(2)));
                rrf.setExpectedJoiningDate(expectedJoiningDate);

                String numberOfVacanciesString = dataItems.get(3).trim();
                if (numberOfVacanciesString.equals("-")) {
                    continue; // number of vacancies can't be null
                }
                int numberOfVacancies = (int) Math.floor(Double.parseDouble(dataItems.get(3)));
                rrf.setNumberOfVacancies(numberOfVacancies);

                String requesterPin = PinUtil.formatPin(dataItems.get(4));
                if (!requesterPin.equals("-")) {
                    Optional<Employee> requesterPinOptional = employeeRepository.findEmployeeByPin(requesterPin);

                    // if employee is present then set following values
                    if (requesterPinOptional.isPresent()) {
                        rrf.setRequester(requesterPinOptional.get());

                        String requestedDateString = dataItems.get(5).trim();
                        if (!requestedDateString.equals("-")) {
                            LocalDate requestedDate = doubleToDate(Double.parseDouble(requestedDateString));
                            rrf.setRequestedDate(requestedDate);
                        }
                    }
                }

                String lmRecommendedPin = PinUtil.formatPin(dataItems.get(6));
                if (!lmRecommendedPin.equals("-")) {
                    Optional<Employee> lmRecommendedEmployee = employeeRepository.findEmployeeByPin(lmRecommendedPin);

                    // if employee is present then set following values
                    if (lmRecommendedEmployee.isPresent()) {
                        rrf.setRecommendedBy01(lmRecommendedEmployee.get());

                        String lmRecommendationDateString = dataItems.get(7).trim();
                        if (!lmRecommendationDateString.equals("-")) {
                            LocalDate lmRecommendationDate = doubleToDate(Double.parseDouble(dataItems.get(7)));
                            rrf.setRecommendationDate01(lmRecommendationDate);
                        }
                    }
                }

                String ctoRecommendedPin = PinUtil.formatPin(dataItems.get(8));
                if (!ctoRecommendedPin.equals("-")) {
                    Optional<Employee> ctoRecommendedEmployee = employeeRepository.findEmployeeByPin(ctoRecommendedPin);

                    if (ctoRecommendedEmployee.isPresent()) {
                        rrf.setRecommendedBy02(ctoRecommendedEmployee.get());

                        String ctoRecommendationDateString = dataItems.get(9).trim();
                        if (!ctoRecommendationDateString.equals("-")) {
                            LocalDate ctoRecommendationDate = doubleToDate(Double.parseDouble(dataItems.get(9)));
                            rrf.setRecommendationDate02(ctoRecommendationDate);
                        }
                    }
                }

                String cooRecommendedPin = PinUtil.formatPin(dataItems.get(10));
                if (!cooRecommendedPin.equals("-")) {
                    Optional<Employee> cooRecommendedEmployee = employeeRepository.findEmployeeByPin(cooRecommendedPin);

                    if (cooRecommendedEmployee.isPresent()) {
                        rrf.setRecommendedBy03(cooRecommendedEmployee.get());

                        String cooRecommendationDateString = dataItems.get(11).trim();
                        if (!cooRecommendationDateString.equals("-")) {
                            LocalDate cooRecommendationDate = doubleToDate(Double.parseDouble(dataItems.get(11)));
                            rrf.setRecommendationDate03(cooRecommendationDate);
                        }
                    }
                }

                String ceoRecommendedPin = PinUtil.formatPin(dataItems.get(12));
                if (!ceoRecommendedPin.equals("-")) {
                    Optional<Employee> ceoRecommendedEmployee = employeeRepository.findEmployeeByPin(ceoRecommendedPin);

                    if (ceoRecommendedEmployee.isPresent()) {
                        rrf.setRecommendedBy04(ceoRecommendedEmployee.get());

                        String ceoRecommendationDateString = dataItems.get(13).trim();
                        if (!ceoRecommendationDateString.equals("-")) {
                            LocalDate ceoRecommendationDate = doubleToDate(Double.parseDouble(dataItems.get(13)));
                            rrf.setRecommendationDate04(ceoRecommendationDate);
                        }
                    }
                }

                Designation jobTitle = getDesignation(dataItems.get(14));
                rrf.setFunctionalDesignation(jobTitle);

                Department department = getDepartment(dataItems.get(15));
                rrf.setDepartment(department);

                Unit unit = getUnit(dataItems.get(16));
                rrf.setUnit(unit);

                Band band = getBand(PinUtil.formatPin(dataItems.get(17)));
                rrf.setBand(band);

                String rrfNumber = dataItems.get(18).trim();
                rrf.setRrfNumber(rrfNumber);

                EmployeeCategory employmentType = getEmploymentType(dataItems.get(19));
                rrf.setEmploymentType(employmentType);

                String preferredEducationSkills = dataItems.get(20).trim().equals("-") ? null : dataItems.get(20).trim();
                rrf.setPreferredEducationType(preferredEducationSkills);

                String project = dataItems.get(21).trim().equals("-") ? null : dataItems.get(21).trim();
                rrf.setProject(project);

                RequisitionStatus requisitionStatus = getRequisitionStatus(dataItems.get(22).trim());
                rrf.setRequisitionStatus(requisitionStatus);

                RequisitionResourceType resourceType = getResourceType(dataItems.get(23).trim());
                rrf.setResourceType(resourceType);

                rrfList.add(rrf);
            }

            for (RecruitmentRequisitionForm rrf : rrfList) {
                save(rrf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    boolean save(RecruitmentRequisitionForm rrf) {
        try {
            List<RecruitmentRequisitionForm> duplicateRRFList
                = rrfRepository
                .findByRrfNumber(rrf.getRrfNumber());
            if (duplicateRRFList.size() > 0) {
                rrf.setId(duplicateRRFList.get(0).getId());
                rrfRepository.save(rrf);
            } else {
                rrfRepository.save(rrf);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    LocalDate doubleToDate(Double d) {
        Date javaDate = DateUtil.getJavaDate(d);
        LocalDate date = javaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return date;
    }

    Designation getDesignation(String designationName) {
        String designation = designationName.trim();
        Optional<Designation> optionalDesignation = designationRepository.findDesignationByDesignationName(designation);

        if (optionalDesignation.isPresent()) {
            return optionalDesignation.get();
        } else {
            return designationRepository.save(new Designation().designationName(designation));
        }
    }

    Unit getUnit(String unitName) {
        unitName = unitName.trim();
        Optional<Unit> unitOptional = unitRepository.findUnitByUnitNameIgnoreCase(unitName);

        if (unitOptional.isPresent()) {
            return unitOptional.get();
        } else {
            return unitRepository.save(new Unit().unitName(unitName));
        }
    }

    Department getDepartment(String departmentName) {
        String department = departmentName.trim();

        Optional<Department> optionalDepartment =
            departmentRepository.findDepartmentByDepartmentNameIgnoreCase(department);

        if (optionalDepartment.isPresent()) {
            return optionalDepartment.get();
        } else {
            return departmentRepository.save(new Department().departmentName(department));
        }
    }

    Band getBand(String bandName) {
        bandName = bandName.trim();

        Optional<Band> bandOptional = bandRepository.findByBandName(bandName);

        if (bandOptional.isPresent()) {
            return bandOptional.get();
        } else {
            Band band = new Band();
            band.setBandName(bandName);
            band.setCreatedAt(LocalDate.now());
            band.setMaxSalary(0d);
            band.setMinSalary(0d);
            band.setMobileCelling(0d);
            band.setWelfareFund(0d);
            return bandRepository.save(band);
        }
    }

    EmployeeCategory getEmploymentType(String employmentType) {
        String employeeCategory = employmentType.trim().toUpperCase();

        switch (employeeCategory) {
            case "REGULAR_CONFIRMED_EMPLOYEE":
                return EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
            case "REGULAR_PROVISIONAL_EMPLOYEE":
                return EmployeeCategory.REGULAR_PROVISIONAL_EMPLOYEE;
            case "CONTRACTUAL_EMPLOYEE":
                return EmployeeCategory.CONTRACTUAL_EMPLOYEE;
            case "PART_TIME":
                return EmployeeCategory.PART_TIME;
            case "CONSULTANTS":
                return EmployeeCategory.CONSULTANTS;
            default:
                return EmployeeCategory.INTERN;
        }
    }

    RequisitionStatus getRequisitionStatus(String status) {
        status = status.toUpperCase();

        switch (status) {
            case "LM_APPROVED":
                return RequisitionStatus.LM_APPROVED;
            case "HOD_APPROVED":
                return RequisitionStatus.HOD_APPROVED;
            case "CTO_APPROVED":
                return RequisitionStatus.CTO_APPROVED;
            case "HOHR_VETTED":
                return RequisitionStatus.HOHR_VETTED;
            case "CEO_APPROVED":
                return RequisitionStatus.CEO_APPROVED;
            case "OPEN":
                return RequisitionStatus.OPEN;
            case "CLOSED":
                return RequisitionStatus.CLOSED;
            case "PARTIALLY_CLOSED":
                return RequisitionStatus.PARTIALLY_CLOSED;
            case "NOT_APPROVED":
                return RequisitionStatus.NOT_APPROVED;
            default:
                return RequisitionStatus.PENDING;
        }
    }

    RequisitionResourceType getResourceType(String resourceType) {
        resourceType = resourceType.trim().toUpperCase();

        switch (resourceType) {
            case "BUDGET":
                return RequisitionResourceType.BUDGET;
            default:
                return RequisitionResourceType.NON_BUDGET;
        }
    }


}
