package com.webcodepro.shrinkit.io;

import junit.framework.AssertionFailedError;
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
		try {
			assertEquals("Length mismatch", expected.length, actual.length);
			for (int i=0; i<expected.length; i++) {
				assertEquals("Byte mismatch at offset " + i, expected[i], actual[i]);
			}
		} catch (AssertionFailedError err) {
			int minvalue = Math.min(expected.length, actual.length);
			for (int i=0; i<minvalue; i++) {
				assertEquals(err.getMessage() + " -- Byte mismatch at offset " + i, expected[i], actual[i]);
			}
			fail(err.getMessage() + " -- all bytes that could be compared match");
		}
	}
	/**
	 * Compare two int arrays.
	 */
	public void assertEquals(int[] expected, int[] actual) {
		try {
			assertEquals("Length mismatch", expected.length, actual.length);
			for (int i=0; i<expected.length; i++) {
				assertEquals("int mismatch at offset " + i, expected[i], actual[i]);
			}
		} catch (AssertionFailedError err) {
			int minvalue = Math.min(expected.length, actual.length);
			for (int i=0; i<minvalue; i++) {
				assertEquals(err.getMessage() + " -- Byte mismatch at offset " + i, expected[i], actual[i]);
			}
			fail(err.getMessage() + " -- all bytes that could be compared match");
		}
	}
}
