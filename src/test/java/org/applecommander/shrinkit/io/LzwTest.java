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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Exercise the LZW encoder and decoders.
 * 
 * @author robgreene@users.sourceforge.net
 */
public class LzwTest extends TestBase {
	@Test
	public void testLzwDecoder() throws IOException {
		try (LzwInputStream is = new LzwInputStream(new BitInputStream(new ByteArrayInputStream(getHgrColorsLzw1()), 9))) {
			int[] expected = getHgrColorsUncompressed();
			
			int[] actual = new int[expected.length];
			for (int i=0; i<actual.length; i++) actual[i] = is.read();
			
			Assert.assertEquals("Expecting end of stream", -1, is.read());
			assertEquals(expected, actual);
		}
	}
	@Test
	public void testLzwEncoder() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		LzwOutputStream os = new LzwOutputStream(new BitOutputStream(baos, 9));
		byte[] expected = getHgrColorsLzw1();

		os.write(asBytes(getHgrColorsUncompressed()));
		os.close();
		byte[] actual = baos.toByteArray();
		
		assertEquals(expected, actual);
	}

	@Test
	public void testLzwDecoder2() throws IOException {
		try (RleInputStream is = new RleInputStream(new LittleEndianByteInputStream(new LzwInputStream(new BitInputStream(new ByteArrayInputStream(getHgrColorsLzw1()), 9))))) {
			int bytes = 0;
			int b;
			while ( (b = is.read()) != -1) {
				if (bytes % 16 == 0) {
					if (bytes > 0) System.out.println();
					System.out.printf("%08x:  ", bytes);
				}
				System.out.printf("%02x ", b);
				bytes++;
			}
		}
	}
	
	@Test
	public void testLzwDecoder3() throws IOException {
		try (LzwInputStream is = new LzwInputStream(new BitInputStream(new ByteArrayInputStream(getTextFileLzw1()), 9))) {
			System.out.printf("\n\nText File decoded...\n\n");
			int i = 0;
			int b = 0;
			while (b != -1) {
				b = is.read();
				if (b != -1) System.out.printf("$%04x: %02x (%c)\n", i++, b, b);
			}
			System.out.printf("** END **");
		}
	}
	
	protected byte[] asBytes(int[] source) {
		byte[] array = new byte[source.length];
		for (int i=0; i<source.length; i++) array[i] = (byte)(source[i] & 0xff);
		return array;
	}
	protected int[] asInts(byte[] source) {
		int[] array = new int[source.length];
		for (int i=0; i<source.length; i++) array[i] = source[i] & 0xff;
		return array;
	}
}
