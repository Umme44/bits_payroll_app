package com.bits.hr.service;

import com.bits.hr.domain.Authority;
import java.util.List;
import java.util.Set;

public interface AuthorityMapperService {
    List<String> getAuthorities(Set<Authority> roles);

    List<String> getAuthorityForRoles(Set<String> roles);

    List<String> getRoleForAuthority(String authority);
}
