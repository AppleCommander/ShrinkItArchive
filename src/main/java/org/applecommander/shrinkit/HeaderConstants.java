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
package org.applecommander.shrinkit;

/**
 * Handy reference for constant values based on header blocks.
 * These are likely user interface oriented.
 * 
 * @author rob
 */
public interface HeaderConstants {
	public static final String[] FILE_SYS = {
		"Reserved", "ProDOS/SOS", "DOS 3.3", "DOS 3.2", "Apple II Pascal", "Macintosh HFS", "Macintosh MFS",
		"Lisa File System", "Apple CP/M", "Reserved", "MS-DOS", "High Sierra", "ISO 9660", "AppleShare"
		};
}
