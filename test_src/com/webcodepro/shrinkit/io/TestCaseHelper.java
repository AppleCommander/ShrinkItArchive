package com.webcodepro.shrinkit.io;

import junit.framework.TestCase;

/**
 * Some commmon testing methods.
 * 
 * @author robgreene@users.sourceforge.net
 */
public abstract class TestCaseHelper extends TestCase {
	/**
	 * Compare two byte arrays.
	 */
	public void assertEquals(byte[] expected, byte[] actual) {
		assertEquals("Length mismatch", expected.length, actual.length);
		for (int i=0; i<expected.length; i++) {
			assertEquals("Byte mismatch at offset " + i, expected[i], actual[i]);
		}
	}
}
