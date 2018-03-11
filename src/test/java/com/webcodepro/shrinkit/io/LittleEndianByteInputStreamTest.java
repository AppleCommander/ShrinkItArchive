package com.webcodepro.shrinkit.io;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import com.webcodepro.shrinkit.NuFileArchive;

/**
 * Exercise the LittleEndianByteInputStream class.
 * @author robgreene@users.sourceforge.net
 */
public class LittleEndianByteInputStreamTest {
	@Test
	public void testReadA() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream("a".getBytes())) {
			Assert.assertEquals('a', bs.read());
			Assert.assertEquals(-1, bs.read());
			Assert.assertEquals(1, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testReadB() throws IOException {
		// Just to ensure we can get more than one byte...
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream("hello".getBytes())) {
			Assert.assertEquals('h', bs.read());
			Assert.assertEquals('e', bs.read());
			Assert.assertEquals('l', bs.read());
			Assert.assertEquals('l', bs.read());
			Assert.assertEquals('o', bs.read());
			Assert.assertEquals(-1, bs.read());
			Assert.assertEquals(5, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testReadBytesInt() throws IOException {
		// Ensure we read the requested data.
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream("HelloWorld".getBytes())) {
			Assert.assertEquals("Hello", new String(bs.readBytes(5)));
			Assert.assertEquals("World", new String(bs.readBytes(5)));
			Assert.assertEquals(-1, bs.read());
			Assert.assertEquals(10, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testReadBytesIntError() throws IOException {
		// Ensure that we fail appropriately
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream("Hi".getBytes())) {
			try {
				bs.readBytes(3);
				Assert.fail();
			} catch (IOException ex) {
				Assert.assertTrue(true);	// Expected
				Assert.assertEquals(2, bs.getTotalBytesRead());
			}
		}
	}
	@Test
	public void testCheckNuFileId() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(
				new byte[] { 0x4e, (byte)0xf5, 0x46, (byte)0xe9, 0x6c, (byte)0xe5 })) {
			Assert.assertEquals(NuFileArchive.NUFILE_ARCHIVE, bs.seekFileType(6));
			Assert.assertEquals(6, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testCheckNuFxId() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(
				new byte[] { 0x4e, (byte)0xf5, 0x46, (byte)0xd8 })) {
			Assert.assertEquals(NuFileArchive.NUFX_ARCHIVE, bs.seekFileType(4));
			Assert.assertEquals(4, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testReadWord() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 })) {
			Assert.assertEquals(0x0201, bs.readWord());
			Assert.assertEquals(0x0403, bs.readWord());
			Assert.assertEquals(4, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testReadWordHighBitSet() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { (byte)0xff, (byte)0xff })) {
			Assert.assertEquals(0xffff, bs.readWord());
			Assert.assertEquals(2, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testReadLong() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 })) {
			Assert.assertEquals(0x04030201, bs.readLong());
			Assert.assertEquals(4, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testReadLongHighBitSet() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff })) {
			Assert.assertEquals(0xffffffffL, bs.readLong());
			Assert.assertEquals(4, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testReadDate() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { 
					// From NuFX documentation, final revision 3
					0x00, 0x0a, 0x01, 0x58, 0x16, 0x0a, 0x00, 0x07,	// 01:10:00am 10/22/1988 Saturday
					0x00, 0x10, 0x0b, 0x58, 0x11, 0x0b, 0x00, 0x05,	// 11:16:00am 11/17/1988 Thursday
					0x00, 0x0c, 0x0d, 0x58, 0x16, 0x0a, 0x00, 0x07,	// 01:12:00pm 10/22/1988 Saturday
				})) {
			Assert.assertEquals(new GregorianCalendar(1988, Calendar.OCTOBER, 22, 1, 10, 0).getTime(), bs.readDate());
			Assert.assertEquals(new GregorianCalendar(1988, Calendar.NOVEMBER, 17, 11, 16, 0).getTime(), bs.readDate());
			Assert.assertEquals(new GregorianCalendar(1988, Calendar.OCTOBER, 22, 13, 12, 0).getTime(), bs.readDate());
			Assert.assertEquals(24, bs.getTotalBytesRead());
		}
	}
	@Test
	public void testReadNullDate() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] {
					0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00	// null date
				})) {
			Assert.assertNull(bs.readDate());
			Assert.assertEquals(8, bs.getTotalBytesRead());
		}
	}
}
