package com.bits.hr.service.xlExportHandling.taxAcknowledgementReceipt;

import com.bits.hr.domain.TaxAcknowledgementReceipt;
import com.bits.hr.repository.TaxAcknowledgementReceiptRepository;
import com.bits.hr.service.xlExportHandling.genericXlsxExport.dto.ExportXLPropertiesDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxAcknowledgementExportService {

    @Autowired
    TaxAcknowledgementReceiptRepository taxAcknowledgementReceiptRepository;

    public ExportXLPropertiesDTO exportTaxAcknowledgementReceiptYearWise(long aitConfigId) {
        List<TaxAcknowledgementReceipt> taxAcknowledgementReceiptList = taxAcknowledgementReceiptRepository.findByFiscalYearId(aitConfigId);

        String sheetName = "Tax Acknowledgement Receipt" + taxAcknowledgementReceiptList.get(0).getFiscalYear().toString();

        List<String> titleList = new ArrayList<>();

        List<String> subTitleList = new ArrayList<>();

        List<String> tableHeaderList = new ArrayList<>();
        tableHeaderList.add("SL");
        tableHeaderList.add("PIN");
        tableHeaderList.add("Full Name");
        tableHeaderList.add("Designation");
        tableHeaderList.add("TIN No.");
        tableHeaderList.add("Acknowledgement Receipt Number");
        tableHeaderList.add("Tax Zone");
        tableHeaderList.add("Tax Circle");
        tableHeaderList.add("Date Of Submission");

        List<List<Object>> dataList = new ArrayList<>();

        for (int i = 0; i < taxAcknowledgementReceiptList.size(); i++) {
            List<Object> data = new ArrayList<>();
            data.add(i + 1);
            data.add(taxAcknowledgementReceiptList.get(i).getEmployee().getPin());
            data.add(taxAcknowledgementReceiptList.get(i).getEmployee().getFullName());
            data.add(taxAcknowledgementReceiptList.get(i).getEmployee().getDesignation().getDesignationName());
            data.add(taxAcknowledgementReceiptList.get(i).getTinNumber());
            data.add(taxAcknowledgementReceiptList.get(i).getReceiptNumber());
            data.add(taxAcknowledgementReceiptList.get(i).getTaxesZone());
            data.add(taxAcknowledgementReceiptList.get(i).getTaxesCircle());
            data.add(taxAcknowledgementReceiptList.get(i).getDateOfSubmission());

            dataList.add(data);
        }

        ExportXLPropertiesDTO exportXLPropertiesDTO = new ExportXLPropertiesDTO();

        exportXLPropertiesDTO.setSheetName(sheetName);
        exportXLPropertiesDTO.setTitleList(titleList);
        exportXLPropertiesDTO.setTableHeaderList(tableHeaderList);
        exportXLPropertiesDTO.setTableDataListOfList(dataList);
        exportXLPropertiesDTO.setHasAutoSummation(false);
        exportXLPropertiesDTO.setAutoSizeColumnUpTo(9);

        return exportXLPropertiesDTO;
    }
}
