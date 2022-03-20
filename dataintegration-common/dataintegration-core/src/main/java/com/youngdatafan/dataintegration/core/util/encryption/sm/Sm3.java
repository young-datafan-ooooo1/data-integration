package com.youngdatafan.dataintegration.core.util.encryption.sm;

/**
 * sm3算法实现类.
 *
 * @author gavin
 */
public class Sm3 {
    public static final byte[] IV = {0x73, (byte) 0x80, 0x16, 0x6f, 0x49,
        0x14, (byte) 0xb2, (byte) 0xb9, 0x17, 0x24, 0x42, (byte) 0xd7,
        (byte) 0xda, (byte) 0x8a, 0x06, 0x00, (byte) 0xa9, 0x6f, 0x30,
        (byte) 0xbc, (byte) 0x16, 0x31, 0x38, (byte) 0xaa, (byte) 0xe3,
        (byte) 0x8d, (byte) 0xee, 0x4d, (byte) 0xb0, (byte) 0xfb, 0x0e,
        0x4e};

    private static final int[] TJ = new int[64];

    static {
        for (int i = 0; i < 16; i++) {
            TJ[i] = 0x79cc4519;
        }

        for (int i = 16; i < 64; i++) {
            TJ[i] = 0x7a879d8a;
        }
    }

    /**
     * cf.
     *
     * @param v v.
     * @param b b.
     * @return convert data
     */
    public static byte[] cf(final byte[] v, final byte[] b) {
        int[] v1;
        int[] b1;
        v1 = convert(v);
        b1 = convert(b);
        return convert(cf(v1, b1));
    }

    /**
     * cf.
     *
     * @param v v.
     * @param z z.
     * @return convert data
     */
    public static int[] cf(final int[] v, final int[] z) {
        int ss1;
        int ss2;
        int tt1;
        int tt2;
        int a = v[0];
        int b = v[1];
        int c = v[2];
        int d = v[3];
        int e = v[4];
        int f = v[5];
        int g = v[6];
        int h = v[7];

        int[][] arr = expand(z);
        int[] w = arr[0];
        int[] w1 = arr[1];

        for (int j = 0; j < 64; j++) {
            ss1 = bitCycleLeft(a, 12) + e + bitCycleLeft(TJ[j], j);
            ss1 = bitCycleLeft(ss1, 7);
            ss2 = ss1 ^ bitCycleLeft(a, 12);
            tt1 = ffj(a, b, c, j) + d + ss2 + w1[j];
            tt2 = ggj(e, f, g, j) + h + ss1 + w[j];
            d = c;
            c = bitCycleLeft(b, 9);
            b = a;
            a = tt1;
            h = g;
            g = bitCycleLeft(f, 19);
            f = e;
            e = po(tt2);
        }

        int[] out = new int[8];
        out[0] = a ^ v[0];
        out[1] = b ^ v[1];
        out[2] = c ^ v[2];
        out[3] = d ^ v[3];
        out[4] = e ^ v[4];
        out[5] = f ^ v[5];
        out[6] = g ^ v[6];
        out[7] = h ^ v[7];

        return out;
    }

    /**
     * byte转换为Int.
     *
     * @param arr data
     * @return result
     */
    private static int[] convert(final byte[] arr) {
        int[] out = new int[arr.length / 4];
        byte[] tmp = new byte[4];
        for (int i = 0; i < arr.length; i += 4) {
            System.arraycopy(arr, i, tmp, 0, 4);
            out[i / 4] = bigEndianByteToInt(tmp);
        }
        return out;
    }

    private static byte[] convert(final int[] arr) {
        byte[] out = new byte[arr.length * 4];
        byte[] tmp;
        for (int i = 0; i < arr.length; i++) {
            tmp = bigEndianIntToByte(arr[i]);
            System.arraycopy(tmp, 0, out, i * 4, 4);
        }
        return out;
    }

    private static int[][] expand(final int[] b) {
        int[] w = new int[68];
        int[] w1 = new int[64];
        System.arraycopy(b, 0, w, 0, b.length);

        for (int i = 16; i < 68; i++) {
            w[i] = p1(w[i - 16] ^ w[i - 9] ^ bitCycleLeft(w[i - 3], 15))
                ^ bitCycleLeft(w[i - 13], 7) ^ w[i - 6];
        }

        for (int i = 0; i < 64; i++) {
            w1[i] = w[i] ^ w[i + 4];
        }

        return new int[][] {w, w1};
    }

