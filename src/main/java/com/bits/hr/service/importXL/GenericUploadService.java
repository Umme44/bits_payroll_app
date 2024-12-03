package com.bits.hr.service.importXL;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface GenericUploadService {
    List<ArrayList<String>> upload(MultipartFile file) throws Exception;
}
