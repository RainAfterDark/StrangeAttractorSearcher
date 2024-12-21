package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.Vector3;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StrangeQuadraticAttractor extends StrangeAttractor {
    @JsonCreator
    protected StrangeQuadraticAttractor(@JsonProperty("coefficients") float[] coefficients) {
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

        float dx = coefficients[0] * x * x + coefficients[1] * y * y + coefficients[2] * z * z
                 + coefficients[3] * x * y + coefficients[4] * x * z + coefficients[5] * y * z
                 + coefficients[6] * x + coefficients[7] * y + coefficients[8] * z
                 + coefficients[9];

        float dy = coefficients[10] * x * x + coefficients[11] * y * y + coefficients[12] * z * z
                 + coefficients[13] * x * y + coefficients[14] * x * z + coefficients[15] * y * z
                 + coefficients[16] * x + coefficients[17] * y + coefficients[18] * z
                 + coefficients[19];

        float dz = coefficients[20] * x * x + coefficients[21] * y * y + coefficients[22] * z * z
                 + coefficients[23] * x * y + coefficients[24] * x * z + coefficients[25] * y * z
                 + coefficients[26] * x + coefficients[27] * y + coefficients[28] * z
                 + coefficients[29];

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }
}
