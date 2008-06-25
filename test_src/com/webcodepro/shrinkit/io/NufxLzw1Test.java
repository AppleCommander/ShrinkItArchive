package com.webcodepro.shrinkit.io;

import java.io.IOException;
import java.util.List;

import com.webcodepro.shrinkit.HeaderBlock;
import com.webcodepro.shrinkit.NuFileArchive;
import com.webcodepro.shrinkit.ThreadKind;
import com.webcodepro.shrinkit.ThreadRecord;

/**
 * Test some LZW/1 format streams.
 * 
 * @author robgreene@users.sourceforge.net
 */
public class NufxLzw1Test extends TestCaseHelper {
	public void testTextFile() throws IOException {
		NufxLzw1InputStream is = new NufxLzw1InputStream(new LittleEndianByteInputStream(getTextFileLzw1StreamData()));
		byte[] expected = getTextFileData();
		byte[] actual = new byte[expected.length];
		is.read(actual);
		assertEquals(expected, actual);
		assertTrue(is.isCrcValid());
	}
	
	public void testAppleIIShk() throws IOException {
		NuFileArchive archive = new NuFileArchive(getClass().getResourceAsStream("APPLE.II-LZW1.SHK"));
		List<HeaderBlock> blocks = archive.getHeaderBlocks();
		HeaderBlock block = blocks.get(0);	// only one file
		if (block.getFilename() != null) System.out.printf("\n\n%s\n\n", block.getFilename());
		List<ThreadRecord> records = block.getThreadRecords();
		for (ThreadRecord record : records) {
			if (record.getThreadKind() == ThreadKind.FILENAME) {
				System.out.printf("\n\n%s\n\n", record.getText());
			}
			long bytes = record.getThreadEof();
			if (record.getThreadKind() == ThreadKind.DATA_FORK) {
				NufxLzw1InputStream is = new NufxLzw1InputStream(new LittleEndianByteInputStream(record.getRawInputStream()));
				while ( bytes-- > 0 ) {
					System.out.print((char)is.read());
				}
			}
		}
	}

	
	private byte[] getTextFileLzw1StreamData() {
		return new byte[] {
				(byte)0xCA, 0x42, 0x00, (byte)0xDB, (byte)0xB7, 0x00, 0x01, 0x54, 
				(byte)0x90, 0x24, (byte)0x99, 0x02, 0x62, 0x20, (byte)0x88, (byte)0x80, 
				0x45, 0x40, 0x5C, 0x09, (byte)0x92, 0x45, 0x61, (byte)0xC2, 
				(byte)0x85, 0x53, (byte)0x90, (byte)0x80, 0x78, 0x52, 0x45, 0x0A, 
				(byte)0x88, 0x21, 0x4C, (byte)0x9E, 0x20, (byte)0x9C, (byte)0xC2, 0x42, 
				0x61, (byte)0x90, (byte)0x88, 0x13, 0x2B, 0x5E, (byte)0xCC, (byte)0xB8, 
				(byte)0xB1, 0x23, 0x44, (byte)0x89, 0x14, 0x2D, 0x62, (byte)0xD4, 
				(byte)0x88, (byte)0xA4, (byte)0xC8, 0x14, 0x17, 0x20, 0x0E, 0x0A, 
				0x24, 0x68, 0x10, (byte)0xA1, (byte)0xC7, (byte)0x86, 0x57, 0x1E, 
				0x7E, 0x44, 0x29, 0x72, 0x65, 0x49, 0x10, 0x53, 
				(byte)0x9E, (byte)0x80, 0x28, 0x12, 0x44, 0x0A, (byte)0x93, (byte)0x86, 
				0x49, (byte)0x9C, (byte)0xC8, 0x4C, (byte)0xD8, (byte)0xE4, (byte)0x89, 0x14, 
				0x27, 0x49, (byte)0x8F, (byte)0xB8, (byte)0xD8, 0x06, (byte)0xE0, 0x1F, 
				0x55, (byte)0xAB, 0x55, (byte)0xAF, 0x6A, (byte)0xCD, (byte)0xCA, 0x15, 
				(byte)0xAB, (byte)0xD7, (byte)0xAD, 0x5F, (byte)0xBB, 0x52, (byte)0xC5, 0x03, 
				0x00	
		};
	}
	private byte[] getTextFileData() {
		byte[] data = new byte[4096];	// file was forced to be 4096 bytes long
		String s = "THIS IS THE WAY WE WASH OUR CLOTHES, WASH OUR CLOTHES, WASH OUR CLOTHES.  " +
				"THIS IS THE WAY WE WASH OUR CLOTHES, SO EARLY IN THE MORNING.";
		System.arraycopy(s.getBytes(), 0, data, 0, s.length());
		return data;
	}
}
