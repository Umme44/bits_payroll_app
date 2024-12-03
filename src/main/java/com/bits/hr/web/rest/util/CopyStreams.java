package com.bits.hr.web.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyStreams {

    public static void copy(InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[15192];
        int length;
        while ((length = source.read(buf)) > 0) {
            target.write(buf, 0, length);
        }
    }
}
