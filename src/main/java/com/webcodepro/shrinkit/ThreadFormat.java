package com.webcodepro.shrinkit;

/**
 * Define and decode the thread_format field.
 * @author robgreene@users.sourceforge.net
 */
public enum ThreadFormat {
	UNCOMPRESSED(0x0000, "Uncompressed"), 
	HUFFMAN_SQUEEZE(0x0001, "Huffman Squeeze"), 
	DYNAMIC_LZW1(0x0002, "Dynamic LZW/1"), 
	DYNAMIC_LZW2(0x0003, "Dynamic LZW/2"), 
	UNIX_12BIT_COMPRESS(0x0004, "Unix 12-bit Compress"), 
	UNIX_16BIT_COMPRESS(0x0005, "Unix 16-bit Compress");
	
	/** Associate the hex codes with the enum */
	private final int threadFormat;
	private final String name;
	
	private ThreadFormat(int threadFormat, String name) {
		this.threadFormat = threadFormat;
		this.name = name;
	}
	
	public int getThreadFormat() {
		return threadFormat;
	}
	public String getName() {
		return name;
	}

	/**
	 * Find the ThreadFormat.
	 * @throws IllegalArgumentException if the thread_format is unknown
	 */
	public static ThreadFormat find(int threadFormat) {
		for (ThreadFormat f : values()) {
			if (threadFormat == f.getThreadFormat()) return f;
		}
		throw new IllegalArgumentException("Unknown thread_format of " + threadFormat);
	}
}
