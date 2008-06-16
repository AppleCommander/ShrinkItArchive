package com.webcodepro.shrinkit;

/**
 * Define and decode the thread_format field.
 * @author robgreene@users.sourceforge.net
 */
public enum ThreadFormat {
	UNCOMPRESSED, HUFFMAN_SQUEEZE, DYNAMIC_LZW1, DYNAMIC_LZW2, 
	UNIX_12BIT_COMPRESS, UNIX_16BIT_COMPRESS;

	/**
	 * Find the ThreadFormat.
	 * @throws IllegalArgumentException if the thread_format is unknown
	 */
	public static ThreadFormat find(int threadFormat) {
		switch (threadFormat) {
		case 0x0000: return UNCOMPRESSED;
		case 0x0001: return HUFFMAN_SQUEEZE;
		case 0x0002: return DYNAMIC_LZW1;
		case 0x0003: return DYNAMIC_LZW2;
		case 0x0004: return UNIX_12BIT_COMPRESS;
		case 0x0005: return UNIX_16BIT_COMPRESS;
		default:
			throw new IllegalArgumentException("Unknown thread_format of " + threadFormat);
		}
	}
}
