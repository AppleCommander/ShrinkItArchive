package com.webcodepro.shrinkit.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

/**
 * Exercise the LittleEndianByteOutputStream class.
 * @author robgreene@users.sourceforge.net
 */
public class LittleEndianByteOutputStreamTest extends TestBase {
	@Test
	public void testWriteA() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.write('a');
		bs.close();
		assertEquals("a".getBytes(), os.toByteArray());
	}
	@Test
	public void testWriteB() throws IOException {
		// Just to ensure we can write chunks of bytes...
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.write("hello".getBytes());
		bs.close();
		assertEquals("hello".getBytes(), os.toByteArray());
	}
	@Test
	public void testWriteNuFileId() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.writeNuFileId();
		bs.close();
		assertEquals(new byte[] { 0x4e, (byte)0xf5, 0x46, (byte)0xe9, 0x6c, (byte)0xe5 }, os.toByteArray());
	}
	@Test
	public void testCheckNuFxId() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.writeNuFxId();
		bs.close();
		assertEquals(new byte[] { 0x4e, (byte)0xf5, 0x46, (byte)0xd8 }, os.toByteArray());
	}
	@Test
	public void testWriteWord() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.writeWord(0x201);
		bs.writeWord(0x403);
		bs.close();
		assertEquals(new byte[] { 0x01, 0x02, 0x03, 0x04 }, os.toByteArray());
	}
	@Test
	public void testWriteWordHighBitSet() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.writeWord(0xffff);
		bs.close();
		assertEquals(new byte[] { (byte)0xff, (byte)0xff }, os.toByteArray());
	}
	@Test
	public void testWriteLong() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.writeLong(0x04030201);
		bs.close();
		assertEquals(new byte[] { 0x01, 0x02, 0x03, 0x04 }, os.toByteArray());
	}
	@Test
	public void testWriteLongHighBitSet() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.writeLong(0xffffffffL);
		bs.close();
		assertEquals(new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff }, os.toByteArray());
	}
	@Test
	public void testWriteDate() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.writeDate(new GregorianCalendar(1988, Calendar.OCTOBER, 22, 1, 10, 0).getTime());
		bs.writeDate(new GregorianCalendar(1988, Calendar.NOVEMBER, 17, 11, 16, 0).getTime());
		bs.writeDate(new GregorianCalendar(1988, Calendar.OCTOBER, 22, 13, 12, 0).getTime());
		bs.close();
		byte[] expected = new byte[] { 
				// From NuFX documentation, final revision 3
				0x00, 0x0a, 0x01, 0x58, 0x16, 0x0a, 0x00, 0x07,	// 01:10:00am 10/22/1988 saturday
				0x00, 0x10, 0x0b, 0x58, 0x11, 0x0b, 0x00, 0x05,	// 11:16:00am 11/17/1988 thursday
				0x00, 0x0c, 0x0d, 0x58, 0x16, 0x0a, 0x00, 0x07,	// 01:12:00pm 10/22/1988 saturday
			};
		assertEquals(expected, os.toByteArray());
	}
	@Test
	public void testWriteNullDate() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		LittleEndianByteOutputStream bs = new LittleEndianByteOutputStream(os);
		bs.writeDate(null);
		bs.close();
		byte[] expected = new byte[] {
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00	// null date
			};
		assertEquals(expected, os.toByteArray());
	}
}
