package com.bits.hr.security;

import com.bits.hr.config.YamlConfig;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PermissionManagerServiceImpl implements PermissionManagerService {

    private static final String PERMISSIONS_FILE_PATH = "config/permissions.yml";

    @Override
    public List<Permission> getAllPermissions() {
        return YamlConfig.read(PERMISSIONS_FILE_PATH, PermissionList.class).getPermissions();
    }
}
