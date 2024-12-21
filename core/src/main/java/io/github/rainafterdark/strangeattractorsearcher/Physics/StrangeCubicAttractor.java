package io.github.rainafterdark.strangeattractorsearcher.Physics;

import com.badlogic.gdx.math.Vector3;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StrangeCubicAttractor extends StrangeAttractor {
    @JsonCreator
    public StrangeCubicAttractor(@JsonProperty("coefficients") float[] coefficients) {
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

        float dx = coefficients[0] * x * x * x  // x^3
                 + coefficients[1] * y * y * y  // y^3
                 + coefficients[2] * z * z * z  // z^3
                 + coefficients[3] * x * x * y  // x^2y
                 + coefficients[4] * x * x * z  // x^2z
                 + coefficients[5] * y * y * x  // y^2x
                 + coefficients[6] * y * y * z  // y^2z
                 + coefficients[7] * z * z * x  // z^2x
                 + coefficients[8] * z * z * y  // z^2y
                 + coefficients[9] * x * y * z  // xyz
                 + coefficients[10] * x * x     // x^2
                 + coefficients[11] * y * y     // y^2
                 + coefficients[12] * z * z     // z^2
                 + coefficients[13] * x * y     // xy
                 + coefficients[14] * x * z     // xz
                 + coefficients[15] * y * z     // yz
                 + coefficients[16] * x         // x
                 + coefficients[17] * y         // y
                 + coefficients[18] * z         // z
                 + coefficients[19];            // constant term

        float dy = coefficients[20] * x * x * x  // x^3
                 + coefficients[21] * y * y * y  // y^3
                 + coefficients[22] * z * z * z  // z^3
                 + coefficients[23] * x * x * y  // x^2y
                 + coefficients[24] * x * x * z  // x^2z
                 + coefficients[25] * y * y * x  // y^2x
                 + coefficients[26] * y * y * z  // y^2z
                 + coefficients[27] * z * z * x  // z^2x
                 + coefficients[28] * z * z * y  // z^2y
                 + coefficients[29] * x * y * z  // xyz
                 + coefficients[30] * x * x     // x^2
                 + coefficients[31] * y * y     // y^2
                 + coefficients[32] * z * z     // z^2
                 + coefficients[33] * x * y     // xy
                 + coefficients[34] * x * z     // xz
                 + coefficients[35] * y * z     // yz
                 + coefficients[36] * x         // x
                 + coefficients[37] * y         // y
                 + coefficients[38] * z         // z
                 + coefficients[39];            // constant term

        float dz = coefficients[40] * x * x * x  // x^3
                 + coefficients[41] * y * y * y  // y^3
                 + coefficients[42] * z * z * z  // z^3
                 + coefficients[43] * x * x * y  // x^2y
                 + coefficients[44] * x * x * z  // x^2z
                 + coefficients[45] * y * y * x  // y^2x
                 + coefficients[46] * y * y * z  // y^2z
                 + coefficients[47] * z * z * x  // z^2x
                 + coefficients[48] * z * z * y  // z^2y
                 + coefficients[49] * x * y * z  // xyz
                 + coefficients[50] * x * x     // x^2
                 + coefficients[51] * y * y     // y^2
                 + coefficients[52] * z * z     // z^2
                 + coefficients[53] * x * y     // xy
                 + coefficients[54] * x * z     // xz
                 + coefficients[55] * y * z     // yz
                 + coefficients[56] * x         // x
                 + coefficients[57] * y         // y
                 + coefficients[58] * z         // z
                 + coefficients[59];            // constant term

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }
}
