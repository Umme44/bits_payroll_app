package com.bits.hr.security;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class ClientPermissionServicePropertiesBasedImpl implements ClientPermissionService, BaseAuthService {

    private String[] predefinedClientIds;

    private String clientRoles;

    public ClientPermissionServicePropertiesBasedImpl(
        @Value(
            "${bits-payroll.external-client.access-config.client-ids:19674c62b7476a08cbb376bc733dac3f3e98648c3bb6d39387380d7ffa1dc8d4}"
        ) String[] predefinedClientIds,
        @Value("${bits-payroll.external-client.access-config.roles:EXTERNAL_API}") String clientRoles
    ) {
        this.predefinedClientIds = predefinedClientIds;
        this.clientRoles = clientRoles;
    }

    @Override
    public void authorizeClient(String clientId) {
        for (String predefinedClientId : predefinedClientIds) {
            if (predefinedClientId.equalsIgnoreCase(clientId)) {
                authorize(clientId, Stream.of(clientRoles.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
                return;
            }
        }

        throw new AuthenticationCredentialsNotFoundException("The provided client-id is not valid: " + clientId);
    }
}
