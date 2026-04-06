package dev.bti.chrona.androidsdk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DevicePreferences {
    private boolean isHidden;
    private boolean isMuted;
    private String localColorOverride;
}
