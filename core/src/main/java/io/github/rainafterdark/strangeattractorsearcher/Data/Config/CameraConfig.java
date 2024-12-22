package io.github.rainafterdark.strangeattractorsearcher.Data.Config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CameraConfig {
    private float horizontalMovementSpeed = 0.25f;
    private float verticalMovementSpeed = 0.1f;
}
