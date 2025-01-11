package io.github.rainafterdark.strangeattractorsearcher.Physics.Strange;

import com.badlogic.gdx.math.Vector3;

public class StrangeCubicAttractor extends StrangeAttractor {
    public StrangeCubicAttractor(float[] coefficients) {
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

        float dx = c[0] * x * x * x  // x^3
                 + c[1] * y * y * y  // y^3
                 + c[2] * z * z * z  // z^3
                 + c[3] * x * x * y  // x^2y
                 + c[4] * x * x * z  // x^2z
                 + c[5] * y * y * x  // y^2x
                 + c[6] * y * y * z  // y^2z
                 + c[7] * z * z * x  // z^2x
                 + c[8] * z * z * y  // z^2y
                 + c[9] * x * y * z  // xyz
                 + c[10] * x * x     // x^2
                 + c[11] * y * y     // y^2
                 + c[12] * z * z     // z^2
                 + c[13] * x * y     // xy
                 + c[14] * x * z     // xz
                 + c[15] * y * z     // yz
                 + c[16] * x         // x
                 + c[17] * y         // y
                 + c[18] * z         // z
                 + c[19];            // constant term

        float dy = c[20] * x * x * x  // x^3
                 + c[21] * y * y * y  // y^3
                 + c[22] * z * z * z  // z^3
                 + c[23] * x * x * y  // x^2y
                 + c[24] * x * x * z  // x^2z
                 + c[25] * y * y * x  // y^2x
                 + c[26] * y * y * z  // y^2z
                 + c[27] * z * z * x  // z^2x
                 + c[28] * z * z * y  // z^2y
                 + c[29] * x * y * z  // xyz
                 + c[30] * x * x     // x^2
                 + c[31] * y * y     // y^2
                 + c[32] * z * z     // z^2
                 + c[33] * x * y     // xy
                 + c[34] * x * z     // xz
                 + c[35] * y * z     // yz
                 + c[36] * x         // x
                 + c[37] * y         // y
                 + c[38] * z         // z
                 + c[39];            // constant term

        float dz = c[40] * x * x * x  // x^3
                 + c[41] * y * y * y  // y^3
                 + c[42] * z * z * z  // z^3
                 + c[43] * x * x * y  // x^2y
                 + c[44] * x * x * z  // x^2z
                 + c[45] * y * y * x  // y^2x
                 + c[46] * y * y * z  // y^2z
                 + c[47] * z * z * x  // z^2x
                 + c[48] * z * z * y  // z^2y
                 + c[49] * x * y * z  // xyz
                 + c[50] * x * x     // x^2
                 + c[51] * y * y     // y^2
                 + c[52] * z * z     // z^2
                 + c[53] * x * y     // xy
                 + c[54] * x * z     // xz
                 + c[55] * y * z     // yz
                 + c[56] * x         // x
                 + c[57] * y         // y
                 + c[58] * z         // z
                 + c[59];            // constant term

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }
}
