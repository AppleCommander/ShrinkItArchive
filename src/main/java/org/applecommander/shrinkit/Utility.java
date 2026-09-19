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

/// Some utility type methods that don't appear to belong anywhere else.
public abstract class Utility {
    /// Given a byte array, and length, strip off the high bit and create a "normal" 7-bit ASCII string.
    public static String makeString(byte[] data, int length) {
        assert data.length >= length;
        for (int i=0; i<length; i++) {
            data[i] = (byte)(data[i] & 0x7f);
        }
        return new String(data, 0, length);
    }
}