    private static byte[] bigEndianIntToByte(final int num) {
        return back(Utils.intToBytes(num));
    }

    private static int bigEndianByteToInt(final byte[] bytes) {
        return Utils.byteToInt(back(bytes));
    }

    private static int ffj(final int x, final int y, final int z, final int j) {
        if (j >= 0 && j <= 15) {
            return ff1j(x, y, z);
        } else {
            return ff2j(x, y, z);
        }
    }

    private static int ggj(final int x, final int y, final int z, final int j) {
        if (j >= 0 && j <= 15) {
            return gg1j(x, y, z);
        } else {
            return gg2j(x, y, z);
        }
    }

    private static int ff1j(final int x, final int y, final int z) {
        return x ^ y ^ z;
    }

    private static int ff2j(final int x, final int y, final int z) {
        return (x & y) | (x & z) | (y & z);
    }

    private static int gg1j(final int x, final int y, final int z) {
        return x ^ y ^ z;
    }

    private static int gg2j(final int x, final int y, final int z) {
        return (x & y) | (~x & z);
    }

    private static int po(final int x) {
        int y = rotateLeft(x, 9);
        y = bitCycleLeft(x, 9);
        int z = rotateLeft(x, 17);
        z = bitCycleLeft(x, 17);
        return x ^ y ^ z;
    }

    private static int p1(final int x) {
        return x ^ bitCycleLeft(x, 15) ^ bitCycleLeft(x, 23);
    }

    /**
     * 对最后一个分组字节数据padding.
     *
     * @param in   in
     * @param bLen 分组个数
     * @return result
     */
    public static byte[] padding(final byte[] in, final int bLen) {
        int k = 448 - (8 * in.length + 1) % 512;
        if (k < 0) {
            k = 960 - (8 * in.length + 1) % 512;
        }
        k += 1;
        byte[] padd = new byte[k / 8];
        padd[0] = (byte) 0x80;
        final long n = in.length * 8L + bLen * 512L;
        byte[] out = new byte[in.length + k / 8 + 64 / 8];
        int pos = 0;
        System.arraycopy(in, 0, out, 0, in.length);
        pos += in.length;
        System.arraycopy(padd, 0, out, pos, padd.length);
        pos += padd.length;
        byte[] tmp = back(Utils.longToBytes(n));
        System.arraycopy(tmp, 0, out, pos, tmp.length);
        return out;
    }

    /**
     * 字节数组逆序.
     *
     * @param in in
     * @return data
     */
    private static byte[] back(final byte[] in) {
        byte[] out = new byte[in.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[out.length - i - 1];
        }

        return out;
    }

    /**
     * rotateLeft.
     *
     * @param x x
     * @param n n
     * @return data
     */
    public static int rotateLeft(final int x, final int n) {
        return (x << n) | (x >> (32 - n));
    }

    private static int bitCycleLeft(final int n, int bitLen) {
        int bitLenTmp = bitLen % 32;
        byte[] tmp = bigEndianIntToByte(n);
        int byteLen = bitLenTmp / 8;
        int len = bitLenTmp % 8;
        if (byteLen > 0) {
            tmp = byteCycleLeft(tmp, byteLen);
        }

        if (len > 0) {
            tmp = bitSmall8CycleLeft(tmp, len);
        }

        return bigEndianByteToInt(tmp);
    }

    private static byte[] bitSmall8CycleLeft(final byte[] in, final int len) {
        byte[] tmp = new byte[in.length];
        int t1;
        int t2;
        int t3;
        for (int i = 0; i < tmp.length; i++) {
            t1 = (byte) ((in[i] & 0x000000ff) << len);
            t2 = (byte) ((in[(i + 1) % tmp.length] & 0x000000ff) >> (8 - len));
            t3 = (byte) (t1 | t2);
            tmp[i] = (byte) t3;
        }

        return tmp;
    }

    private static byte[] byteCycleLeft(final byte[] in, final int byteLen) {
        byte[] tmp = new byte[in.length];
        System.arraycopy(in, byteLen, tmp, 0, in.length - byteLen);
        System.arraycopy(in, 0, tmp, in.length - byteLen, byteLen);
        return tmp;
    }
}
