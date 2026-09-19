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

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Exercise the BitOutputStream.  
 * 
 * @author robgreene@users.sourceforge.net
 */
public class BitOutputStreamTest extends TestBase {
	@Test
    public void test1() throws IOException { 
        byte[] expected = new byte[] { 
                0x01, 0x01, 0x01, 0x01, 
                0x01, 0x01, 0x01, 0x01,
                0x01
            }; 
        ByteArrayOutputStream actual = new ByteArrayOutputStream();
        BitOutputStream os = new BitOutputStream(actual, 9); 
        // 9-bit groups: 100000001 010000000 001000000 000100000 000010000 000001000 000000100 000000010 
        // 8-bit groups: 00000001 00000001 00000001 00000001 00000001 00000001 00000001 00000001 00000001
        os.write(0x101);
        os.write(0x80);
        os.write(0x40);
        os.write(0x20);
        os.write(0x10);
        os.write(0x08);
        os.write(0x04);
        os.write(0x02);
        os.close();			// VERY IMPORTANT!
        assertEquals(expected, actual.toByteArray());
    } 
}
