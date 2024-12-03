package com.bits.hr.service.importXL.batchOperations.employmentActions;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface GenericXlsxImportService {
    List<ArrayList<String>> importXlsx(MultipartFile file) throws Exception;
}
