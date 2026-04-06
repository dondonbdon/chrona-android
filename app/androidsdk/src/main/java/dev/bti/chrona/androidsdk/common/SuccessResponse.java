package dev.bti.chrona.androidsdk.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SuccessResponse<T> extends Response {
    private T payload;
}
