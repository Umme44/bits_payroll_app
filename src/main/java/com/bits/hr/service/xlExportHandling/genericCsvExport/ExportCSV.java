package com.bits.hr.service.xlExportHandling.genericCsvExport;

import com.bits.hr.service.fileOperations.FileOperationService;
import com.bits.hr.service.fileOperations.pathBuilder.PathBuilderService;
import com.bits.hr.service.fileOperations.pathBuilder.PathCategory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ExportCSV {

    @Autowired
    private PathBuilderService pathBuilderService;

    @Autowired
    private FileOperationService fileOperationService;

    /*
     * default csvFormat = CSVFormat.Default
     * */
    public Optional<File> createCSVFile(String fileName, String[] headerRow, String[][] dataTable, CSVFormat csvFormat) throws IOException {
        try {
            //create a CSV printer
            String path = pathBuilderService.buildPath(fileName + ".csv", PathCategory.TMP).get();

            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file, false);
            CSVPrinter printer = new CSVPrinter(fileWriter, csvFormat);

            //create header row
            printer.printRecord((Object) headerRow);

            // data table = many dataRow || data row = many cell
            for (String[] dataRow : dataTable) {
                printer.printRecord((Object) dataRow);
            }
            // closing the printer after the file is complete
            printer.flush();
            printer.close();

            // read fresh copy
            return Optional.of(fileOperationService.loadAsFile(path));
        } catch (Exception ex) {
            log.error(ex);
            return Optional.of(null);
        }
    }
}
