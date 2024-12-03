package com.bits.hr.web.rest.importXL;

import com.bits.hr.service.importXL.GenericUploadServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class GenericXlsxUploadController {

    @Autowired
    private GenericUploadServiceImpl genericUploadServiceImpl;

    @PostMapping("/generic_xlsx_upload")
    // public List<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws Exception{
    public List<ArrayList<String>> upload(@RequestParam("file") MultipartFile file) throws Exception {
        List<ArrayList<String>> result = genericUploadServiceImpl.upload(file);
        return result;
    }
}
