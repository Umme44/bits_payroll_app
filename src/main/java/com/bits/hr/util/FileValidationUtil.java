package com.bits.hr.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FileValidationUtil {

    public static boolean isFileNameAndTypeValid(MultipartFile file) {
        return isFileNameAndTypeValid(file.getOriginalFilename());
    }

    public static boolean isFileNameAndTypeValid(String pathWithExt) {
        /*
         * Filename should only contain a single dot
         * sampleFile.txt
         * File name must not contain any special characters
         * Only valid document, spreadsheet, and image formats are allowed
         * Spaces allowed
         * */
        String ext = StringUtils.getFilenameExtension(pathWithExt);
        String filename = StringUtils.getFilename(pathWithExt);
        if (filename != null && ext != null && isValidFilename(filename) && isValidFileType(ext)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidFileType(String fileExtension) {
        List<String> document = Arrays.asList("doc", "docx", "pdf", "rtf", "txt");
        List<String> spreadsheet = Arrays.asList("xls", "xlsx", "csv");
        List<String> image = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "tiff", "tif", "svg", "jfif", "jpe", "jif", "jfi", "webp");

        List<String> allowedExtensions = new ArrayList<>();
        allowedExtensions.addAll(document);
        allowedExtensions.addAll(spreadsheet);
        allowedExtensions.addAll(image);

        if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
            System.out.println("Error: Invalid file extension.");
            return false;
        }
        return true;
    }

    public static boolean isValidFilename(String filename) {
        /*
            ^: The string must start at the beginning.
            [a-zA-Z0-9]: The first character must be a letter (uppercase or lowercase) or a digit.
            [a-zA-Z0-9_. -]{0,254}:
            This part allows zero to 254 occurrences of characters that can be:
                Letters (uppercase or lowercase).
                Digits (0-9).
                Underscores (_).
                Hyphens (-).
                Spaces ( ).
                Dots (.).
            $: The string must end at this point.
            Overall, this regular expression is designed to validate a string that represents a file name with the following conditions:
            The first character must be a letter or a digit.
            The following characters (if any) can be letters, digits, underscores, hyphens, spaces, or dots.
            The total length of the string must not exceed 255 characters.
        * */
        if (!filename.matches("^[a-zA-Z0-9][a-zA-Z0-9_. -]{0,254}$")) {
            System.out.println("Error: The filename contains special characters.");
            return false;
        }
        return true;
    }

    public static boolean validateFileNamesInFileArray(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (!isFileNameAndTypeValid(file)) {
                return false;
            }
        }
        return true;
    }
}
