package dev.bti.chrona.androidsdk.dto;

import dev.bti.chrona.androidsdk.constants.CredentialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    private String credential;
    private String password;
    private CredentialType credentialType;
}