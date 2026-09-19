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
 * Define and decode the thread_kind field.
 * @author robgreene@users.sourceforge.net
 */
public enum ThreadKind {
	ASCII_TEXT, ALLOCATED_SPACE, APPLE_IIGS_ICON, CREATE_DIRECTORY, DATA_FORK, DISK_IMAGE, RESOURCE_FORK,
	FILENAME;

	/**
	 * Find the specific ThreadKind.
	 * @throws IllegalArgumentException when the thread_kind cannot be determined
	 */
	public static ThreadKind find(int threadKind, ThreadClass threadClass) {
        return switch (threadClass) {
            case MESSAGE -> switch (threadKind) {
                case 0x0000 -> ASCII_TEXT;
                case 0x0001 -> ALLOCATED_SPACE;
                case 0x0002 -> APPLE_IIGS_ICON;
                default -> throw new IllegalArgumentException("Unknown thread_kind " + threadKind
                        + " for message thread_class of " + threadClass);
            };
            case CONTROL -> {
                if (threadKind == 0x0000) yield CREATE_DIRECTORY;
                throw new IllegalArgumentException("Unknown thread_kind " + threadKind
                        + " for control thread_class of " + threadClass);
            }
            case DATA -> switch (threadKind) {
                case 0x0000 -> DATA_FORK;
                case 0x0001 -> DISK_IMAGE;
                case 0x0002 -> RESOURCE_FORK;
                default -> throw new IllegalArgumentException("Unknown thread_kind " + threadKind
                        + " for data thread_class of " + threadClass);
            };
            case FILENAME -> {
                if (threadKind == 0x0000) yield FILENAME;
                throw new IllegalArgumentException("Unknown thread_kind " + threadKind
                        + " for filename thread_class of " + threadClass);
            }
        };
	}
}
