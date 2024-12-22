package io.github.rainafterdark.strangeattractorsearcher.Physics.Strange;

import com.badlogic.gdx.math.Vector3;

public class StrangeQuarticAttractor extends StrangeAttractor {
    public StrangeQuarticAttractor(float[] coefficients) {
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

        float dx = c[0] * x * x * x * x  // x^4
         + c[1] * y * y * y * y  // y^4
         + c[2] * z * z * z * z  // z^4
         + c[3] * x * x * x * y  // x^3y
         + c[4] * x * x * x * z  // x^3z
         + c[5] * y * y * y * x  // y^3x
         + c[6] * y * y * y * z  // y^3z
         + c[7] * z * z * z * x  // z^3x
         + c[8] * z * z * z * y  // z^3y
         + c[9] * x * x * y * y  // x^2y^2
         + c[10] * x * x * z * z // x^2z^2
         + c[11] * y * y * z * z // y^2z^2
         + c[12] * x * x * y * z // x^2yz
         + c[13] * y * y * x * z // y^2xz
         + c[14] * z * z * x * y // z^2xy
         + c[15] * x * x * x     // x^3
         + c[16] * y * y * y     // y^3
         + c[17] * z * z * z     // z^3
         + c[18] * x * x * y     // x^2y
         + c[19] * x * x * z     // x^2z
         + c[20] * y * y * x     // y^2x
         + c[21] * y * y * z     // y^2z
         + c[22] * z * z * x     // z^2x
         + c[23] * z * z * y     // z^2y
         + c[24] * x * y * z     // xyz
         + c[25] * x * x         // x^2
         + c[26] * y * y         // y^2
         + c[27] * z * z         // z^2
         + c[28] * x * y         // xy
         + c[29] * x * z         // xz
         + c[30] * y * z         // yz
         + c[31] * x             // x
         + c[32] * y             // y
         + c[33] * z             // z
         + c[34];                // constant term

float dy = c[35] * x * x * x * x  // x^4
         + c[36] * y * y * y * y  // y^4
         + c[37] * z * z * z * z  // z^4
         + c[38] * x * x * x * y  // x^3y
         + c[39] * x * x * x * z  // x^3z
         + c[40] * y * y * y * x  // y^3x
         + c[41] * y * y * y * z  // y^3z
         + c[42] * z * z * z * x  // z^3x
         + c[43] * z * z * z * y  // z^3y
         + c[44] * x * x * y * y  // x^2y^2
         + c[45] * x * x * z * z  // x^2z^2
         + c[46] * y * y * z * z  // y^2z^2
         + c[47] * x * x * y * z  // x^2yz
         + c[48] * y * y * x * z  // y^2xz
         + c[49] * z * z * x * y  // z^2xy
         + c[50] * x * x * x      // x^3
         + c[51] * y * y * y      // y^3
         + c[52] * z * z * z      // z^3
         + c[53] * x * x * y      // x^2y
         + c[54] * x * x * z      // x^2z
         + c[55] * y * y * x      // y^2x
         + c[56] * y * y * z      // y^2z
         + c[57] * z * z * x      // z^2x
         + c[58] * z * z * y      // z^2y
         + c[59] * x * y * z      // xyz
         + c[60] * x * x          // x^2
         + c[61] * y * y          // y^2
         + c[62] * z * z          // z^2
         + c[63] * x * y          // xy
         + c[64] * x * z          // xz
         + c[65] * y * z          // yz
         + c[66] * x              // x
         + c[67] * y              // y
         + c[68] * z              // z
         + c[69];                 // constant term

float dz = c[70] * x * x * x * x  // x^4
         + c[71] * y * y * y * y  // y^4
         + c[72] * z * z * z * z  // z^4
         + c[73] * x * x * x * y  // x^3y
         + c[74] * x * x * x * z  // x^3z
         + c[75] * y * y * y * x  // y^3x
         + c[76] * y * y * y * z  // y^3z
         + c[77] * z * z * z * x  // z^3x
         + c[78] * z * z * z * y  // z^3y
         + c[79] * x * x * y * y  // x^2y^2
         + c[80] * x * x * z * z  // x^2z^2
         + c[81] * y * y * z * z  // y^2z^2
         + c[82] * x * x * y * z  // x^2yz
         + c[83] * y * y * x * z  // y^2xz
         + c[84] * z * z * x * y  // z^2xy
         + c[85] * x * x * x      // x^3
         + c[86] * y * y * y      // y^3
         + c[87] * z * z * z      // z^3
         + c[88] * x * x * y      // x^2y
         + c[89] * x * x * z      // x^2z
         + c[90] * y * y * x      // y^2x
         + c[91] * y * y * z      // y^2z
         + c[92] * z * z * x      // z^2x
         + c[93] * z * z * y      // z^2y
         + c[94] * x * y * z      // xyz
         + c[95] * x * x          // x^2
         + c[96] * y * y          // y^2
         + c[97] * z * z          // z^2
         + c[98] * x * y          // xy
         + c[99] * x * z          // xz
         + c[100] * y * z         // yz
         + c[101] * x             // x
         + c[102] * y             // y
         + c[103] * z             // z
         + c[104];                // constant term

        Vector3 nP = point.cpy();
        nP.x += dx * dt;
        nP.y += dy * dt;
        nP.z += dz * dt;
        return nP;
    }
}
