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
 * Define and decode the thread_class field.
 * @author robgreene@users.sourceforge.net
 */
public enum ThreadClass {
	MESSAGE, CONTROL, DATA, FILENAME;

	/**
	 * Find the given ThreadClass.
	 * @throws IllegalArgumentException if the thread_class is unknown
	 */
	public static ThreadClass find(int threadClass) {
        return switch (threadClass) {
            case 0x0000 -> MESSAGE;
            case 0x0001 -> CONTROL;
            case 0x0002 -> DATA;
            case 0x0003 -> FILENAME;
            default -> throw new IllegalArgumentException("Unknown thread_class of " + threadClass);
        };
	}
}
