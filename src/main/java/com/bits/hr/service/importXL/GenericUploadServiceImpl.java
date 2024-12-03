package com.bits.hr.service.importXL;

import com.bits.hr.service.fileOperations.TempFileService;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GenericUploadServiceImpl implements GenericUploadService {

    private final UploadUtil uploadUtil;
    private final TempFileService tempFileService;

    public GenericUploadServiceImpl(UploadUtil uploadUtil, TempFileService tempFileService) {
        this.uploadUtil = uploadUtil;
        this.tempFileService = tempFileService;
    }

    @Override
    public List<ArrayList<String>> upload(MultipartFile file) throws Exception {
        //        Path tempDir = Files.createTempDirectory("");
        //
        //        File fileTempDir=tempDir.toFile();
        //        if(!fileTempDir.exists()){
        //            fileTempDir.mkdirs();
        //        }
        //        File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
        //
        //        file.transferTo(tempFile);

        File tempFile = tempFileService.save(file);

        //FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));

        List<ArrayList<String>> dataTable = new ArrayList<ArrayList<String>>();
        ArrayList<String> dataRow = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(tempFile); //new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        int i = 1;
        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();

            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();

                try {
                    switch (currentCell.getCellTypeEnum()) {
                        case STRING:
                            dataRow.add(currentCell.getStringCellValue().trim());
                            break;
                        case NUMERIC:
                            dataRow.add(Double.toString(currentCell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            dataRow.add(Boolean.toString(currentCell.getBooleanCellValue()));
                            break;
                        case FORMULA:
                            //                        currentCell.getCellFormula()
                            dataRow.add(String.valueOf(evaluator.evaluateFormulaCell(currentCell)));
                            break;
                        case BLANK:
                            dataRow.add("0");
                            break;
                        case ERROR:
                            dataRow.add("0");
                            break;
                        case _NONE:
                            dataRow.add("0");
                            break;
                        default:
                            dataRow.add("0");
                            break;
                    }
                } catch (Exception e) {
                    dataRow.add("0");
                }
            }
            i++;

            if (i > 1) {
                if (!dataRow.isEmpty()) {
                    dataTable.add((ArrayList<String>) dataRow.stream().collect(Collectors.toList()));
                }
                dataRow.clear();
            }
        }

        return dataTable;
    }
}
