package dev.bti.chrona.androidsdk.dto;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String username;
    private String email;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String avatarUrl;

    private AddressDto address;

    private boolean receiveNotifications;
    private String preferredLanguage;
    private String preferredCurrency;

    private List<String> roles;
    private boolean isActive;
    private boolean isVerified;

    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastLogin;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDto {
        private String street;
        private String city;
        private String state;
        private String postalCode;
        private String country;
    }
}