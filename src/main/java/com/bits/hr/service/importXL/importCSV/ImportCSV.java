package com.bits.hr.service.importXL.importCSV;

import com.bits.hr.service.fileOperations.TempFileService;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class ImportCSV {

    @Autowired
    private TempFileService tempFileService;

    public List<ArrayList<String>> getDataTable(MultipartFile file, String[] HEADERS) throws Exception {
        File tempFile = tempFileService.save(file);
        List<ArrayList<String>> dataTable = new ArrayList<ArrayList<String>>();
        ArrayList<String> dataRow = new ArrayList<>();

        Reader in = new FileReader(tempFile);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader().parse(in);

        for (CSVRecord record : records) {
            int headerSize = HEADERS.length;
            for (int i = 0; i < headerSize; i++) {
                String cell = "";
                try {
                    cell = record.get(i);
                } catch (Exception ex) {
                    log.error(ex);
                }
                dataRow.add(cell);
            }
            dataTable.add(dataRow);
        }
        return dataTable;
    }
}
