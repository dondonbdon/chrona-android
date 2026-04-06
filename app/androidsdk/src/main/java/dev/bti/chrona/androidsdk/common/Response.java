package dev.bti.chrona.androidsdk.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Response {
    Boolean success;
    String message;
}
