package com.bits.hr.service.fileOperations;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Log4j2
public class FileOperationServiceImpl implements FileOperationService {

    @Autowired
    private SubDirectoryService subDirectoryService;

    // save byte array as file
    @Override
    public String save(String content, String pathWithExt) {
        try {
            subDirectoryService.createDirectoryIfNotExist();
            File file = new File(pathWithExt);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    // save byte array as file
    @Override
    public String save(byte[] bytes, String pathWithExt) {
        try {
            subDirectoryService.createDirectoryIfNotExist();

            File file = new File(pathWithExt);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(bytes);
            outputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    // save multipart file
    @Override
    public String save(MultipartFile file, String pathWithExt) {
        try {
            subDirectoryService.createDirectoryIfNotExist();

            byte[] bytes = file.getBytes();
            File tmpFile = new File(pathWithExt);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tmpFile));
            outputStream.write(bytes);
            outputStream.close();
            return pathWithExt;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    // load file as byte array
    @Override
    public byte[] loadAsByte(String pathWithExt) throws IOException {
        Path path = Paths.get(pathWithExt);
        byte[] data = Files.readAllBytes(path);
        return data;
    }

    // load file as file object
    @Override
    public File loadAsFile(String absolutePathWithExt) {
        try {
            File file = new File(absolutePathWithExt);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load file");
        }
    }

    @Override
    public void delete(String filePathWithExt) {
        File file = new File(filePathWithExt);
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public boolean isExist(String filePathWithExt) {
        File file = new File(filePathWithExt);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
