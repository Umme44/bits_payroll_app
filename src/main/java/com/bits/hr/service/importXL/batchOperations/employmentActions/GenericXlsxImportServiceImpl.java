package com.bits.hr.service.importXL.batchOperations.employmentActions;

import com.bits.hr.service.importXL.UploadUtil;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GenericXlsxImportServiceImpl implements GenericXlsxImportService {

    private final UploadUtil uploadUtil;

    public GenericXlsxImportServiceImpl(UploadUtil uploadUtil) {
        this.uploadUtil = uploadUtil;
    }

    @Override
    public List<ArrayList<String>> importXlsx(MultipartFile file) throws Exception {
        Path tempDir = Files.createTempDirectory("");

        File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();

        file.transferTo(tempFile);

        //FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));

        List<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        ArrayList<String> stringList = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(tempFile); //new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();

        int i = 1;
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();

            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();

                switch (currentCell.getCellTypeEnum()) {
                    case STRING:
                        stringList.add(currentCell.getStringCellValue().trim());
                        break;
                    case NUMERIC:
                        stringList.add(Double.toString(currentCell.getNumericCellValue()));
                        break;
                    case BOOLEAN:
                        stringList.add(Boolean.toString(currentCell.getBooleanCellValue()));
                        break;
                    case FORMULA:
                        stringList.add(currentCell.getCellFormula());
                        break;
                    case BLANK:
                        stringList.add("0");
                        break;
                    case ERROR:
                        stringList.add("0");
                        break;
                    case _NONE:
                        stringList.add("0");
                        break;
                    default:
                        stringList.add("0");
                        break;
                }
            }
            i++;

            if (i > 1) {
                if (stringList.size() > 0) {
                    data.add((ArrayList<String>) new ArrayList<>(stringList));
                }
                stringList.clear();
            }
        }

        return data;
    }
}
