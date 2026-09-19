package org.applecommander.shrinkit;

/// Some utility type methods that don't appear to belong anywhere else.
public abstract class Utility {
    /// Given a byte array, and length, strip off the high bit and create a "normal" 7-bit ASCII string.
    public static String makeString(byte[] data, int length) {
        assert data.length >= length;
        for (int i=0; i<length; i++) {
            data[i] = (byte)(data[i] & 0x7f);
        }
        return new String(data, 0, length);
    }
}
