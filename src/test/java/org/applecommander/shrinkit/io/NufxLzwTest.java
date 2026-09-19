/*
 * ShrinkItArchive
 * Copyright (C) 2026  Rob Greene
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see
 * <https://www.gnu.org/licenses/>.
 */
package org.applecommander.shrinkit.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import org.applecommander.shrinkit.HeaderBlock;
import org.applecommander.shrinkit.NuFileArchive;
import org.applecommander.shrinkit.ThreadRecord;

/**
 * Compare actual decompressed content against the expected content.
 * This is a very broad test of the NuFX archive format.
 * 
 * @author robgreene@users.sourceforge.net
 */
public class NufxLzwTest extends TestBase {
	@Test
	public void testNufxLzw1() throws IOException {
		check("/APPLE.II-LZW1.SHK", "APPLE.II", "/APPLE.II.txt");
	}
	@Test
	public void testNufxLzw2() throws IOException {
		check("/APPLE.II-LZW2.SHK", "APPLE.II", "/APPLE.II.txt");
	}
	
	@Test
	public void testLargerFilesNufxLzw1() throws IOException {
		check("/PRODOS.MSTR-LZW1.SHK", "SYSUTIL.SYSTEM", "/SYSUTIL.SYSTEM.bin");
		check("/PRODOS.MSTR-LZW1.SHK", "UTIL.0", "/UTIL.0.bin");
		check("/PRODOS.MSTR-LZW1.SHK", "UTIL.1", "/UTIL.1.bin");
		check("/PRODOS.MSTR-LZW1.SHK", "UTIL.2", "/UTIL.2.bin");
	}
	@Test
	public void testLargerFilesNufxLzw2() throws IOException {
		check("/PRODOS.MSTR-LZW2.SHK", "SYSUTIL.SYSTEM", "/SYSUTIL.SYSTEM.bin");
		check("/PRODOS.MSTR-LZW2.SHK", "UTIL.0", "/UTIL.0.bin");
		check("/PRODOS.MSTR-LZW2.SHK", "UTIL.1", "/UTIL.1.bin");
		check("/PRODOS.MSTR-LZW2.SHK", "UTIL.2", "/UTIL.2.bin");
	}
	
	@Test
	public void testUncompressedFiles() throws IOException {
		check("/UNCOMPRESSED.SHK", "APPLE.II-LZW1.SHK", "/APPLE.II-LZW1.SHK");
		check("/UNCOMPRESSED.SHK", "APPLE.II-LZW2.SHK", "/APPLE.II-LZW2.SHK");
		check("/UNCOMPRESSED.SHK", "PRODOS.MSTR-LZW1.SHK", "/PRODOS.MSTR-LZW1.SHK");
		check("/UNCOMPRESSED.SHK", "PRODOS.MSTR-LZW2.SHK", "/PRODOS.MSTR-LZW2.SHK");
	}
	
	/**
	 * Given details about an archive file and it's expected contents, locate that
	 * file and then check it against the contents.
	 */
	protected void check(String archiveName, String archiveFile, String expectedContentFile) throws IOException {
		NuFileArchive archive = new NuFileArchive(getClass().getResourceAsStream(archiveName));
		List<HeaderBlock> headers = archive.getHeaderBlocks();
		byte[] actual = null;
		for (HeaderBlock header : headers) {
			if (archiveFile.equals(header.getFilename())) {
				ThreadRecord r = header.getDataForkThreadRecord();
				long bytes = r.getThreadEof();
				ByteArrayOutputStream buf = new ByteArrayOutputStream();
				InputStream is = r.getInputStream();
				while ( bytes-- > 0 ) {
					buf.write(is.read());
				}
				actual = buf.toByteArray();
				is.close();
				break;
			}
		}
		InputStream is = getClass().getResourceAsStream(expectedContentFile);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int b;
		while ( (b = is.read()) != -1 ) {
			buf.write(b);
		}
		byte[] expected = buf.toByteArray();
		assertEquals(expected, actual);
	}
}
