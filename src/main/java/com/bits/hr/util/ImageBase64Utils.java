package com.bits.hr.util;

import org.springframework.util.StringUtils;

public class ImageBase64Utils {

    public static final String JPEG_OR_JPG_BASE64_DATA_TYPE = "data:image/jpg;base64";
    public static final String PNG_BASE64_DATA_TYPE = "data:image/png;base64";
    public static final String SVG_BASE64_DATA_TYPE = "data:image/svg+xml;base64";

    public static final String SEPARATOR = ",";

    /**
     * This util method return a base64 version for file preview in Angular
     * (example - <img id="img" src="data:image/svg+xml;base64,PHN2ZyBpZD0iREVWSUNFX01BUF8tX09wZW5lZF9jb3B5IiBkYXRhLW5..." /> )
     *
     * @param filePath
     * @return
     */
    public static String getImageBase64Prefix(String filePath) {
        String extension = StringUtils.getFilenameExtension(filePath);
        return getImageBase64PrefixByExtension(extension);
    }

    public static String getImageBase64PrefixByExtension(String extension) {
        if (extension.equals("png")) {
            return PNG_BASE64_DATA_TYPE + SEPARATOR;
        } else if (extension.equals("jpg") || extension.equals("jpeg")) {
            return JPEG_OR_JPG_BASE64_DATA_TYPE + SEPARATOR;
        } else if (extension.equals("svg")) {
            return SVG_BASE64_DATA_TYPE + SEPARATOR;
        } else {
            throw new RuntimeException("File Type is unknown");
        }
    }
}
