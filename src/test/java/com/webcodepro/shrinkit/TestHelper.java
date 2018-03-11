package com.webcodepro.shrinkit;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;

import com.webcodepro.shrinkit.io.LittleEndianByteInputStream;

public class TestHelper {
	private TestHelper() {
		// Prevent construction
	}
	
	
	public static void checkDate(byte[] streamData, Date actual) throws IOException {
		try (LittleEndianByteInputStream is = new LittleEndianByteInputStream(streamData)) {
			Assert.assertEquals(is.readDate(), actual);
		}
	}
}
