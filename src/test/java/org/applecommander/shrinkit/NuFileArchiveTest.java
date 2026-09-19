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

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Perform some tests generally around an archive.
 */
public class NuFileArchiveTest {
    /**
     * This tests the finalName logic when there is a "/" for the directory separator.
     */
    @Test
    public void testSlash() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/hypercadd.shk");
        assertNotNull(inputStream);
        NuFileArchive archive = new NuFileArchive(inputStream);
        verifyFinalNames(archive, "INTRODUCTION", "GETTING.STARTED", "COMPATIBILITY", "OPSYS.OVRVIEW",
            "COMMAND.LINES", "TECHNOTE.1");
    }

    /**
     * This tests the finalName logic when there is a ":" for the directory separator.
     */
    @Test
    public void testColon() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/NuFxMess.SHK");
        assertNotNull(inputStream);
        NuFileArchive archive = new NuFileArchive(inputStream);
        verifyFinalNames(archive, "NUFX.MESSENGER", "NUFX.MESS.DOCS", "NUFX.MESS.ICON");
    }

    public void verifyFinalNames(NuFileArchive archive, String... expectedFilenames) {
        Set<String> filenames = new HashSet<>(Set.of(expectedFilenames));
        for (HeaderBlock headerBlock : archive.getHeaderBlocks()) {
            String finalName = headerBlock.getFinalFilename();
            assertTrue(filenames.contains(finalName));
            filenames.remove(finalName);
        }
        if (!filenames.isEmpty()) {
            fail("These filename were not in expected list: " + filenames);
        }
    }
}
