package com.webcodepro.shrinkit;

import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Test;

public class CRC16Test {
	@Test
	public void testTable() {
		int[] table = CRC16.getTable();
		Assert.assertEquals(0, table[0]);
		Assert.assertEquals(0x1ef0, table[0xff]);
		System.out.println("CRC16 lookup table:");
		for (int i = 0; i < 256; i++) {
			System.out.print(Integer.toHexString(table[i]) + " ");
			if ((i + 1) % 8 == 0) System.out.println();
		}
	}

	@Test
	public void testUpdate() throws UnsupportedEncodingException {
		CRC16 crc16 = new CRC16();
		crc16.update("123456789".getBytes("UTF-8"));
		Assert.assertEquals(0x31c3, crc16.getValue());
		crc16.update("ABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes("UTF-8"));
		Assert.assertEquals(0x92cc, crc16.getValue());
		crc16.update("abcdefghijklmnopqrstuvwxyz".getBytes("UTF-8"));
		Assert.assertEquals(0xfc85, crc16.getValue());
		crc16.reset();
		crc16.update("xxx123456789xxx".getBytes("UTF-8"), 3, 9);
		Assert.assertEquals(0x31c3, crc16.getValue());
	}

	@Test
	public void testVariousValues() throws UnsupportedEncodingException {
		CRC16 crc16 = new CRC16();
		byte[] data = new byte[] { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, (byte)0x80 };
		crc16.update(data);
		Assert.assertEquals(0x2299, crc16.getValue());
	}
}