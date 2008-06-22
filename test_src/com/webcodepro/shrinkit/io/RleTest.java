package com.webcodepro.shrinkit.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import junit.framework.TestCase;


/**
 * Exercise the RLE encoder and decoders.  
 * 
 * @author robgreene@users.sourceforge.net
 */
public class RleTest extends TestCase {
	public void testInputStream() throws IOException {
		InputStream is = new RleInputStream(new LittleEndianByteInputStream(getPatternFileRle()));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		copy(is,os);
		byte[] expected = getPatternFileUncompressed();
		byte[] actual = os.toByteArray();
		assertEquals(expected, actual);
	}
	
	private void assertEquals(byte[] expected, byte[] actual) {
		assertEquals(expected.length, actual.length);
		for (int i=0; i<expected.length; i++) {
			assertEquals("Byte mismatch at offset " + i, expected[i], actual[i]);
		}
	}
	private void copy(InputStream is, OutputStream os) throws IOException {
		int b = is.read();
		while (b != -1) {
			os.write(b);
			b = is.read();
		}
	}
	
	private byte[] getPatternFileRle() {
		return new byte[] {
			(byte)0xdb, 0x01, (byte)0xfd, 
			(byte)0xdb, 0x02, (byte)0xfc,
			(byte)0xdb, 0x03, (byte)0xfb,
			(byte)0xdb, 0x04, (byte)0xfa,
			(byte)0xdb, 0x05, (byte)0xf9,
			(byte)0xdb, 0x06, (byte)0xf8,
			(byte)0xdb, 0x07, (byte)0xf7,
			(byte)0xdb, 0x08, (byte)0xf6,
			(byte)0xdb, 0x09, (byte)0xf5,
			(byte)0xdb, 0x0a, (byte)0xf4,
			(byte)0xdb, 0x0b, (byte)0xf3,
			(byte)0xdb, 0x0c, (byte)0xf2,
			(byte)0xdb, 0x0d, (byte)0xf1,
			(byte)0xdb, 0x0e, (byte)0xf0,
			(byte)0xdb, 0x0f, (byte)0xef,
			(byte)0xdb, 0x10, (byte)0xee,
			(byte)0xdb, 0x11, (byte)0x97
		};
	}
	private byte[] getPatternFileUncompressed() {
		byte[] data = new byte[4096];
		int value = 0x01;
		int nextCount = 0xfd;
		int count = 0xfe;
		for (int i=0; i<data.length; i++) {
			data[i] = (byte)(value & 0xff);
			count--;
			if (count == 0) {
				count = nextCount;
				nextCount--;
				value++;
			}
		}
		return data;
	}
}
