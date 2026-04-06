package dev.bti.chrona.androidsdk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthDto {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String username;
    private String avatarUrl;

}