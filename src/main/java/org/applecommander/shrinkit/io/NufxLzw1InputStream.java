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

import org.applecommander.shrinkit.CRC16;

import java.io.IOException;
import java.io.InputStream;

/// The `NufxLzw1InputStream` reads a data fork or
/// resource fork written in the NuFX LZW/1 format.
///
/// The layout of the LZW/1 data is as follows:
///
/// * "Fork" Header
///
///   | Offset | Size | Description |
///   | :--- | :--- | :--- |
///   | +0 | Word | CRC-16 of the uncompressed data within the thread. |
///   | +2 | Byte | Low-level volume number use to format 5.25" disks. |
///   | +3 | Byte | RLE character used to decode this thread. |
///
/// * Each subsequent 4K chunk of data:
///
///   | Offset | Size | Description |
///   | :--- | :--- | :--- |
///   | +0 | Word | Length after RLE compression (if RLE is not used, length will be 4096). |
///   | +2 | Byte | A `$01` indicates LZW applied to this chunk; `$00` that LZW *was not* applied to this chunk. |
///
/// Note that the LZW string table is _cleared_ after
/// every chunk.
///
/// @author robgreene@users.sourceforge.net
public class NufxLzw1InputStream extends InputStream {
	/** This is the raw data stream with all markers and compressed data. */
	private final LittleEndianByteInputStream dataStream;
	/** Used for an LZW-only <code>InputStream</code>. */
	private LzwInputStream lzwStream;
	/** Used for an RLE-only <code>InputStream</code>. */
	private RleInputStream rleStream;
	/** Used for an LZW+RLE <code>InputStream</code>. */
	private InputStream lzwRleStream;
	/** This is the generic decompression stream from which we read. */
	private InputStream decompressionStream;
	/** Counts the number of bytes in the 4096 byte chunk. */
	private int bytesLeftInChunk;
	/** This is the CRC-16 for the uncompressed fork. */
	private int givenCrc = -1;
	/** This is the volume number for 5.25" disks. */
	private int volumeNumber;
	/** This is the RLE character to use. */
	private int rleCharacter;
	/** Used to track the CRC of data we've extracted */
	private final CRC16 dataCrc = new CRC16();
	
	/**
	 * Create the LZW/1 input stream.
	 */
	public NufxLzw1InputStream(LittleEndianByteInputStream dataStream) {
		this.dataStream = dataStream;
	}

	/**
	 * Read the next byte in the decompressed data stream.
	 */
	public int read() throws IOException {
		if (givenCrc == -1) {					// read the data or resource fork header
			givenCrc = dataStream.readWord();
			volumeNumber = dataStream.readByte();
			rleCharacter = dataStream.readByte();
			lzwStream = new LzwInputStream(new BitInputStream(dataStream, 9));
			rleStream = new RleInputStream(dataStream, rleCharacter);
			lzwRleStream = new RleInputStream(lzwStream);
		}
		if (bytesLeftInChunk == 0) {		// read the chunk header
			bytesLeftInChunk = 4096;		// NuFX always reads 4096 bytes
			lzwStream.clearDictionary();	// Always clear dictionary
			int length = dataStream.readWord();
			int lzwFlag = dataStream.readByte();
			int flag = lzwFlag + (length == 4096 ? 0 : 2);
			switch (flag) {
			case 0:		decompressionStream = dataStream;
						break;
			case 1:		decompressionStream = lzwStream;
						break;
			case 2:		decompressionStream = rleStream;
						break;
			case 3:		decompressionStream = lzwRleStream;
						break;
			default:	throw new IOException("Unknown type of decompression, flag = " + flag);
			}
		}
		// Now we can read a data byte
		int b = decompressionStream.read();
		bytesLeftInChunk--;
		dataCrc.update(b);
		return b;
	}
	
	/**
	 * Indicates if the computed CRC matches the CRC given in the data stream.
	 */
	public boolean isCrcValid() {
		return givenCrc == dataCrc.getValue();
	}
	
	// GENERATED CODE

	public int getGivenCrc() {
		return givenCrc;
	}
	public void setGivenCrc(int givenCrc) {
		this.givenCrc = givenCrc;
	}
	public int getVolumeNumber() {
		return volumeNumber;
	}
	public void setVolumeNumber(int volumeNumber) {
		this.volumeNumber = volumeNumber;
	}
	public int getRleCharacter() {
		return rleCharacter;
	}
	public void setRleCharacter(int rleCharacter) {
		this.rleCharacter = rleCharacter;
	}
	public long getDataCrc() {
		return dataCrc.getValue();
	}
}
