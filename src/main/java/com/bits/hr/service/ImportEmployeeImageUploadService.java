package com.bits.hr.service;

import com.bits.hr.service.dto.EmployeeImageUploadDTO;
import com.bits.hr.service.dto.EmployeeMinimalDTO;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ImportEmployeeImageUploadService {
    Boolean imageBatchSave(MultipartFile[] files);
    void delete(String pin);
    Page<EmployeeImageUploadDTO> findAllImages(Pageable pageable, String searchText) throws IOException;
    Page<EmployeeImageUploadDTO> findAllImages(Pageable pageable) throws IOException;

    EmployeeImageUploadDTO updateImage(long id, MultipartFile file);
}
