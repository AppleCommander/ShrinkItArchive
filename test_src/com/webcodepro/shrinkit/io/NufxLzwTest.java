package com.webcodepro.shrinkit.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import junit.framework.TestCase;

import com.webcodepro.shrinkit.HeaderBlock;
import com.webcodepro.shrinkit.NuFileArchive;
import com.webcodepro.shrinkit.ThreadRecord;

/**
 * Compare actual decompressed content against the expected content.
 * This is a very broad test of the NuFX archive format.
 * 
 * @author robgreene@users.sourceforge.net
 */
public class NufxLzwTest extends TestCase {
	public void testNufxLzw1() throws IOException {
		check("APPLE.II-LZW1.SHK", "APPLE.II", "APPLE.II.txt");
	}
	public void testNufxLzw2() throws IOException {
		check("APPLE.II-LZW2.SHK", "APPLE.II", "APPLE.II.txt");
	}
	
	public void testLargerFilesNufxLzw1() throws IOException {
		check("PRODOS.MSTR-LZW1.SHK", "SYSUTIL.SYSTEM", "SYSUTIL.SYSTEM.bin");
		check("PRODOS.MSTR-LZW1.SHK", "UTIL.0", "UTIL.0.bin");
		check("PRODOS.MSTR-LZW1.SHK", "UTIL.1", "UTIL.1.bin");
		check("PRODOS.MSTR-LZW1.SHK", "UTIL.2", "UTIL.2.bin");
	}
	public void testLargerFilesNufxLzw2() throws IOException {
		check("PRODOS.MSTR-LZW2.SHK", "SYSUTIL.SYSTEM", "SYSUTIL.SYSTEM.bin");
		check("PRODOS.MSTR-LZW2.SHK", "UTIL.0", "UTIL.0.bin");
		check("PRODOS.MSTR-LZW2.SHK", "UTIL.1", "UTIL.1.bin");
		check("PRODOS.MSTR-LZW2.SHK", "UTIL.2", "UTIL.2.bin");
	}
	
	/**
	 * Given details about an archive file and it's expected contents, locate that
	 * file and then check it against the contents.
	 */
	protected void check(String archiveName, String archiveFile, String expectedContentFile) throws IOException {
		NuFileArchive archive = new NuFileArchive(getClass().getResourceAsStream(archiveName));
		List<HeaderBlock> headers = archive.getHeaderBlocks();
		String actual = null;
		for (HeaderBlock header : headers) {
			if (archiveFile.equals(header.getFilename())) {
				ThreadRecord r = header.getDataForkInputStream();
				long bytes = r.getThreadEof();
				StringBuffer buf = new StringBuffer();
				InputStream is = r.getInputStream();
				while ( bytes-- > 0 ) {
					buf.append((char)is.read());
				}
				actual = buf.toString();
				is.close();
				break;
			}
		}
		InputStream is = getClass().getResourceAsStream(expectedContentFile);
		StringBuffer buf = new StringBuffer();
		int ch;
		while ( (ch = is.read()) != -1 ) {
			buf.append((char)ch);
		}
		String expected = buf.toString();
		assertEquals(expected, actual);
	}
}
