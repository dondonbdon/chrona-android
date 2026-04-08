package dev.bti.chrona.androidsdk.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Config {
    public static Config INSTANCE;
    String serverAddress = "192.168.0.6:8080";

    public static Config GetInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }

        return INSTANCE;
    }
}
