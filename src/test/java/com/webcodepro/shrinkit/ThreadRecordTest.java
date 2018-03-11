package com.webcodepro.shrinkit;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.webcodepro.shrinkit.io.LittleEndianByteInputStream;

/**
 * Exercise the Thread Record.
 * The source of some these test cases come from the "NuFX
 * Documentation Final Revision Three" document.
 * @author robgreene@users.sourceforge.net
 */
public class ThreadRecordTest {
	/**
	 * From "NuFX Documentation Final Revision Three", version 0, "Normal Files" sample. 
	 */
	@Test
	public void testNormalFiles() throws IOException {
		byte[] data = {
				0x02, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 
				0x00, 0x20, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00
			};
		LittleEndianByteInputStream bs = new LittleEndianByteInputStream(data);
		ThreadRecord r = new ThreadRecord(bs);
		Assert.assertEquals(ThreadClass.DATA, r.getThreadClass());
		Assert.assertEquals(ThreadFormat.DYNAMIC_LZW1, r.getThreadFormat());
		Assert.assertEquals(ThreadKind.DATA_FORK, r.getThreadKind());
		Assert.assertEquals(0x0000, r.getThreadCrc());
		Assert.assertEquals(0x00002000, r.getThreadEof());
		Assert.assertEquals(0x00001000, r.getCompThreadEof());
	}
	
	/**
	 * From "NuFX Documentation Final Revision Three", version 0, "Extended Files" sample. 
	 */
	@Test
	public void testExtendedFiles() throws IOException {
		byte[] data = {
				0x02, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 
				0x00, 0x20, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00,
				0x02, 0x00, 0x02, 0x00, 0x02, 0x00, 0x00, 0x00, 
				0x00, 0x10, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00
			};
		LittleEndianByteInputStream bs = new LittleEndianByteInputStream(data);
		ThreadRecord r1 = new ThreadRecord(bs);
		Assert.assertEquals(ThreadClass.DATA, r1.getThreadClass());
		Assert.assertEquals(ThreadFormat.DYNAMIC_LZW1, r1.getThreadFormat());
		Assert.assertEquals(ThreadKind.DATA_FORK, r1.getThreadKind());
		Assert.assertEquals(0x0000, r1.getThreadCrc());
		Assert.assertEquals(0x00002000, r1.getThreadEof());
		Assert.assertEquals(0x00000800, r1.getCompThreadEof());
		ThreadRecord r2 = new ThreadRecord(bs);
		Assert.assertEquals(ThreadClass.DATA, r2.getThreadClass());
		Assert.assertEquals(ThreadFormat.DYNAMIC_LZW1, r2.getThreadFormat());
		Assert.assertEquals(ThreadKind.RESOURCE_FORK, r2.getThreadKind());
		Assert.assertEquals(0x0000, r2.getThreadCrc());
		Assert.assertEquals(0x00001000, r2.getThreadEof());
		Assert.assertEquals(0x00000800, r2.getCompThreadEof());
	}

	/**
	 * From "NuFX Documentation Final Revision Three", version 0, "Disk" sample. 
	 */
	@Test
	public void testDiskImage() throws IOException {
		byte[] data = {
				0x02, 0x00, 0x02, 0x00, 0x01, 0x00, 0x00, 0x00, 
				0x00, 0x00, 0x00, 0x00, 0x51, 0x45, 0x07, 0x00
			};
		LittleEndianByteInputStream bs = new LittleEndianByteInputStream(data);
		ThreadRecord r = new ThreadRecord(bs);
		Assert.assertEquals(ThreadClass.DATA, r.getThreadClass());
		Assert.assertEquals(ThreadFormat.DYNAMIC_LZW1, r.getThreadFormat());
		Assert.assertEquals(ThreadKind.DISK_IMAGE, r.getThreadKind());
		Assert.assertEquals(0x0000, r.getThreadCrc());
		Assert.assertEquals(0x00000000, r.getThreadEof());
		Assert.assertEquals(0x00074551, r.getCompThreadEof());
	}
	
	/**
	 * Sample taken from the SCC.SHK file, first header entry.
	 */
	@Test
	public void testSccShkHeader1() throws IOException {
		byte[] data = {
				0x03, 0x00, 0x00, 0x00,	0x00, 0x00, 0x00, 0x00, 
				0x0b, 0x00, 0x00, 0x00,	0x20, 0x00, 0x00, 0x00, 
				0x00, 0x00, 0x00, 0x00,	0x01, 0x00, 0x00, 0x00, 
				0x00, 0x00, 0x00, 0x00,	(byte)0xc8, 0x00, 0x00, 0x00, 
				0x02, 0x00, 0x03, 0x00, 0x00, 0x00, 0x58, 0x4a, 
				(byte)0xd6, 0x06, 0x00, 0x00, (byte)0xd4, 0x03, 0x00, 0x00
			};
		LittleEndianByteInputStream bs = new LittleEndianByteInputStream(data);
		ThreadRecord r1 = new ThreadRecord(bs);
		Assert.assertEquals(ThreadClass.FILENAME, r1.getThreadClass());
		Assert.assertEquals(ThreadFormat.UNCOMPRESSED, r1.getThreadFormat());
		Assert.assertEquals(ThreadKind.FILENAME, r1.getThreadKind());
		Assert.assertEquals(0x0000, r1.getThreadCrc());
		Assert.assertEquals(0x0000000b, r1.getThreadEof());
		Assert.assertEquals(0x00000020, r1.getCompThreadEof());
		ThreadRecord r2 = new ThreadRecord(bs);
		Assert.assertEquals(ThreadClass.MESSAGE, r2.getThreadClass());
		Assert.assertEquals(ThreadFormat.UNCOMPRESSED, r2.getThreadFormat());
		Assert.assertEquals(ThreadKind.ALLOCATED_SPACE, r2.getThreadKind());
		Assert.assertEquals(0x0000, r2.getThreadCrc());
		Assert.assertEquals(0x00000000, r2.getThreadEof());
		Assert.assertEquals(0x000000c8, r2.getCompThreadEof());
		ThreadRecord r3 = new ThreadRecord(bs);
		Assert.assertEquals(ThreadClass.DATA, r3.getThreadClass());
		Assert.assertEquals(ThreadFormat.DYNAMIC_LZW2, r3.getThreadFormat());
		Assert.assertEquals(ThreadKind.DATA_FORK, r3.getThreadKind());
		Assert.assertEquals(0x4a58, r3.getThreadCrc());
		Assert.assertEquals(0x000006d6, r3.getThreadEof());
		Assert.assertEquals(0x000003d4, r3.getCompThreadEof());
	}

	/**
	 * Sample taken from the SCC.SHK file, first header entry.
	 */
	@Test
	public void testSccShkHeader1FilenameThread() throws IOException {
		byte[] data = {
				0x03, 0x00, 0x00, 0x00,	0x00, 0x00, 0x00, 0x00, 
				0x0b, 0x00, 0x00, 0x00,	0x20, 0x00, 0x00, 0x00,
				0x73, 0x63, 0x63, 0x3a, 0x65, 0x71, 0x75, 0x61,
				0x74, 0x65, 0x73, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
			};
		LittleEndianByteInputStream bs = new LittleEndianByteInputStream(data);
		ThreadRecord r = new ThreadRecord(bs);
		r.readThreadData(bs);
		Assert.assertTrue(r.isText());
		Assert.assertEquals("scc:equates", r.getText());
	}
}
