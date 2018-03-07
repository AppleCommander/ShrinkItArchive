package com.webcodepro.shrinkit.io;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

/**
 * Exercise the LittleEndianByteInputStream class.
 * @author robgreene@users.sourceforge.net
 */
public class LittleEndianByteInputStreamTest extends TestCase {
	public void testReadA() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream("a".getBytes())) {
			assertEquals('a', bs.read());
			assertEquals(-1, bs.read());
			assertEquals(1, bs.getTotalBytesRead());
		}
	}
	public void testReadB() throws IOException {
		// Just to ensure we can get more than one byte...
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream("hello".getBytes())) {
			assertEquals('h', bs.read());
			assertEquals('e', bs.read());
			assertEquals('l', bs.read());
			assertEquals('l', bs.read());
			assertEquals('o', bs.read());
			assertEquals(-1, bs.read());
			assertEquals(5, bs.getTotalBytesRead());
		}
	}
	public void testReadBytesInt() throws IOException {
		// Ensure we read the requested data.
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream("HelloWorld".getBytes())) {
			assertEquals("Hello", new String(bs.readBytes(5)));
			assertEquals("World", new String(bs.readBytes(5)));
			assertEquals(-1, bs.read());
			assertEquals(10, bs.getTotalBytesRead());
		}
	}
	public void textReadBytesIntError() throws IOException {
		// Ensure that we fail appropriately
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream("Hi".getBytes())) {
			try {
				bs.readBytes(3);
				fail();
			} catch (IOException ex) {
				assertTrue(true);	// Expected
				assertEquals(2, bs.getTotalBytesRead());
			}
		}
	}
// 	This methods got removed at some point but nobody updated the unit tests. Bad developer!
//	public void testCheckNuFileId() throws IOException {
//		LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { 0x4e, (byte)0xf5, 0x46, (byte)0xe9, 0x6c, (byte)0xe5 });
//		assertTrue(NuFileArchive.bs.checkNuFileId());
//		assertEquals(6, bs.getTotalBytesRead());
//		bs = new LittleEndianByteInputStream("NotNuFile".getBytes());
//		assertFalse(bs.checkNuFileId());
//	}
// 	This methods got removed at some point but nobody updated the unit tests. Bad developer!
//	public void testCheckNuFxId() throws IOException {
//		LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { 0x4e, (byte)0xf5, 0x46, (byte)0xd8 });
//		assertTrue(bs.checkNuFxId());
//		assertEquals(4, bs.getTotalBytesRead());
//		bs = new LittleEndianByteInputStream("NotNuFx".getBytes());
//		assertFalse(bs.checkNuFxId());
//	}
	public void testReadWord() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 })) {
			assertEquals(0x0201, bs.readWord());
			assertEquals(0x0403, bs.readWord());
			assertEquals(4, bs.getTotalBytesRead());
		}
	}
	public void testReadWordHighBitSet() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { (byte)0xff, (byte)0xff })) {
			assertEquals(0xffff, bs.readWord());
			assertEquals(2, bs.getTotalBytesRead());
		}
	}
	public void testReadLong() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 })) {
			assertEquals(0x04030201, bs.readLong());
			assertEquals(4, bs.getTotalBytesRead());
		}
	}
	public void testReadLongHighBitSet() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff })) {
			assertEquals(0xffffffffL, bs.readLong());
			assertEquals(4, bs.getTotalBytesRead());
		}
	}
	public void testReadDate() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] { 
					// From NuFX documentation, final revision 3
					0x00, 0x0a, 0x01, 0x58, 0x16, 0x0a, 0x00, 0x07,	// 01:10:00am 10/22/1988 Saturday
					0x00, 0x10, 0x0b, 0x58, 0x11, 0x0b, 0x00, 0x05,	// 11:16:00am 11/17/1988 Thursday
					0x00, 0x0c, 0x0d, 0x58, 0x16, 0x0a, 0x00, 0x07,	// 01:12:00pm 10/22/1988 Saturday
				})) {
			assertEquals(new GregorianCalendar(1988, Calendar.OCTOBER, 22, 1, 10, 0).getTime(), bs.readDate());
			assertEquals(new GregorianCalendar(1988, Calendar.NOVEMBER, 17, 11, 16, 0).getTime(), bs.readDate());
			assertEquals(new GregorianCalendar(1988, Calendar.OCTOBER, 22, 13, 12, 0).getTime(), bs.readDate());
			assertEquals(24, bs.getTotalBytesRead());
		}
	}
	public void testReadNullDate() throws IOException {
		try (LittleEndianByteInputStream bs = new LittleEndianByteInputStream(new byte[] {
					0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00	// null date
				})) {
			assertNull(bs.readDate());
			assertEquals(8, bs.getTotalBytesRead());
		}
	}
}
