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

import org.applecommander.shrinkit.io.LittleEndianByteInputStream;
import org.junit.Assert;

import java.io.IOException;
import java.util.Date;

public class TestHelper {
	private TestHelper() {
		// Prevent construction
	}

	public static void checkDate(byte[] streamData, Date actual) throws IOException {
		try (LittleEndianByteInputStream is = new LittleEndianByteInputStream(streamData)) {
			Assert.assertEquals(is.readDate(), actual);
		}
	}
}
