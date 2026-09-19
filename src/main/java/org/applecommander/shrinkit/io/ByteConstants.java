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
 * Provides constants for the LittleEndianByteInputStream and ByteTarget classes.
 * 
 * @author robgreene@users.sourceforge.net
 * @see LittleEndianByteInputStream
 * @see LittleEndianByteOutputStream
 */
public interface ByteConstants {
	/** Master Header Block identifier "magic" bytes. */
	byte[] NUFILE_ID = { 0x4e, (byte)0xf5, 0x46, (byte)0xe9, 0x6c, (byte)0xe5 };
	/** Header Block identifier "magic" bytes. */
	byte[] NUFX_ID = { 0x4e, (byte)0xf5, 0x46, (byte)0xd8 };
	/** Binary II identifier "magic" bytes. */
	byte[] BXY_ID = { 0x0a, 0x47, 0x4c };
	/** Apple IIgs Toolbox TimeRec seconds byte position. */
	int TIMEREC_SECOND = 0;
	/** Apple IIgs Toolbox TimeRec seconds byte position. */
	int TIMEREC_MINUTE = 1;
	/** Apple IIgs Toolbox TimeRec minutes byte position. */
	int TIMEREC_HOUR = 2;
	/** Apple IIgs Toolbox TimeRec hours byte position. */
	int TIMEREC_YEAR = 3;
	/** Apple IIgs Toolbox TimeRec year byte position. */
	int TIMEREC_DAY = 4;
	/** Apple IIgs Toolbox TimeRec day byte position. */
	int TIMEREC_MONTH = 5;
	/** Apple IIgs Toolbox TimeRec weekday (Mon, Tue, etc) byte position. */
	int TIMEREC_WEEKDAY = 7;
	/** Apple IIgs Toolbox TimeRec length. */
	int TIMEREC_LENGTH = 8;
	/** A null TimeRec */
	byte[] TIMEREC_NULL = new byte[TIMEREC_LENGTH];
}
