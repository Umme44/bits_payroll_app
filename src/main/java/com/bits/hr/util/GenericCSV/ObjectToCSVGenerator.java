package com.bits.hr.util.GenericCSV;

import com.bits.hr.service.fileOperations.TempFileService;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * written by ahad v-1.1
 * todo: Algorithm optimization.
 * todo:adding builder pattern for specifying which parameter to keep first.
 * todo:adding builder pattern so new object and mapping should not be necessary.
 **/

@Service
@Log4j2
public class ObjectToCSVGenerator {

    @Autowired
    TempFileService tempFileService;

    @SneakyThrows
    private String produceCsvData(Object[] data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (data.length == 0) {
            return "";
        }

        Class classType = data[0].getClass();
        StringBuilder builder = new StringBuilder();

        Method[] methods = classType.getDeclaredMethods();

        for (Method m : methods) {
            if (m.getParameterTypes().length == 0) {
                if (m.getName().startsWith("get")) {
                    builder.append(m.getName().substring(3)).append(',');
                } else if (m.getName().startsWith("is")) {
                    builder.append(m.getName().substring(2)).append(',');
                }
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append('\n');
        for (Object d : data) {
            for (Method m : methods) {
                if (m.getParameterTypes().length == 0) {
                    if (m.getName().startsWith("get") || m.getName().startsWith("is")) {
                        log.debug(m.invoke(d).toString());
                        builder.append(m.invoke(d).toString()).append(',');
                    }
                }
            }
            builder.append('\n');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public File generateCSV(Object[] data) throws InvocationTargetException, IllegalAccessException {
        try {
            return tempFileService.save(".csv", produceCsvData(data));
        } catch (Exception e) {
            log.debug("Error while generating csv from data. Error message : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
