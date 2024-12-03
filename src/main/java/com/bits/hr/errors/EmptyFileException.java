package com.bits.hr.errors;

public class EmptyFileException extends BadRequestAlertException {

    public EmptyFileException() {
        super("No file has selected", "", "emptyFile");
    }
}
