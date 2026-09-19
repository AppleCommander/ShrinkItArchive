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

/**
 * This interface allows bit-related constants to be shared among
 * classes.
 *  
 * @author robgreene@users.sourceforge.net
 */
public interface BitConstants {
	/** 
	 * The low-tech way to compute a bit mask.  Allowing up to 16 bits at this time. 
	 */
    int[] BIT_MASKS = new int[] {
            0x0000, 0x0001, 0x0003, 0x0007, 0x000f, 
            0x001f, 0x003f, 0x007f, 0x00ff, 0x01ff, 
            0x03ff, 0x07ff, 0x0fff, 0x1fff, 0x3fff, 
            0x7fff, 0xffff 
        }; 
}
