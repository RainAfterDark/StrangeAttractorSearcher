package io.github.rainafterdark.strangeattractorsearcher.Physics.Strange;

import com.badlogic.gdx.math.Vector3;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StrangeQuadraticAttractor extends StrangeAttractor {
    @JsonCreator
    public StrangeQuadraticAttractor(@JsonProperty("coefficients") float[] coefficients) {
        super(coefficients);
    }

    @Override
    public Vector3 initial() {
        return new Vector3();
    }

    @Override
    public Vector3 step(Vector3 point, float deltaTime) {
        float x = point.x;
        float y = point.y;
        float z = point.z;
        float dt = 0.5f * deltaTime;
        float[] c = coefficients;

        float dx = c[0] * x * x + c[1] * y * y + c[2] * z * z
                 + c[3] * x * y + c[4] * x * z + c[5] * y * z
                 + c[6] * x + c[7] * y + c[8] * z
                 + c[9];

        float dy = c[10] * x * x + c[11] * y * y + c[12] * z * z
                 + c[13] * x * y + c[14] * x * z + c[15] * y * z
                 + c[16] * x + c[17] * y + c[18] * z
                 + c[19];

        float dz = c[20] * x * x + c[21] * y * y + c[22] * z * z
                 + c[23] * x * y + c[24] * x * z + c[25] * y * z
                 + c[26] * x + c[27] * y + c[28] * z
                 + c[29];

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }
}
