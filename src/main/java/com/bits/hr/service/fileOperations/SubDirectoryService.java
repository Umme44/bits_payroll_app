package com.bits.hr.service.fileOperations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubDirectoryService {

    @Value("${user.home}")
    private String homePath;

    @Value("${spring.application.base-folder-path}")
    private String rootDir;

    @Value("${spring.application.sub-folder-tmp-path}")
    private String tempSubDir;

    @Value("${spring.application.sub-folder-employee-document-tmp-path}")
    private String employeeDocumentTempSubDir;

    @Value("${spring.application.sub-folder-templates-path}")
    private String templatesSubDir;

    @Value("${spring.application.sub-folder-nominee-path}")
    private String nomineeDir;

    @Value("${spring.application.sub-folder-insurance-path}")
    private String insuranceDir;

    @Value("${spring.application.sub-folder-offer-path}")
    private String offerDir;

    @Value("${spring.application.sub-folder-id-card-path}")
    private String idCardDir;

    @Value("${spring.application.sub-folder-tax-acknowledgement-receipt-path}")
    private String taxAcknowledgementReceiptDir;

    @Value("${spring.application.sub-folder-organization-path}")
    private String organizationDir;

    @Value("${spring.application.sub-folder-procurement-path}")
    private String procurementDir;

    @Value("${spring.application.sub-folder-employee-document-path}")
    private String employeeDocumentDir;
    @Value("${spring.application.sub-folder-employee-document-tmp-path}")
    private String employeeDocumentTmpDir;

    // to-do: do not place files on root dir
    public String getRootDir() {
        return homePath + rootDir;
    }

    public String getTempDir() {
        return tempSubDir;
    }

    public String getTemplatesDir() {
        return homePath + rootDir + templatesSubDir + "/";
    }

    public String getEmployeeDocumentTempSubDir(){ return homePath + rootDir + employeeDocumentTempSubDir + "/";}

    public String getNomineeDir() {
        return homePath + rootDir + nomineeDir + "/";
    }

    public String getInsuranceDir() {
        return homePath + rootDir + insuranceDir + "/";
    }

    public String getOfferDir() {
        return homePath + rootDir + offerDir + "/";
    }

    public String getIdCardDir() {
        return homePath + rootDir + idCardDir + "/";
    }

    public String getTaxAcknowledgementReceiptDir() {
        return homePath + rootDir + taxAcknowledgementReceiptDir + "/";
    }

    public String getOrganizationDir() {
        return homePath + rootDir + organizationDir + "/";
    }

    public String getProcurementDir() {
        return homePath + rootDir + procurementDir + "/";
    }

    public String getEmployeeDocumentDir() {
        return homePath + rootDir + employeeDocumentDir + "/";
    }
    public String getEmployeeDocumentTmpDir() {
        return homePath + rootDir + employeeDocumentTmpDir + "/";
    }

    public void setOrganizationDir(String organizationDir) {
        this.organizationDir = organizationDir;
    }

    public void createDirectoryIfNotExist() {
        final String templatesDir = getTemplatesDir();
        //        String tmpDir = getTempDir();
        String nomineeDir = getNomineeDir();
        String offerDir = getOfferDir();
        String idCardDir = getIdCardDir();
        String insuranceDir = getInsuranceDir();
        try {
            // making sure templates' directory exists
            File baseTemplatesFolder = new File(templatesDir);
            if (!baseTemplatesFolder.exists()) {
                Files.createDirectories(Paths.get(templatesDir));
            }

            //            // making sure that tmp directory exists
            //            File baseTmpFolder = new File(tmpDir);
            //            if (!baseTmpFolder.exists()) {
            //                Files.createDirectories(Paths.get(tmpDir));
            //            }

            // in this folder spring will keep data of multipart request
            File tempFolder = new File(tempSubDir);
            if (!tempFolder.exists()) {
                Files.createDirectories(Paths.get(tempSubDir));
            }

            // making sure that nominee directory exists
            File baseNomineeFolder = new File(nomineeDir);
            if (!baseNomineeFolder.exists()) {
                Files.createDirectories(Paths.get(nomineeDir));
            }

            // making sure that offer directory exists
            File baseOfferFolder = new File(offerDir);
            if (!baseOfferFolder.exists()) {
                Files.createDirectories(Paths.get(offerDir));
            }

            // making sure that ID Card directory exists
            File baseIdCardFolder = new File(idCardDir);
            if (!baseIdCardFolder.exists()) {
                Files.createDirectories(Paths.get(idCardDir));
            }

            // making sure that insurance directory exists
            File baseInsuranceFolder = new File(insuranceDir);
            if (!baseInsuranceFolder.exists()) {
                Files.createDirectories(Paths.get(insuranceDir));
            }

            // making sure that Tax Document directory exists
            File baseTaxAcknowledgementReceiptFolder = new File(getTaxAcknowledgementReceiptDir());
            if (!baseTaxAcknowledgementReceiptFolder.exists()) {
                Files.createDirectories(Paths.get(getTaxAcknowledgementReceiptDir()));
            }

            // making sure that Organization directory exists
            File baseOrganizationFolder = new File(getOrganizationDir());
            if (!baseOrganizationFolder.exists()) {
                Files.createDirectories(Paths.get(getOrganizationDir()));
            }

            // making sure that Procurement directory exists
            File baseProcurementFolder = new File(getProcurementDir());
            if (!baseProcurementFolder.exists()) {
                Files.createDirectories(Paths.get(getProcurementDir()));
            }

            // making sure that EmployeeDocument directory exists
            File baseEmployeeDocumentFolder = new File(getEmployeeDocumentDir());
            if (!baseEmployeeDocumentFolder.exists()) {
                Files.createDirectories(Paths.get(getEmployeeDocumentDir()));
            }

            // making sure that EmployeeDocumentTmp directory exists
            File baseEmployeeDocumentTmpFolder = new File(getEmployeeDocumentTmpDir());
            if (!baseEmployeeDocumentTmpFolder.exists()) {
                Files.createDirectories(Paths.get(getEmployeeDocumentTmpDir()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create folder in the desired directories!");
        }
    }
}
