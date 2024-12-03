package com.bits.hr.service.dto;

import lombok.Data;

@Data
public class FileDetailsDTO {

    private String fileNameWithExtension;
    private byte[] byteStream;
    private String base64DataType;
    private String contentType;
}
