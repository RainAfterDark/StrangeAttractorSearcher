package io.github.rainafterdark.strangeattractorsearcher.Data;

import com.badlogic.gdx.math.Vector3;
import imgui.type.ImBoolean;
import lombok.Data;

@Data
public class DebugSingleton {
    private static DebugSingleton instance;

    private int calculations = 0;
    private int lineSegments = 0;
    private Vector3 autoCenterPoint = new Vector3();
    private float autoZoom = 0f;
    private ImBoolean drawAxes = new ImBoolean(false);

    private DebugSingleton() {
    }

    public static DebugSingleton getInstance() {
        if (instance == null) {
            instance = new DebugSingleton();
        }
        return instance;
    }
}
