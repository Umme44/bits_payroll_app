package com.bits.hr.service.impl;

import com.bits.hr.domain.Employee;
import com.bits.hr.repository.EmployeeRepository;
import com.bits.hr.service.FileService;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import liquibase.util.file.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final File baseFolder;
    private String folderPath;

    @Value("${spring.application.employee-picture-server}")
    private String serverUrl;

    public FileServiceImpl(
        @Value("${spring.application.base-path}") String folderPath,
        @Value("${user.home}") String homePath,
        EmployeeRepository employeeRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.folderPath = homePath + folderPath + "/employee-profile-pictures/";
        try {
            this.baseFolder = new File(this.folderPath);
            log.debug("Saving files to: " + baseFolder);
            if (!baseFolder.exists()) {
                Files.createDirectories(Paths.get(this.folderPath));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    @Override
    public String save(MultipartFile file, String employeePin) {
        String extension = getFileExtension(file);
        String fileName = "photo_" + employeePin + "." + extension;
        try {
            byte[] bytes = file.getBytes();
            File imageFile = new File(baseFolder.getPath() + "/" + fileName);
            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile));
            outputStream.write(bytes);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
        return fileName;
    }

    @Override
    public Path load(String fileName) {
        String fileAbsolutePath = folderPath + fileName;
        try {
            return Paths.get(fileAbsolutePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(String fileName) {
        File file = new File(folderPath + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    private String fetchAndSaveImage(Employee employee) throws IOException {
        URL url = new URL(serverUrl + employee.getPin() + ".jpg");
        log.debug("path: " + url.getPath());
        BufferedImage img = ImageIO.read(url);
        String fileName = "photo_" + employee.getPin() + ".jpg";
        File file = new File(folderPath + fileName);
        ImageIO.write(img, "jpg", file);
        return fileName;
    }

    @Override
    public int syncImages(boolean force) {
        int synced = 0;
        List<Employee> allEmployees = employeeRepository.findAll();
        String fileName;
        for (Employee employee : allEmployees) {
            if (force || employee.getPicture() == null) {
                try {
                    //To prevent delete employee image,after force sync
                    /* if (employee.getPicture() != null) {
                        delete(employee.getPicture());
                    }*/

                    // If employee has no picture then fetch image from Rongdhonu url & save
                    if (employee.getPicture() == null) {
                        fileName = fetchAndSaveImage(employee);
                        employee.setPicture(fileName);
                    }
                    //String fileName = fetchAndSaveImage(employee);
                    employee.setUpdatedAt(LocalDateTime.now());
                    employeeRepository.save(employee);
                    synced++;
                } catch (IOException e) {
                    log.error("Could not sync image for employee " + employee.getPin());
                    log.error(e.getLocalizedMessage());
                }
            }
        }

        return synced;
    }

    @Override
    public boolean syncEmployeeImage(String pin) {
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByPin(pin);
        if (!employeeOptional.isPresent()) return false;
        Employee employee = employeeOptional.get();

        try {
            if (employee.getPicture() != null) {
                delete(employee.getPicture());
            }
            String fileName = fetchAndSaveImage(employee);
            employee.setPicture(fileName);
            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);
        } catch (IOException e) {
            log.warn(e.getLocalizedMessage());
            return false;
        }

        return true;
    }

    public String getFileExtension(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        return StringUtils.getFilenameExtension(originalFileName);
    }
}
