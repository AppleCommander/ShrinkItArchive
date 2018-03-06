package com.webcodepro.shrinkit.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Exercise the BitInputStream.  
 * 
 * @author robgreene@users.sourceforge.net
 */
public class BitInputStreamTest extends TestCase { 
    public void test1() throws IOException { 
        byte[] data = new byte[] { 
                0x01, 0x01, 0x01, 0x01, 
                0x01, 0x01, 0x01, 0x01 
            }; 
        BitInputStream is = new BitInputStream(new ByteArrayInputStream(data), 9); 
        // 8-bit groups: 00000001 00000001 00000001 00000001 00000001 00000001 00000001 00000001 
        // 9-bit groups: 100000001 010000000 001000000 000100000 000010000 000001000 000000100 0 
        assertEquals(0x101, is.read()); 
        assertEquals(0x080, is.read()); 
        assertEquals(0x040, is.read()); 
        assertEquals(0x020, is.read()); 
        assertEquals(0x010, is.read()); 
        assertEquals(0x008, is.read()); 
        assertEquals(0x004, is.read()); 
    } 
    
    public void testCheatin1() throws IOException {
    	byte[] data = new byte[] {
    			(byte)0x54, (byte)0x90, (byte)0x24, (byte)0x99, (byte)0x02, (byte)0x62, (byte)0x20, (byte)0x88, 
    			(byte)0x80, (byte)0x45, (byte)0x40, (byte)0x5C, (byte)0x09, (byte)0x92, (byte)0x45, (byte)0x61, 
    			(byte)0xC2, (byte)0x85, (byte)0x53, (byte)0x90, (byte)0x80, (byte)0x78, (byte)0x52, (byte)0x45, 
    			(byte)0x0A, (byte)0x88, (byte)0x21, (byte)0x4C, (byte)0x9E, (byte)0x20, (byte)0x9C, (byte)0xC2, 
    			(byte)0x42, (byte)0x61, (byte)0x90, (byte)0x88, (byte)0x13, (byte)0x2B, (byte)0x5E, (byte)0xCC, 
    			(byte)0xB8, (byte)0xB1, (byte)0x23, (byte)0x44, (byte)0x89, (byte)0x14, (byte)0x2D, (byte)0x62, 
    			(byte)0xD4, (byte)0x88, (byte)0xA4, (byte)0xC8, (byte)0x14, (byte)0x17, (byte)0x20, (byte)0x0E, 
    			(byte)0x0A, (byte)0x24, (byte)0x68, (byte)0x10, (byte)0xA1, (byte)0xC7, (byte)0x86, (byte)0x57, 
    			(byte)0x1E, (byte)0x7E, (byte)0x44, (byte)0x29, (byte)0x72, (byte)0x65, (byte)0x49, (byte)0x10, 
    			(byte)0x53, (byte)0x9E, (byte)0x80, (byte)0x28, (byte)0x12, (byte)0x44, (byte)0x0A, (byte)0x93, 
    			(byte)0x86, (byte)0x49, (byte)0x9C, (byte)0xC8, (byte)0x4C, (byte)0xD8, (byte)0xE4, (byte)0x89, 
    			(byte)0x14, (byte)0x27, (byte)0x49, (byte)0x8F, (byte)0xB8, (byte)0xD8, (byte)0x06, (byte)0xE0, 
    			(byte)0x1F, (byte)0x55, (byte)0xAB, (byte)0x55, (byte)0xAF, (byte)0x6A, (byte)0xCD, (byte)0xCA, 
    			(byte)0x15, (byte)0xAB, (byte)0xD7, (byte)0xAD, (byte)0x5F, (byte)0xBB, (byte)0x52, (byte)0xC5, 
    			(byte)0x03, (byte)0x00
    		};
    	BitInputStream is = new BitInputStream(new ByteArrayInputStream(data), 9);
    	int b = 0;
    	while (b != -1) {
    		b = is.read();
    		System.out.printf("%04x ", b);
    	}
    }

	/**
	 * Simply ensure that we read the right bit codes from the LZW stream.  This starts with 9 bits and
	 * ultimately might work up to a 12 bit code.
	 */
	public void testBitDecoder() throws IOException {
		BitInputStream is = new BitInputStream(new ByteArrayInputStream(getHgrColorsLzw1()), 9);
		int[] expected = new int[] { 0x0db, 0x000, 0x007, 0x0db, 0x055, 0x103, 0x02a, 0x103, 0x000, 0x06f, 0x0db };
		for (int i=0; i<expected.length; i++) {
			assertEquals("Testing value #" + i, expected[i], is.read());
		}
	}
	
	protected byte[] getHgrColorsLzw1() {
		return new byte[] {
				(byte)0xdb, 0x00, 0x1c, (byte)0xd8, 0x56, 0x65, (byte)0xa0, (byte)0x8a, 
				(byte)0x81, 0x00, (byte)0xde, 0x6c, 0x3b, 0x48, 0x10, (byte)0xa1, 
				(byte)0xc2, 0x3f, 0x0f, 0x02, (byte)0xfe, (byte)0x93, 0x48, 0x11, 
				(byte)0xc0, 0x44, (byte)0x8b, 0x15, 0x2f, 0x6a, (byte)0xcc, (byte)0xc8, 
				0x11, 0x23, (byte)0x80, 0x73, 0x00
			};
	}
} 
