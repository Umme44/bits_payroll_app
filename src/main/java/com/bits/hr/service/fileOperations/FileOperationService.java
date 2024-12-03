package com.bits.hr.service.fileOperations;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileOperationService {
    String save(String content, String pathWithExt);
    String save(byte[] bytes, String pathWithExt);
    String save(MultipartFile file, String pathWithExt);
    byte[] loadAsByte(String pathWithExt) throws IOException;
    File loadAsFile(String absolutePathWithExt);
    void delete(String filePathWithExt);
    boolean isExist(String filePathWithExt);
}
