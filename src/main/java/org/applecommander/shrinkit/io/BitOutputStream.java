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

import java.io.IOException;
import java.io.OutputStream;

/// The BitOutputStream allows varying bit sizes to be written to the wrapped
/// OutputStream.  This is useful for LZW type compression algorithms
/// where 9-12 bit codes are used instead of the 8-bit byte.
///
/// Warning: The `write(byte[])` and `write(byte[], int, int)`
/// methods of `OutputStream` will not work appropriately with any
/// bit size > 8 bits.
///
/// @author robgreene@users.sourceforge.net
public class BitOutputStream extends OutputStream implements BitConstants {
    /** Our data target. */
    private final OutputStream os;
    /** The number of bits to write for a request.  This can be adjusted dynamically. */
    private int requestedNumberOfBits; 
    /** The current bit mask to use for a <code>write(int)</code> request. */ 
    private int bitMask; 
    /** The buffer containing our bits.  */
    private int data = 0;
    /** Number of bits remaining in our buffer */
    private int bitsOfData = 0; 
    
    /**
     * Create a BitOutputStream wrapping the given <code>OutputStream</code>
     * and writing the number of bits specified.
     */
    public BitOutputStream(OutputStream os, int startingNumberOfBits) { 
        this.os = os; 
        setRequestedNumberOfBits(startingNumberOfBits); 
    } 
    
    /**
     * Set the number of bits to be written with each call to <code>write(int)</code>.
     */
    public void setRequestedNumberOfBits(int numberOfBits) { 
        this.requestedNumberOfBits = numberOfBits; 
        this.bitMask = BIT_MASKS[numberOfBits]; 
    } 
    
    /**
     * Increase the requested number of bits by one.
     * This is the general usage and prevents client from needing to track
     * the requested number of bits or from making various method calls.
     */
    public void increaseRequestedNumberOfBits() {
    	setRequestedNumberOfBits(requestedNumberOfBits + 1);
    }
    
    /**
     * Answer with the current bit mask for the current bit size.
     */
    public int getBitMask() {
    	return bitMask;
    }
    
    /**
     * Write the number of bits to the wrapped OutputStream.
     */
    public void write(int b) throws IOException {
    	b &= bitMask;						// Ensure we don't have extra baggage
    	b <<= bitsOfData;					// Move beyond existing bits of data
    	data|= b;							// Add in the additional data
    	bitsOfData+= requestedNumberOfBits;
    	while (bitsOfData >= 8) {
    		os.write(data & 0xff);
    		data >>= 8;
    		bitsOfData-= 8;
    	}
    }
    
    /**
     * When shifting from buffer to buffer, this OutputStream also should be reset.
     * This allows the "left over" bits to be cleared.
     */
    public void clearRemainingBitsOfData() {
    	this.bitsOfData = 0;
    	this.data = 0;
    }

    /**
     * Close the output stream and write any remaining byte to the output.
     * Note that we may very well end up with extra bits if there are &lt; 8
     * bits remaining.
     */
    public void close() throws IOException {
    	if (bitsOfData > 0) {
    		write(0x00);	// forces a flush of the remaining bits in the proper order
    	}
    }

} 

