package dev.bti.chrona.androidsdk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String username;
    private String email, password;
    private String firstName;
    private String lastName;

    @Builder.Default
    private String phoneNumber = "";
    @Builder.Default
    private String avatarUrl = "https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y";

    @Builder.Default
    private AddressDto address = new AddressDto();

    @Builder.Default
    private boolean receiveNotifications = true;

    @Builder.Default
    private String preferredLanguage = "EN";
    @Builder.Default
    private String preferredCurrency = "USD";

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