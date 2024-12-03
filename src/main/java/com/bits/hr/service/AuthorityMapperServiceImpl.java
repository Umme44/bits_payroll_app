package com.bits.hr.service;

import com.bits.hr.config.YamlConfig;
import com.bits.hr.domain.Authority;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@SuppressWarnings("unchecked")
@Service
public class AuthorityMapperServiceImpl implements AuthorityMapperService {

    private static final String AUTHORITIES_FILE_PATH = "config/authorities.yml";
    private final Map<String, List<String>> authoritiesMap;

    public AuthorityMapperServiceImpl() {
        Map<String, List<Object>> map = YamlConfig.read(AUTHORITIES_FILE_PATH, Map.class);
        authoritiesMap = new HashMap<>();
        for (Map.Entry<String, List<Object>> entry : map.entrySet()) {
            List<Object> values = entry.getValue();
            if (!authoritiesMap.containsKey(entry.getKey())) {
                authoritiesMap.put(entry.getKey(), new ArrayList<>());
            }
            if (values != null) {
                for (Object value : values) {
                    authoritiesMap.get(entry.getKey()).add(String.valueOf(value));
                }
            }
        }
    }

    @Override
    public List<String> getAuthorities(Set<Authority> roles) {
        Set<String> rolesStr = roles.stream().map(Authority::getName).collect(Collectors.toSet());
        return getAuthorityForRoles(rolesStr);
    }

    @Override
    public List<String> getAuthorityForRoles(Set<String> roles) {
        List<String> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(role);
            authorities.addAll(Optional.ofNullable(authoritiesMap).map(map -> map.get(role)).orElseGet(ArrayList::new));
        }
        return authorities;
    }

    @Override
    public List<String> getRoleForAuthority(String authority) {
        List<String> roles = new ArrayList<>();
        for (String role : authoritiesMap.keySet()) {
            List<String> authorities = authoritiesMap.get(role);
            if (authorities != null && authorities.contains(authority)) {
                roles.add(role);
            }
        }
        return roles;
    }
}
