package com.webcodepro.shrinkit;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * Exercise the Thread Record.
 * The source of some these test cases come from the "NuFX
 * Documentation Final Revision Three" document.
 * @author robgreene@users.sourceforge.net
 */
public class ThreadRecordTest extends TestCase {
	/**
	 * From "NuFX Documentation Final Revision Three", version 0, "Normal Files" sample. 
	 */
	public void testNormalFiles() throws IOException {
		byte[] data = {
				0x02, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 
				0x00, 0x20, 0x00, 0x00, 0x00, 0x10, 0x00, 0x00
			};
		ByteSource bs = new ByteSource(data);
		ThreadRecord r = new ThreadRecord(bs);
		assertEquals(ThreadClass.DATA, r.getThreadClass());
		assertEquals(ThreadFormat.DYNAMIC_LZW1, r.getThreadFormat());
		assertEquals(ThreadKind.DATA_FORK, r.getThreadKind());
		assertEquals(0x0000, r.getThreadCrc());
		assertEquals(0x00002000, r.getThreadEof());
		assertEquals(0x00001000, r.getCompThreadEof());
	}
	
	/**
	 * From "NuFX Documentation Final Revision Three", version 0, "Extended Files" sample. 
	 */
	public void testExtendedFiles() throws IOException {
		byte[] data = {
				0x02, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 
				0x00, 0x20, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00,
				0x02, 0x00, 0x02, 0x00, 0x02, 0x00, 0x00, 0x00, 
				0x00, 0x10, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00
			};
		ByteSource bs = new ByteSource(data);
		ThreadRecord r1 = new ThreadRecord(bs);
		assertEquals(ThreadClass.DATA, r1.getThreadClass());
		assertEquals(ThreadFormat.DYNAMIC_LZW1, r1.getThreadFormat());
		assertEquals(ThreadKind.DATA_FORK, r1.getThreadKind());
		assertEquals(0x0000, r1.getThreadCrc());
		assertEquals(0x00002000, r1.getThreadEof());
		assertEquals(0x00000800, r1.getCompThreadEof());
		ThreadRecord r2 = new ThreadRecord(bs);
		assertEquals(ThreadClass.DATA, r2.getThreadClass());
		assertEquals(ThreadFormat.DYNAMIC_LZW1, r2.getThreadFormat());
		assertEquals(ThreadKind.RESOURCE_FORK, r2.getThreadKind());
		assertEquals(0x0000, r2.getThreadCrc());
		assertEquals(0x00001000, r2.getThreadEof());
		assertEquals(0x00000800, r2.getCompThreadEof());
	}

	/**
	 * From "NuFX Documentation Final Revision Three", version 0, "Disk" sample. 
	 */
	public void testDiskImage() throws IOException {
		byte[] data = {
				0x02, 0x00, 0x02, 0x00, 0x01, 0x00, 0x00, 0x00, 
				0x00, 0x00, 0x00, 0x00, 0x51, 0x45, 0x07, 0x00
			};
		ByteSource bs = new ByteSource(data);
		ThreadRecord r = new ThreadRecord(bs);
		assertEquals(ThreadClass.DATA, r.getThreadClass());
		assertEquals(ThreadFormat.DYNAMIC_LZW1, r.getThreadFormat());
		assertEquals(ThreadKind.DISK_IMAGE, r.getThreadKind());
		assertEquals(0x0000, r.getThreadCrc());
		assertEquals(0x00000000, r.getThreadEof());
		assertEquals(0x00074551, r.getCompThreadEof());
	}
	
	/**
	 * Sample taken from the SCC.SHK file, first header entry.
	 */
	public void testSccShkHeader1() throws IOException {
		byte[] data = {
				0x03, 0x00, 0x00, 0x00,	0x00, 0x00, 0x00, 0x00, 
				0x0b, 0x00, 0x00, 0x00,	0x20, 0x00, 0x00, 0x00, 
				0x00, 0x00, 0x00, 0x00,	0x01, 0x00, 0x00, 0x00, 
				0x00, 0x00, 0x00, 0x00,	(byte)0xc8, 0x00, 0x00, 0x00, 
				0x02, 0x00, 0x03, 0x00, 0x00, 0x00, 0x58, 0x4a, 
				(byte)0xd6, 0x06, 0x00, 0x00, (byte)0xd4, 0x03, 0x00, 0x00
			};
		ByteSource bs = new ByteSource(data);
		ThreadRecord r1 = new ThreadRecord(bs);
		assertEquals(ThreadClass.FILENAME, r1.getThreadClass());
		assertEquals(ThreadFormat.UNCOMPRESSED, r1.getThreadFormat());
		assertEquals(ThreadKind.FILENAME, r1.getThreadKind());
		assertEquals(0x0000, r1.getThreadCrc());
		assertEquals(0x0000000b, r1.getThreadEof());
		assertEquals(0x00000020, r1.getCompThreadEof());
		ThreadRecord r2 = new ThreadRecord(bs);
		assertEquals(ThreadClass.MESSAGE, r2.getThreadClass());
		assertEquals(ThreadFormat.UNCOMPRESSED, r2.getThreadFormat());
		assertEquals(ThreadKind.ALLOCATED_SPACE, r2.getThreadKind());
		assertEquals(0x0000, r2.getThreadCrc());
		assertEquals(0x00000000, r2.getThreadEof());
		assertEquals(0x000000c8, r2.getCompThreadEof());
		ThreadRecord r3 = new ThreadRecord(bs);
		assertEquals(ThreadClass.DATA, r3.getThreadClass());
		assertEquals(ThreadFormat.DYNAMIC_LZW2, r3.getThreadFormat());
		assertEquals(ThreadKind.DATA_FORK, r3.getThreadKind());
		assertEquals(0x4a58, r3.getThreadCrc());
		assertEquals(0x000006d6, r3.getThreadEof());
		assertEquals(0x000003d4, r3.getCompThreadEof());
	}

	/**
	 * Sample taken from the SCC.SHK file, first header entry.
	 */
	public void testSccShkHeader1FilenameThread() throws IOException {
		byte[] data = {
				0x03, 0x00, 0x00, 0x00,	0x00, 0x00, 0x00, 0x00, 
				0x0b, 0x00, 0x00, 0x00,	0x20, 0x00, 0x00, 0x00,
				0x73, 0x63, 0x63, 0x3a, 0x65, 0x71, 0x75, 0x61,
				0x74, 0x65, 0x73, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
			};
		ByteSource bs = new ByteSource(data);
		ThreadRecord r = new ThreadRecord(bs);
		r.readThreadData(bs);
		assertTrue(r.isText());
		assertEquals("scc:equates", r.getText());
	}
}
