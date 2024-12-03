package com.bits.hr.service;

import java.nio.file.Path;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String save(MultipartFile file, String employeePin);

    Path load(String fileName);

    void delete(String fileName);

    int syncImages(boolean force);

    boolean syncEmployeeImage(String pin);
}
