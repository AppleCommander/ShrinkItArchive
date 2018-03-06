package com.webcodepro.shrinkit.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Exercise the RLE encoder and decoders.  
 * 
 * @author robgreene@users.sourceforge.net
 */
public class RleTest extends TestCaseHelper {
	/**
	 * Test the RleInputStream to verify decoding.
	 */
	public void testInputStream() throws IOException {
		InputStream is = new RleInputStream(new ByteArrayInputStream(getPatternFileRle()));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		copy(is,os);
		byte[] expected = getPatternFileUncompressed();
		byte[] actual = os.toByteArray();
		assertEquals(expected, actual);
	}
	/**
	 * Test the RleOutputStream to verify compression.
	 */
	public void testOutputStream() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RleOutputStream os = new RleOutputStream(baos);
		ByteArrayInputStream is = new ByteArrayInputStream(getPatternFileUncompressed());
		copy(is,os);
		byte[] expected = getPatternFileRle();
		byte[] actual = baos.toByteArray();
		assertEquals(expected, actual);
	}
	/**
	 * Test the RleOutputStream with the escape character to ensure it <em>always</em> is encoded.
	 * Note that a file with lots of 0xdb codes will grow.
	 */
	public void testOutputStreamEscapeCharacter() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		RleOutputStream os = new RleOutputStream(baos);
		ByteArrayInputStream is = new ByteArrayInputStream(new byte[] { (byte)0xdb, (byte)0xdb, 0x00, (byte)0xdb });
		copy(is,os);
		byte[] expected = new byte[] { (byte)0xdb, (byte)0xdb, 0x01, 0x00, (byte)0xdb, (byte)0xdb, 0x00 };
		byte[] actual = baos.toByteArray();
		assertEquals(expected, actual);
	}
	
	/**
	 * Copy the input stream to the output stream.
	 */
	private void copy(InputStream is, OutputStream os) throws IOException {
		int b = is.read();
		while (b != -1) {
			os.write(b);
			b = is.read();
		}
		is.close();
		os.close();
	}

	/**
	 * This the RLE compressed pattern file that happens to not compress via LZW.
	 */
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
	/**
	 * This is the uncompressed pattern file that happens to not compress via LZW.
	 */
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
