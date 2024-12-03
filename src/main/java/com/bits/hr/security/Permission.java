package com.bits.hr.security;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Getter(AccessLevel.NONE)
    String method;

    private String url;
    private List<String> authorities;

    public String getRequestMethod() {
        return method;
    }

    @Override
    public String toString() {
        String string = "Permission{" + "url='" + url + '\'' + ", method='" + method + "', ";
        string += "authorities= ";
        if (authorities != null) {
            for (String authority : authorities) {
                string += authority + ",";
            }
        }
        string += "}";
        return string;
    }
}
